package swishlive.com.swishlive;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.MediaCodec;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.octiplex.android.rtmp.AACAudioFrame;
import com.octiplex.android.rtmp.AACAudioHeader;
import com.octiplex.android.rtmp.H264VideoFrame;
import com.octiplex.android.rtmp.RtmpConnectionListener;
import com.octiplex.android.rtmp.RtmpMuxer;
import com.octiplex.android.rtmp.Time;

import org.json.JSONException;
import org.parceler.Parcels;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class StreamingActivity extends AppCompatActivity implements RtmpConnectionListener, ScreenRecorder.ScreenRecordListener, ScreenRecorder.RequestCameraRender, SportFragment.OnFragmentClickerInteractionListener {

    private static final int REQUEST_STREAM = 2;
    private static String[] PERMISSIONS_STREAM = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAPTURE_VIDEO_OUTPUT,
            Manifest.permission.CAPTURE_AUDIO_OUTPUT,
    };
    // RTMP Constraints
    private static final String DEFAULT_RMTP_HOST = "rtmp-api.facebook.com";
    private static final String TAG = ".StreamingAvctivity";
    private static final int DEFAULT_RTMP_PORT = 80;
    private static final String DEFAULT_APP_NAME = "rtmp";

    private ScreenRecorder mScreenRecorder;
    private RtmpMuxer mRtmpMuxer;
    private CameraManager mCameraManager;
    private CameraDevice mCameraDevice;
    private String mFrontCameraId;
    private CaptureRequest mCaptureRequestRecord;
    private CameraCaptureSession mCameraCaptureSession;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    private Surface mSurfacePreview;
    private boolean streaming = false;
    private CameraDevice.StateCallback mCameraDeviceCallback = new android.hardware.camera2.CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            mCameraDevice = camera;
            final int width = 640;
            final int height = 480;
            final int bitrate = 1000000;
            final int audioSampleRate = 48000;
            final int audioBitrate = 128000;

            File file = new File(Environment.getExternalStorageDirectory(),
                    "record-" + width + "x" + height + "-" + System.currentTimeMillis() + ".mp4");

            mScreenRecorder = new ScreenRecorder(width, height, bitrate, 1, audioSampleRate, audioBitrate, file.getAbsolutePath(), StreamingActivity.this, StreamingActivity.this);
            mScreenRecorder.start();

        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {

        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            //@IntDef(value = {CameraDevice.StateCallback.ERROR_CAMERA_IN_USE, CameraDevice.StateCallback.ERROR_MAX_CAMERAS_IN_USE, CameraDevice.StateCallback.ERROR_CAMERA_DISABLED, CameraDevice.StateCallback.ERROR_CAMERA_DEVICE, CameraDevice.StateCallback.ERROR_CAMERA_SERVICE})
        }
    };
    private CameraCaptureSession.StateCallback mCaptureSessionCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            mCameraCaptureSession = session;
            try {
                mCameraCaptureSession.setRepeatingRequest(
                        mCaptureRequestRecord,
                        new CameraCaptureSession.CaptureCallback() {
                        },
                        mBackgroundHandler
                );
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
            String str = "fafea";
        }
    };

    private TextView textViewIncrust;
    private TextView textViewLive;

    FloatingActionButton floatingActionButton;
    int sportKey;
    private String streamingKey;
    private Fragment consoleFragment;

    static GameModel gameModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streaming);

        Intent intent = getIntent();
        sportKey = intent.getIntExtra(MatchConfigurationActivity.SPORT_KEY, -1);
        gameModel = Parcels.unwrap(intent.getParcelableExtra(MatchConfigurationActivity.GAME_MODEL));
        streamingKey = intent.getStringExtra(MatchConfigurationActivity.STREAMING_KEY);

        final FragmentManager fragmentManager = getSupportFragmentManager();

        SurfaceView surfaceViewPreview = (SurfaceView) findViewById(R.id.cameraPreview_surfaceView);
        mSurfacePreview = surfaceViewPreview.getHolder().getSurface();


        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingActionButton.setBackgroundResource(R.drawable.swish);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (consoleFragment != null) {

                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        //transaction.setCustomAnimations(R.animator.enter_from_right, R.animator.exit_to_right);

                        if (consoleFragment.isHidden()) {
                            transaction.show(consoleFragment);
                        } else {
                            transaction.hide(consoleFragment);
                        }
                        transaction.commit();
                    }
            }
        });
        mCameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        try {
            String[] cameraIds = mCameraManager.getCameraIdList();
            if (cameraIds.length != 0) {
                for (int i = 0; i < cameraIds.length; i++) {
                    if (mCameraManager.getCameraCharacteristics(cameraIds[i]).get(CameraCharacteristics.LENS_FACING)
                            == CameraCharacteristics.LENS_FACING_FRONT) {
                        mFrontCameraId = cameraIds[i];
                    }
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        verifyPermissions();
    }

    public void verifyPermissions() {
        for (String permission : PERMISSIONS_STREAM) {
            int permissionResult = ActivityCompat.checkSelfPermission(StreamingActivity.this, permission);
            if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        StreamingActivity.this,
                        PERMISSIONS_STREAM,
                        REQUEST_STREAM
                );
                return;
            }
        }
    }

    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    private void openCamera() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mCameraManager.openCamera(mFrontCameraId, mCameraDeviceCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STREAM) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
        }
    }



    public static GameModel getGameModel() {

        return gameModel;
    }

    @Override
    public void onFragmentClickerInteraction() {
        if (mRtmpMuxer != null || mScreenRecorder != null) {
            release();
            //textViewLive.setVisibility(View.INVISIBLE);
        } else {
            // Always call start method from a background thread.

            mRtmpMuxer = new RtmpMuxer(DEFAULT_RMTP_HOST, DEFAULT_RTMP_PORT, new Time()
            {
                @Override
                public long getCurrentTimestamp()
                {
                    return System.currentTimeMillis();
                }
            });

            new AsyncTask<Void, Void, Void>()
            {
                @Override
                protected Void doInBackground(Void... params)
                {
                    mRtmpMuxer.start(StreamingActivity.this, DEFAULT_APP_NAME, null, null);

                    return null;
                }
            }.execute();

            //textViewLive.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        release();
    }

    private void release()
    {
        if (mScreenRecorder != null) {
            mScreenRecorder.quit();
            mScreenRecorder = null;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (mRtmpMuxer != null) {
                    try {
                        mRtmpMuxer.deleteStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mRtmpMuxer.stop();
                    mRtmpMuxer = null;
                }
            }
        }).start();
    }

    @Override
    public void onConnected() {
        // Muxer is connected to the RTMP server, you can create a stream to publish data
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground(Void... params)
            {
                try {
                    mRtmpMuxer.createStream(streamingKey);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();

    }

    @Override
    public void onReadyToPublish() {
        startBackgroundThread();
        openCamera();
    }

    @Override
    public void onConnectionError(@NonNull IOException e) {
        release();
    }


    @Override
    public void OnReceiveScreenRecordData(final MediaCodec.BufferInfo bufferInfo, final boolean isHeader, final long timestamp, final byte[] data) {

        // Always call postVideo method from a background thread.
        try {
            mRtmpMuxer.postVideo(new H264VideoFrame() {
                @Override
                public boolean isHeader() {
                    return isHeader;
                }

                @Override
                public long getTimestamp() {
                    return timestamp;
                }

                @NonNull
                @Override
                public byte[] getData() {
                    return data;
                }

                @Override
                public boolean isKeyframe() {
                    return !isHeader;
                }
            });
        } catch (IOException e) {
            // An error occured while sending the video frame to the server
        }
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/live_videos",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        textViewLive.setVisibility(View.VISIBLE);
                        textViewLive.postDelayed(new Runnable() {
                            public void run() {
                                textViewLive.setVisibility(View.INVISIBLE);
                            }
                        }, 1000);
                        try {
                            String responseCode = response.getJSONObject().get("data").toString();
                            Log.i(TAG,responseCode);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            textViewLive.setVisibility(View.INVISIBLE);
                        }
                    }
                }
        ).executeAsync();

    }


    @Override
    public void OnReceiveAudioRecordData(final MediaCodec.BufferInfo bufferInfo, final boolean isHeader, final long timestamp, final byte[] data, final int numberOfChannel, final int sampleSizeIndex) {
        if (isHeader) {
            mRtmpMuxer.setAudioHeader(new AACAudioHeader() {
                @NonNull
                @Override
                public byte[] getData() {
                    return data;
                }

                @Override
                public int getNumberOfChannels() {
                    return numberOfChannel;
                }

                @Override
                public int getSampleSizeIndex() {
                    return sampleSizeIndex;
                }
            });
        } else {
            try {
                mRtmpMuxer.postAudio(new AACAudioFrame() {
                    @Override
                    public long getTimestamp() {
                        return timestamp;
                    }

                    @NonNull
                    @Override
                    public byte[] getData() {
                        return data;
                    }
                });
            } catch (IOException e) {
                // An error occured while sending the audio frame to the server
            }
        }
    }

    @Override
    public void setCameraRender(Surface surface) {
        try {
            CaptureRequest.Builder captureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            captureRequestBuilder.addTarget(mSurfacePreview);
            mCaptureRequestRecord = captureRequestBuilder.build();

            mCameraDevice.createCaptureSession(Arrays.asList(surface, mSurfacePreview), mCaptureSessionCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}
