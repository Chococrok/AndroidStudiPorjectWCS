package com.wcs.facebooklogintest;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.octiplex.android.rtmp.H264VideoFrame;
import com.octiplex.android.rtmp.RtmpConnectionListener;
import com.octiplex.android.rtmp.RtmpMuxer;
import com.octiplex.android.rtmp.Time;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class StreamingActivity extends AppCompatActivity implements SurfaceHolder.Callback2, RtmpConnectionListener {

    private static final String TAG = "MainActivity";
    private static final String[] REQUEST_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_CODE_CAMERA = 1;

    private String mStreamKey;
    private boolean mReadyToStream = false;

    private CameraManager mCameraManager;
    private VideoEncoderCore mVideoEncoderCore;
    private String mFrontCameraId;
    private CameraDevice mCameraDevice;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    private SurfaceView mSurfacView;
    private Button buttonRecord;
    private boolean isRecording = false;
    private ArrayList<Surface> mSurfaces;
    private CaptureRequest mCaptureRequestPreview;
    private CaptureRequest mCaptureRequestRecord;
    private CameraCaptureSession mCameraCaptureSession;
    private CameraCaptureSession.StateCallback mCaptureSessionCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            mCameraCaptureSession = session;
            try {
                mCameraCaptureSession.setRepeatingRequest(
                        mCaptureRequestPreview,
                        new CameraCaptureSession.CaptureCallback() {},
                        mBackgroundHandler
                );
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {

        }
    };
    private CameraDevice.StateCallback mCameraDeviceCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            mCameraDevice = camera;
            createCaptureSession();

            /*try {
                mCameraManager.setTorchMode(mFrontCameraId, true);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }*/
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {

        }

        @Override
        public void onError(@NonNull CameraDevice camera,  int error) {
            //@IntDef(value = {CameraDevice.StateCallback.ERROR_CAMERA_IN_USE, CameraDevice.StateCallback.ERROR_MAX_CAMERAS_IN_USE, CameraDevice.StateCallback.ERROR_CAMERA_DISABLED, CameraDevice.StateCallback.ERROR_CAMERA_DEVICE, CameraDevice.StateCallback.ERROR_CAMERA_SERVICE})
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.streaming_activity);

        mSurfacView = (SurfaceView) findViewById(R.id.surfaceView);
        mSurfacView.getHolder().addCallback(StreamingActivity.this);

        buttonRecord =(Button) findViewById(R.id.buttonRecord);
        buttonRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mReadyToStream){
                    Toast.makeText(StreamingActivity.this, "Un peu de patience je me connecte à facebook...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isRecording){
                    try {
                        mCameraCaptureSession.abortCaptures();
                        mCameraCaptureSession.setRepeatingRequest(
                                mCaptureRequestPreview,
                                new CameraCaptureSession.CaptureCallback() {},
                                mBackgroundHandler
                        );
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                    isRecording = false;
                    buttonRecord.setText("Play");
                }
                else{
                    isRecording = true;
                    try {
                        mCameraCaptureSession.abortCaptures();
                        mCameraCaptureSession.setRepeatingRequest(mCaptureRequestRecord, new CameraCaptureSession.CaptureCallback() {
                            @Override
                            public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                                super.onCaptureCompleted(session, request, result);
                                mVideoEncoderCore.drainEncoder(false);

                            }

                            @Override
                            public void onCaptureSequenceAborted(@NonNull CameraCaptureSession session, int sequenceId) {
                                super.onCaptureSequenceAborted(session, sequenceId);
                                mVideoEncoderCore.drainEncoder(true);
                            }
                        }, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                    buttonRecord.setText("Stop");
                }
            }
        });
        mCameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        try {
            String[] cameraIds = mCameraManager.getCameraIdList();
            if (cameraIds.length != 0) {
                for (int i = 0; i < cameraIds.length; i++){
                    if (mCameraManager.getCameraCharacteristics(cameraIds[i]).get(CameraCharacteristics.LENS_FACING)
                    == CameraCharacteristics.LENS_FACING_FRONT){
                        mFrontCameraId = cameraIds[i];
                    }
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoEncoderCore != null) {
            mVideoEncoderCore.release();
        }
    }

    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    private void openCamera(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUEST_PERMISSIONS, REQUEST_CODE_CAMERA);
            return;
        }
        try {
            mCameraManager.openCamera(mFrontCameraId, mCameraDeviceCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        try {
            mCameraManager.openCamera(mFrontCameraId, mCameraDeviceCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceRedrawNeeded(SurfaceHolder holder) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startBackgroundThread();
        openCamera();
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "me/live_videos",
                null,
                HttpMethod.POST,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            String url = response.getJSONObject().get("stream_url").toString();
                            String[] splitedUrl = url.split("rtmp/");
                            mStreamKey = splitedUrl[1];
                            Log.d(TAG, url);
                            Log.d(TAG, mStreamKey);
                            initMuxer();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private String getVideoFilePath(Context context) {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + "/"
                + System.currentTimeMillis() + ".mp4";
    }

    public void createCaptureSession(){
        try {
            mVideoEncoderCore = new VideoEncoderCore(
                    1280,
                    720,
                    3000000,
                    new File(getVideoFilePath(this))
            );

            Surface previewSurface = mSurfacView.getHolder().getSurface();
            Surface encoderSurface = mVideoEncoderCore.getInputSurface();
            CaptureRequest.Builder captureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(previewSurface);
            mSurfaces = new ArrayList<>();
            mSurfaces.add(previewSurface);
            mSurfaces.add(encoderSurface);
            mCaptureRequestPreview = captureRequestBuilder.build();
            captureRequestBuilder.addTarget(encoderSurface);
            mCaptureRequestRecord = captureRequestBuilder.build();

            mCameraDevice.createCaptureSession(mSurfaces, mCaptureSessionCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (IOException io) {
        }
    }

    private RtmpMuxer muxer;

    private void initMuxer() {
        muxer = new RtmpMuxer("rtmp-api.facebook.com", 80, new Time()
        {
            @Override
            public long getCurrentTimestamp() {
                return System.currentTimeMillis();
            }
        });

        // Always call start method from a background thread.
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground(Void... params) {
                muxer.start(StreamingActivity.this, "rtmp", null, null);
                return null;
            }
        }.execute();
    }
    @Override
    public void onConnected() {
        Log.d(TAG, "Connection established");
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    muxer.createStream(mStreamKey);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();

    }

    @Override
    public void onReadyToPublish() {
        mVideoEncoderCore.setRTMPMuxer(muxer);
        AudioRecorder m = new AudioRecorder();
        try {
            m.configure(muxer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        m.startAudio();
        mReadyToStream = true;
        Toast.makeText(this, "prêt à envoyer le flux vidéo !", Toast.LENGTH_SHORT).show();
        m.run();
    }

    @Override
    public void onConnectionError(@NonNull IOException e) {

    }
}
