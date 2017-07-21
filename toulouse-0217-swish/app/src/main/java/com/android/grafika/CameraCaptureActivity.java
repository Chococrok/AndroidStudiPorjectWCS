/*
 * Copyright 2013 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.grafika;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.MediaCodec;
import android.opengl.EGL14;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.TextView;

import com.android.grafika.gles.FullFrameRect;
import com.android.grafika.gles.Texture2dProgram;
import com.android.texample2.GLText;
import com.octiplex.android.rtmp.AACAudioFrame;
import com.octiplex.android.rtmp.AACAudioHeader;
import com.octiplex.android.rtmp.H264VideoFrame;
import com.octiplex.android.rtmp.RtmpConnectionListener;
import com.octiplex.android.rtmp.RtmpMuxer;
import com.octiplex.android.rtmp.Time;

import org.parceler.Parcels;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import swishlive.com.swishlive.BasketFragment;
import swishlive.com.swishlive.BasketballModel;
import swishlive.com.swishlive.FootballFragment;
import swishlive.com.swishlive.FootballModel;
import swishlive.com.swishlive.GameModel;
import swishlive.com.swishlive.HandballModel;
import swishlive.com.swishlive.MatchConfigurationActivity;
import swishlive.com.swishlive.R;
import swishlive.com.swishlive.RugbyFragment;
import swishlive.com.swishlive.RugbyModel;
import swishlive.com.swishlive.ScreenRecorder;
import swishlive.com.swishlive.SportFragment;
import swishlive.com.swishlive.TennisModel;
import swishlive.com.swishlive.VolleyFragment;
import swishlive.com.swishlive.VolleyModel;

/**
 * Shows the camera preview on screen while simultaneously recording it to a .mp4 file.
 * <p>
 * Every time we receive a frame from the camera, we need to:
 * <ul>
 * <li>Render the frame to the SurfaceView, on GLSurfaceView's renderer thread.
 * <li>Render the frame to the mediacodec's input surface, on the encoder thread, if
 *     recording is enabled.
 * </ul>
 * <p>
 * At any given time there are four things in motion:
 * <ol>
 * <li>The UI thread, embodied by this Activity.  We must respect -- or work around -- the
 *     app lifecycle changes.  In particular, we need to release and reacquire the Camera
 *     so that, if the user switches away from us, we're not preventing another app from
 *     using the camera.
 * <li>The Camera, which will busily generate preview frames once we hand it a
 *     SurfaceTexture.  We'll get notifications on the main UI thread unless we define a
 *     Looper on the thread where the SurfaceTexture is created (the GLSurfaceView renderer
 *     thread).
 * <li>The video encoder thread, embodied by TextureMovieEncoder.  This needs to share
 *     the Camera preview external texture with the GLSurfaceView renderer, which means the
 *     EGLContext in this thread must be created with a reference to the renderer thread's
 *     context in hand.
 * <li>The GLSurfaceView renderer thread, embodied by CameraSurfaceRenderer.  The thread
 *     is created for us by GLSurfaceView.  We don't get callbacks for pause/resume or
 *     thread startup/shutdown, though we could generate messages from the Activity for most
 *     of these things.  The EGLContext created on this thread must be shared with the
 *     video encoder, and must be used to create a SurfaceTexture that is used by the
 *     Camera.  As the creator of the SurfaceTexture, it must also be the one to call
 *     updateTexImage().  The renderer thread is thus at the center of a multi-thread nexus,
 *     which is a bit awkward since it's the thread we have the least control over.
 * </ol>
 * <p>
 * GLSurfaceView is fairly painful here.  Ideally we'd create the video encoder, create
 * an EGLContext for it, and pass that into GLSurfaceView to share.  The API doesn't allow
 * this, so we have to do it the other way around.  When GLSurfaceView gets torn down
 * (say, because we rotated the device), the EGLContext gets tossed, which means that when
 * it comes back we have to re-create the EGLContext used by the video encoder.  (And, no,
 * the "preserve EGLContext on pause" feature doesn't help.)
 * <p>
 * We could simplify this quite a bit by using TextureView instead of GLSurfaceView, but that
 * comes with a performance hit.  We could also have the renderer thread drive the video
 * encoder directly, allowing them to work from a single EGLContext, but it's useful to
 * decouple the operations, and it's generally unwise to perform disk I/O on the thread that
 * renders your UI.
 * <p>
 * We want to access Camera from the UI thread (setup, teardown) and the renderer thread
 * (configure SurfaceTexture, start preview), but the API says you can only access the object
 * from a single thread.  So we need to pick one thread to own it, and the other thread has to
 * access it remotely.  Some things are simpler if we let the renderer thread manage it,
 * but we'd really like to be sure that Camera is released before we leave onPause(), which
 * means we need to make a synchronous call from the UI thread into the renderer thread, which
 * we don't really have full control over.  It's less scary to have the UI thread own Camera
 * and have the renderer call back into the UI thread through the standard Handler mechanism.
 * <p>
 * (The <a href="http://developer.android.com/training/camera/cameradirect.html#TaskOpenCamera">
 * camera docs</a> recommend accessing the camera from a non-UI thread to avoid bogging the
 * UI thread down.  Since the GLSurfaceView-managed renderer thread isn't a great choice,
 * we might want to create a dedicated camera thread.  Not doing that here.)
 * <p>
 * With three threads working simultaneously (plus Camera causing periodic events as frames
 * arrive) we have to be very careful when communicating state changes.  In general we want
 * to send a message to the thread, rather than directly accessing state in the object.
 * <p>
 * &nbsp;
 * <p>
 * To exercise the API a bit, the video encoder is required to survive Activity restarts.  In the
 * current implementation it stops recording but doesn't stop time from advancing, so you'll
 * see a pause in the video.  (We could adjust the timer to make it seamless, or output a
 * "paused" message and hold on that in the recording, or leave the Camera running so it
 * continues to generate preview frames while the Activity is paused.)  The video encoder object
 * is managed as a static property of the Activity.
 */
public class CameraCaptureActivity extends AppCompatActivity
        implements SurfaceTexture.OnFrameAvailableListener,
        RtmpConnectionListener, ScreenRecorder.ScreenRecordListener,
        ScreenRecorder.RequestCameraRender, SportFragment.OnFragmentClickerInteractionListener {

    private static final String TAG = "CameraCaptureActivity";
    private static final boolean VERBOSE = false;

    // Camera filters; must match up with cameraFilterNames in strings.xml
    static final int FILTER_NONE = 0;
    static final int FILTER_BLACK_WHITE = 1;
    static final int FILTER_BLUR = 2;
    static final int FILTER_SHARPEN = 3;
    static final int FILTER_EDGE_DETECT = 4;
    static final int FILTER_EMBOSS = 5;

    private GLSurfaceView mGLView;
    private CameraSurfaceRenderer mRenderer;
    private Camera mCamera;
    private CameraHandler mCameraHandler;
    private boolean mRecordingEnabled;      // controls button state

    private int mCameraPreviewWidth, mCameraPreviewHeight;

    // this is static so it survives activity restarts
    private static TextureMovieEncoder sVideoEncoder = new TextureMovieEncoder();


    private RtmpMuxer mRtmpMuxer;
    private ScreenRecorder mScreenRecorder;
    private static final String DEFAULT_RMTP_HOST = "rtmp-api.facebook.com";
    private static final int DEFAULT_RTMP_PORT = 80;
    private static final String DEFAULT_APP_NAME = "rtmp";
    private String streamingKey;
    private FloatingActionButton floatingActionButton;
    private int sportKey;
    private static SportFragment consoleFragment;
    private static GameModel gameModel;
    private TextView textViewLive;
    private Chronometer liveChronometer;

    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streaming);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent intentFromMacthConfigurationActivity = getIntent();
        streamingKey = intentFromMacthConfigurationActivity.getStringExtra(MatchConfigurationActivity.STREAMING_KEY);
        sportKey = intentFromMacthConfigurationActivity.getIntExtra(MatchConfigurationActivity.SPORT_KEY, -1);
        gameModel = Parcels.unwrap(intentFromMacthConfigurationActivity.getParcelableExtra(MatchConfigurationActivity.GAME_MODEL));
        textViewLive = (TextView) findViewById(R.id.textViewLive);
        liveChronometer = (Chronometer) findViewById(R.id.liveChronometer);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.close_stream));


        final FragmentManager fragmentManager = getSupportFragmentManager();
        generateSportModel(sportKey, fragmentManager);


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

        File outputFile = new File(getFilesDir(), "camera-test.mp4");


        mCameraHandler = new CameraHandler(this);

        mRecordingEnabled = sVideoEncoder.isRecording();

        // Configure the GLSurfaceView.  This will start the Renderer thread, with an
        // appropriate EGL context.
        mGLView = (GLSurfaceView) findViewById(R.id.cameraPreview_surfaceView);
        mGLView.setEGLContextClientVersion(2);     // select GLES 2.0
        mRenderer = new CameraSurfaceRenderer(mCameraHandler, sVideoEncoder, outputFile, CameraCaptureActivity.this);
        mGLView.setRenderer(mRenderer);
        mGLView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        Log.d(TAG, "onCreate complete: " + this);


        if (mRtmpMuxer != null || mScreenRecorder != null) {
            release();
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
                    mRtmpMuxer.start(CameraCaptureActivity.this, DEFAULT_APP_NAME, null, null);
                    return null;
                }
            }.execute();
        }
    }

    public void generateSportModel(int sportKey, FragmentManager fragmentManager) {

        switch (sportKey) {

            case 0 :
                gameModel.setSportModel(new VolleyModel());
                consoleFragment = new VolleyFragment();
                fragmentManager.beginTransaction().add(R.id.fragmentContainer, consoleFragment).commit();
                break;

            case 1 :
                gameModel.setSportModel(new BasketballModel());
                consoleFragment = new BasketFragment();
                fragmentManager.beginTransaction().add(R.id.fragmentContainer, consoleFragment).commit();
                break;

            /*
            Tennis not available ATM
            */
            case 2 :
                gameModel.setSportModel(new TennisModel());
                break;

            case 3 :
                gameModel.setSportModel(new HandballModel());
                consoleFragment = new FootballFragment();
                fragmentManager.beginTransaction().add(R.id.fragmentContainer, consoleFragment).commit();
                break;

            case 4 :
                gameModel.setSportModel(new FootballModel());
                consoleFragment = new FootballFragment();
                fragmentManager.beginTransaction().add(R.id.fragmentContainer, consoleFragment).commit();
                break;

            case 5 :
                gameModel.setSportModel(new RugbyModel());
                consoleFragment = new RugbyFragment();
                fragmentManager.beginTransaction().add(R.id.fragmentContainer, consoleFragment).commit();
                break;
        }
    }

    public static GameModel getGameModel() {

        return gameModel;
    }

    public static SportFragment getFragment() {

        return consoleFragment;
    }




    @Override
    protected void onResume() {
        Log.d(TAG, "onResume -- acquiring camera");
        super.onResume();
        openCamera(1280, 720);      // updates mCameraPreviewWidth/Height



        // Set the preview aspect ratio.
        AspectFrameLayout layout = (AspectFrameLayout) findViewById(R.id.cameraPreview_afl);
        layout.setAspectRatio((double) mCameraPreviewWidth / mCameraPreviewHeight);

        mGLView.onResume();
        mGLView.queueEvent(new Runnable() {
            @Override public void run() {
                mRenderer.setCameraPreviewSize(mCameraPreviewWidth, mCameraPreviewHeight);
            }
        });
        Log.d(TAG, "onResume complete: " + this);
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause -- releasing camera");
        super.onPause();
        releaseCamera();
        mGLView.queueEvent(new Runnable() {
            @Override public void run() {
                // Tell the renderer that it's about to be paused so it can clean up.
                mRenderer.notifyPausing();
            }
        });
        mGLView.onPause();
        Log.d(TAG, "onPause complete");
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        mCameraHandler.invalidateHandler();     // paranoia
    }


    @Override
    public void onFragmentClickerInteraction() {
        mRecordingEnabled = !mRecordingEnabled;
        if(mRecordingEnabled) {
            textViewLive.setVisibility(View.VISIBLE);
            liveChronometer.setBase(SystemClock.elapsedRealtime());
            liveChronometer.start();
            liveChronometer.setVisibility(View.VISIBLE);
        }else{
            mProgressDialog.show();
            textViewLive.setVisibility(View.INVISIBLE);
            liveChronometer.stop();
            liveChronometer.setVisibility(View.INVISIBLE);
            release();

        }
        mGLView.queueEvent(new Runnable() {
            @Override public void run() {
                // notify the renderer that we want to change the encoder's state
                mRenderer.changeRecordingState(mRecordingEnabled);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(mRecordingEnabled){
            mRecordingEnabled = !mRecordingEnabled;

            mProgressDialog.show();
            textViewLive.setVisibility(View.INVISIBLE);
            liveChronometer.stop();
            liveChronometer.setVisibility(View.INVISIBLE);

            mGLView.queueEvent(new Runnable() {
                @Override public void run() {
                    // notify the renderer that we want to change the encoder's state
                    mRenderer.changeRecordingState(mRecordingEnabled);
                }
            });

            release();
        }
        else {
            super.onBackPressed();
        }
    }

    /**
     * Opens a camera, and attempts to establish preview mode at the specified width and height.
     * <p>
     * Sets mCameraPreviewWidth and mCameraPreviewHeight to the actual width/height of the preview.
     */
    private void openCamera(int desiredWidth, int desiredHeight) {
        if (mCamera != null) {
            throw new RuntimeException("camera already initialized");
        }

        Camera.CameraInfo info = new Camera.CameraInfo();

        // Try to find a front-facing camera (e.g. for videoconferencing).
        int numCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numCameras; i++) {
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                mCamera = Camera.open(i);
                break;
            }
        }
        if (mCamera == null) {
            Log.d(TAG, "No front-facing camera found; opening default");
            mCamera = Camera.open();    // opens first back-facing camera
        }
        if (mCamera == null) {
            throw new RuntimeException("Unable to open camera");
        }

        Camera.Parameters parms = mCamera.getParameters();

        CameraUtils.choosePreviewSize(parms, desiredWidth, desiredHeight);

        // Give the camera a hint that we're recording video.  This can have a big
        // impact on frame rate.
        parms.setRecordingHint(true);

        // leave the frame rate set to default
        mCamera.setParameters(parms);

        int[] fpsRange = new int[2];
        Camera.Size mCameraPreviewSize = parms.getPreviewSize();
        parms.getPreviewFpsRange(fpsRange);
        String previewFacts = mCameraPreviewSize.width + "x" + mCameraPreviewSize.height;
        if (fpsRange[0] == fpsRange[1]) {
            previewFacts += " @" + (fpsRange[0] / 1000.0) + "fps";
        } else {
            previewFacts += " @[" + (fpsRange[0] / 1000.0) +
                    " - " + (fpsRange[1] / 1000.0) + "] fps";
        }

        mCameraPreviewWidth = mCameraPreviewSize.width;
        mCameraPreviewHeight = mCameraPreviewSize.height;
    }

    /**
     * Stops camera preview, and releases the camera to the system.
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            Log.d(TAG, "releaseCamera -- done");
        }
    }

    /**
     * onClick handler for "record" button.
     */


    @Override
    public void onConnected() {
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
        final int width = 1280;
        final int height = 720;
        final int bitrate = 3500000;
        final int audioSampleRate = 48000;
        final int audioBitrate = 128000;

        File file = new File(Environment.getExternalStorageDirectory(),
                "record-" + width + "x" + height + "-" + System.currentTimeMillis() + ".mp4");

        mScreenRecorder = new ScreenRecorder(width, height, bitrate, 1, audioSampleRate, audioBitrate, file.getAbsolutePath(), CameraCaptureActivity.this, CameraCaptureActivity.this);
        mScreenRecorder.start();
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
                    mProgressDialog.dismiss();
                    CameraCaptureActivity.this.finish();
                }
            }
        }).start();
    }

//    /**
//     * onClick handler for "rebind" checkbox.
//     */
//    public void clickRebindCheckbox(View unused) {
//        CheckBox cb = (CheckBox) findViewById(R.id.rebindHack_checkbox);
//        TextureRender.sWorkAroundContextProblem = cb.isChecked();
//    }

    /**
     * Connects the SurfaceTexture to the Camera preview output, and starts the preview.
     */
    private void handleSetSurfaceTexture(SurfaceTexture st) {
        st.setOnFrameAvailableListener(this);
        try {
            mCamera.setPreviewTexture(st);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        mCamera.startPreview();
    }

    @Override
    public void onFrameAvailable(SurfaceTexture st) {
        // The SurfaceTexture uses this to signal the availability of a new frame.  The
        // thread that "owns" the external texture associated with the SurfaceTexture (which,
        // by virtue of the context being shared, *should* be either one) needs to call
        // updateTexImage() to latch the buffer.
        //
        // Once the buffer is latched, the GLSurfaceView thread can signal the encoder thread.
        // This feels backward -- we want recording to be prioritized over rendering -- but
        // since recording is only enabled some of the time it's easier to do it this way.
        //
        // Since GLSurfaceView doesn't establish a Looper, this will *probably* execute on
        // the main UI thread.  Fortunately, requestRender() can be called from any thread,
        // so it doesn't really matter.
        if (VERBOSE) Log.d(TAG, "ST onFrameAvailable");
        mGLView.requestRender();
    }

    private Surface screenRecorderSurface;
    @Override
    public void setCameraRender(Surface surface) {
        this.screenRecorderSurface = surface;
        mRenderer.setSurface(surface);
    }

    /**
     * Handles camera operation requests from other threads.  Necessary because the Camera
     * must only be accessed from one thread.
     * <p>
     * The object is created on the UI thread, and all handlers run there.  Messages are
     * sent from other threads, using sendMessage().
     */
    static class CameraHandler extends Handler {
        public static final int MSG_SET_SURFACE_TEXTURE = 0;

        // Weak reference to the Activity; only access this from the UI thread.
        private WeakReference<CameraCaptureActivity> mWeakActivity;

        public CameraHandler(CameraCaptureActivity activity) {
            mWeakActivity = new WeakReference<CameraCaptureActivity>(activity);
        }

        /**
         * Drop the reference to the activity.  Useful as a paranoid measure to ensure that
         * attempts to access a stale Activity through a handler are caught.
         */
        public void invalidateHandler() {
            mWeakActivity.clear();
        }

        @Override  // runs on UI thread
        public void handleMessage(Message inputMessage) {
            int what = inputMessage.what;
            Log.d(TAG, "CameraHandler [" + this + "]: what=" + what);

            CameraCaptureActivity activity = mWeakActivity.get();
            if (activity == null) {
                Log.w(TAG, "CameraHandler.handleMessage: activity is null");
                return;
            }

            switch (what) {
                case MSG_SET_SURFACE_TEXTURE:
                    activity.handleSetSurfaceTexture((SurfaceTexture) inputMessage.obj);
                    break;
                default:
                    throw new RuntimeException("unknown msg " + what);
            }
        }
    }
}

/**
 * Renderer object for our GLSurfaceView.
 * <p>
 * Do not call any methods here directly from another thread -- use the
 * GLSurfaceView#queueEvent() call.
 */
class CameraSurfaceRenderer implements GLSurfaceView.Renderer{
    private static final String TAG = "CameraSurfaceRenderer";
    private static final boolean VERBOSE = false;

    private static final int TEXT_SIZE = 50;

    private static final int RECORDING_OFF = 0;
    private static final int RECORDING_ON = 1;
    private static final int RECORDING_RESUMED = 2;

    private CameraCaptureActivity.CameraHandler mCameraHandler;
    private TextureMovieEncoder mVideoEncoder;
    private File mOutputFile;

    private FullFrameRect mFullScreen;

    private final float[] mSTMatrix = new float[16];
    private int mTextureId;

    private SurfaceTexture mSurfaceTexture;
    private boolean mRecordingEnabled;
    private int mRecordingStatus;

    // width/height of the incoming camera preview frames
    private boolean mIncomingSizeUpdated;
    private int mIncomingWidth;
    private int mIncomingHeight;

    private int mCurrentFilter;
    private int mNewFilter;

    private Context mContext;
    GLText glText;
    private int width = 100;                           // Updated to the Current Width + Height in onSurfaceChanged()
    private int height = 100;
    private float[] mProjMatrix = new float[16];
    private float[] mVMatrix = new float[16];
    private float[] mVPMatrix = new float[16];


    /**
     * Constructs CameraSurfaceRenderer.
     * <p>
     * @param cameraHandler Handler for communicating with UI thread
     * @param movieEncoder video encoder object
     * @param outputFile output file for encoded video; forwarded to movieEncoder
     */
    public CameraSurfaceRenderer(CameraCaptureActivity.CameraHandler cameraHandler,
            TextureMovieEncoder movieEncoder, File outputFile, Context context) {
        mCameraHandler = cameraHandler;
        mVideoEncoder = movieEncoder;
        mContext = context;

        mOutputFile = outputFile;

        mTextureId = -1;

        mRecordingStatus = -1;
        mRecordingEnabled = false;

        mIncomingSizeUpdated = false;
        mIncomingWidth = mIncomingHeight = -1;

        // We could preserve the old filter mode, but currently not bothering.
        mCurrentFilter = -1;
        mNewFilter = CameraCaptureActivity.FILTER_NONE;
    }

    public void setSurface(Surface surface){
        mVideoEncoder.setmSurface(surface);
    }

    /**
     * Notifies the renderer thread that the activity is pausing.
     * <p>
     * For best results, call this *after* disabling Camera preview.
     */
    public void notifyPausing() {
        if (mSurfaceTexture != null) {
            Log.d(TAG, "renderer pausing -- releasing SurfaceTexture");
            mSurfaceTexture.release();
            mSurfaceTexture = null;
        }
        if (mFullScreen != null) {
            mFullScreen.release(false);     // assume the GLSurfaceView EGL context is about
            mFullScreen = null;             //  to be destroyed
        }
        mIncomingWidth = mIncomingHeight = -1;
    }

    /**
     * Notifies the renderer that we want to stop or start recording.
     */
    public void changeRecordingState(boolean isRecording) {
        Log.d(TAG, "changeRecordingState: was " + mRecordingEnabled + " now " + isRecording);
        mRecordingEnabled = isRecording;
    }

    /**
     * Changes the filter that we're applying to the camera preview.
     */
    public void changeFilterMode(int filter) {
        mNewFilter = filter;
    }

    /**
     * Updates the filter program.
     */
    public void updateFilter() {
        Texture2dProgram.ProgramType programType;
        float[] kernel = null;
        float colorAdj = 0.0f;

        Log.d(TAG, "Updating filter to " + mNewFilter);
        switch (mNewFilter) {
            case CameraCaptureActivity.FILTER_NONE:
                programType = Texture2dProgram.ProgramType.TEXTURE_EXT;
                break;
            case CameraCaptureActivity.FILTER_BLACK_WHITE:
                // (In a previous version the TEXTURE_EXT_BW variant was enabled by a flag called
                // ROSE_COLORED_GLASSES, because the shader set the red channel to the B&W color
                // and green/blue to zero.)
                programType = Texture2dProgram.ProgramType.TEXTURE_EXT_BW;
                break;
            case CameraCaptureActivity.FILTER_BLUR:
                programType = Texture2dProgram.ProgramType.TEXTURE_EXT_FILT;
                kernel = new float[] {
                        1f/16f, 2f/16f, 1f/16f,
                        2f/16f, 4f/16f, 2f/16f,
                        1f/16f, 2f/16f, 1f/16f };
                break;
            case CameraCaptureActivity.FILTER_SHARPEN:
                programType = Texture2dProgram.ProgramType.TEXTURE_EXT_FILT;
                kernel = new float[] {
                        0f, -1f, 0f,
                        -1f, 5f, -1f,
                        0f, -1f, 0f };
                break;
            case CameraCaptureActivity.FILTER_EDGE_DETECT:
                programType = Texture2dProgram.ProgramType.TEXTURE_EXT_FILT;
                kernel = new float[] {
                        -1f, -1f, -1f,
                        -1f, 8f, -1f,
                        -1f, -1f, -1f };
                break;
            case CameraCaptureActivity.FILTER_EMBOSS:
                programType = Texture2dProgram.ProgramType.TEXTURE_EXT_FILT;
                kernel = new float[] {
                        2f, 0f, 0f,
                        0f, -1f, 0f,
                        0f, 0f, -1f };
                colorAdj = 0.5f;
                break;
            default:
                throw new RuntimeException("Unknown filter mode " + mNewFilter);
        }

        // Do we need a whole new program?  (We want to avoid doing this if we don't have
        // too -- compiling a program could be expensive.)
        if (programType != mFullScreen.getProgram().getProgramType()) {
            mFullScreen.changeProgram(new Texture2dProgram(programType));
            // If we created a new program, we need to initialize the texture width/height.
            mIncomingSizeUpdated = true;
        }

        // Update the filter kernel (if any).
        if (kernel != null) {
            mFullScreen.getProgram().setKernel(kernel, colorAdj);
        }

        mCurrentFilter = mNewFilter;
    }

    /**
     * Records the size of the incoming camera preview frames.
     * <p>
     * It's not clear whether this is guaranteed to execute before or after onSurfaceCreated(),
     * so we assume it could go either way.  (Fortunately they both run on the same thread,
     * so we at least know that they won't execute concurrently.)
     */
    public void setCameraPreviewSize(int width, int height) {
        Log.d(TAG, "setCameraPreviewSize");
        mIncomingWidth = width;
        mIncomingHeight = height;
        mIncomingSizeUpdated = true;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        Log.d(TAG, "onSurfaceCreated");
        glText = new GLText(mContext.getAssets());
        mVideoEncoder.setGlText(new GLText(mContext.getAssets()));
        glText.load("Roboto-Regular.ttf", TEXT_SIZE, 2, 2 );  // Create Font (Height: 14 Pixels / X+Y Padding 2 Pixels)

        // enable texture + alpha blending
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        // We're starting up or coming back.  Either way we've got a new EGLContext that will
        // need to be shared with the video encoder, so figure out if a recording is already
        // in progress.
        mRecordingEnabled = mVideoEncoder.isRecording();
        if (mRecordingEnabled) {
            mRecordingStatus = RECORDING_RESUMED;
        } else {
            mRecordingStatus = RECORDING_OFF;
        }

        // Set up the texture blitter that will be used for on-screen display.  This
        // is *not* applied to the recording, because that uses a separate shader.
        mFullScreen = new FullFrameRect(
                new Texture2dProgram(Texture2dProgram.ProgramType.TEXTURE_EXT));

        mTextureId = mFullScreen.createTextureObject();

        // Create a SurfaceTexture, with an external texture, in this EGL context.  We don't
        // have a Looper in this thread -- GLSurfaceView doesn't create one -- so the frame
        // available messages will arrive on the main thread.
        mSurfaceTexture = new SurfaceTexture(mTextureId);

        // Tell the UI thread to enable the camera preview.
        mCameraHandler.sendMessage(mCameraHandler.obtainMessage(
                CameraCaptureActivity.CameraHandler.MSG_SET_SURFACE_TEXTURE, mSurfaceTexture));
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        Log.d(TAG, "onSurfaceChanged " + width + "x" + height);
        float ratio = (float) width / height;

        // Take into account device orientation
        if (width > height) {
            Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 1, 10);
        }
        else {
            Matrix.frustumM(mProjMatrix, 0, -1, 1, -1/ratio, 1/ratio, 1, 10);
        }

        // Save width and height
        this.width = width;                             // Save Current Width
        this.height = height;                           // Save Current Height

        int useForOrtho = Math.min(width, height);

        //TODO: Is this wrong?
        Matrix.orthoM(mVMatrix, 0,
                -useForOrtho/2,
                useForOrtho/2,
                -useForOrtho/2,
                useForOrtho/2, 0.1f, 100f);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        if (VERBOSE) Log.d(TAG, "onDrawFrame tex=" + mTextureId);
        boolean showBox = false;

        // Latch the latest frame.  If there isn't anything new, we'll just re-use whatever
        // was there before.
        mSurfaceTexture.updateTexImage();

        // If the recording state is changing, take care of it here.  Ideally we wouldn't
        // be doing all this in onDrawFrame(), but the EGLContext sharing with GLSurfaceView
        // makes it hard to do elsewhere.
        if (mRecordingEnabled) {
            switch (mRecordingStatus) {
                case RECORDING_OFF:
                    Log.d(TAG, "START recording");
                    // start recording
                    mRecordingStatus = RECORDING_ON;
                    mVideoEncoder.startRecording(new TextureMovieEncoder.EncoderConfig(
                            mOutputFile, 640, 480, 1000000, EGL14.eglGetCurrentContext()));
                    break;
                case RECORDING_RESUMED:
                    Log.d(TAG, "RESUME recording");
                    mVideoEncoder.updateSharedContext(EGL14.eglGetCurrentContext());
                    mRecordingStatus = RECORDING_ON;
                    break;
                case RECORDING_ON:
                    // yay
                    break;
                default:
                    throw new RuntimeException("unknown status " + mRecordingStatus);
            }
        } else {
            switch (mRecordingStatus) {
                case RECORDING_ON:
                case RECORDING_RESUMED:
                    // stop recording
                    Log.d(TAG, "STOP recording");
                    mVideoEncoder.stopRecording();
                    mRecordingStatus = RECORDING_OFF;
                    break;
                case RECORDING_OFF:
                    // yay
                    break;
                default:
                    throw new RuntimeException("unknown status " + mRecordingStatus);
            }
        }

        // Set the video encoder's texture name.  We only need to do this once, but in the
        // current implementation it has to happen after the video encoder is started, so
        // we just do it here.
        //
        // TODO: be less lame.
        mVideoEncoder.setTextureId(mTextureId);

        // Tell the video encoder thread that a new frame is available.
        // This will be ignored if we're not actually recording.
        mVideoEncoder.frameAvailable(mSurfaceTexture);

        if (mIncomingWidth <= 0 || mIncomingHeight <= 0) {
            // Texture size isn't set yet.  This is only used for the filters, but to be
            // safe we can just skip drawing while we wait for the various races to resolve.
            // (This seems to happen if you toggle the screen off/on with power button.)
            Log.i(TAG, "Drawing before incoming texture size set; skipping");
            return;
        }
        // Update the filter, if necessary.
        if (mCurrentFilter != mNewFilter) {
            updateFilter();
        }
        if (mIncomingSizeUpdated) {
            mFullScreen.getProgram().setTexSize(mIncomingWidth, mIncomingHeight);
            mIncomingSizeUpdated = false;
        }

        // Draw the video frame.
        mSurfaceTexture.getTransformMatrix(mSTMatrix);
        mFullScreen.drawFrame(mTextureId, mSTMatrix);

        // Draw a flashing box if we're recording.  This only appears on screen.
        /*showBox = (mRecordingStatus == RECORDING_ON);
        if (showBox && (++mFrameCount & 0x04) == 0) {
            drawScore();
        }*/
        drawChrono();
        drawScore();
    }

    /**
     * Draws a red box in the corner.
     */
    private void drawScore() {
        /*GLES20.glEnable(GLES20.GL_SCISSOR_TEST);
        GLES20.glScissor(0, 0, 100, 100);
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glDisable(GLES20.GL_SCISSOR_TEST);*/


        //GLES20.glClear( GL10.GL_COLOR_BUFFER_BIT );

        String overlay = CameraCaptureActivity.getGameModel().getTeamScores();

        Matrix.multiplyMM(mVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);

        glText.begin( 1.0f, 1.0f, 1.0f, 1.0f, mVPMatrix );         // Begin Text Rendering (Set Color BLUE)
        glText.draw( overlay, -width/2 + TEXT_SIZE *2, height/2 - TEXT_SIZE * 2 );        // int are position
        glText.end();
    }

    private void drawChrono() {

        StringBuilder chronoTime = new StringBuilder();
        String sequence = CameraCaptureActivity.getFragment().getSportSequence();
        if (sequence != null) {
            chronoTime.append(sequence);
            chronoTime.append(" : ");
        }
        chronoTime.append(CameraCaptureActivity.getFragment().getChronoTime());

        Matrix.multiplyMM(mVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);

        glText.begin( 1.0f, 1.0f, 1.0f, 1.0f, mVPMatrix );         // Begin Text Rendering (Set Color BLUE)
        glText.draw( chronoTime.toString(), width/3, height/2 - TEXT_SIZE * 2);        // int are position
        glText.end();
    }

}
