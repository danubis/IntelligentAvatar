package danubis.derrick.library.Body;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class TransparentBody extends GLSurfaceView implements MediaPlayer.OnCompletionListener {

    private static final String LOGTAG = "VideoGLSurfaceView";

    public static int ON_HELLO_SPEAK_START = 5100;
    public static int ON_SPEAK_START = 10000;
    public static int ON_SPEAK_END = 41000;
    public static int ON_IDLE_1 = -1;
    public static int ON_IDLE_2 = -1;
    public static int ON_IDLE_3 = -1;

    VideoRender mRenderer;
    private MediaPlayer mMediaPlayer = null;
    private int pausedPosition = -1;
    private boolean prepared = false;


    public TransparentBody(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);

        setZOrderOnTop(true);
    }


    public void setVideoPath(String path) {
        mRenderer = new VideoRender(path);
        setRenderer(mRenderer);
    }


    public void start() {
        mMediaPlayer.start();
    }


    public void pause() {
        Log.e(LOGTAG, "paused...");
        super.onPause();
        mMediaPlayer.pause();
        pausedPosition = mMediaPlayer.getCurrentPosition();
    }


    public void resume() {
        super.onResume();
        if (pausedPosition != -1 && prepared) {
            mMediaPlayer.seekTo(pausedPosition);
            start();
        }
    }


    public void destroy() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }


    public void doSpeakingAction() {
        doAction(ON_SPEAK_START);
    }


    public void doWaitingAction() {
        doAction(ON_SPEAK_END);
    }


    public void doIdleAction() {

        ArrayList<Integer> idleActions = new ArrayList<>();

        if (ON_IDLE_1 != -1) {
            idleActions.add(ON_IDLE_1);
        }

        if (ON_IDLE_2 != -1) {
            idleActions.add(ON_IDLE_2);
        }

        if (ON_IDLE_3 != -1) {
            idleActions.add(ON_IDLE_3);
        }

        if (idleActions.isEmpty()) {
            doWaitingAction();
        } else {
            Random random = new Random();
            int idleAction = random.nextInt(idleActions.size());
            doAction(idleActions.get(idleAction));
        }
    }


    private void doAction(int msec) {
        mMediaPlayer.pause();
        mMediaPlayer.seekTo(msec);
        mMediaPlayer.start();
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.pause();
        mp.seekTo(ON_SPEAK_END);
        mp.start();
    }


    private class VideoRender implements Renderer {

        private static final int FLOAT_SIZE_BYTES = 4;
        private static final int TRIANGLE_VERTICES_DATA_STRIDE_BYTES = 3 * FLOAT_SIZE_BYTES;
        private static final int TEXTURE_VERTICES_DATA_STRIDE_BYTES = 2 * FLOAT_SIZE_BYTES;
        private static final int TRIANGLE_VERTICES_DATA_POS_OFFSET = 0;
        private static final int TRIANGLE_VERTICES_DATA_UV_OFFSET = 0;
        private final float[] mTriangleVerticesData = {-1.0f, -1.0f, 0, 1.0f,
                -1.0f, 0, -1.0f, 1.0f, 0, 1.0f, 1.0f, 0,};

        private final float[] mTextureVerticesData = {0.f, 0.0f, 1.0f, 0.f,
                0.0f, 1.f, 1.0f, 1.0f};

        private FloatBuffer mTriangleVertices;

        // extra
        private FloatBuffer mTextureVertices;

        private float[] mMVPMatrix = new float[16];
        private float[] mSTMatrix = new float[16];
        private float[] projectionMatrix = new float[16];

        private int mProgram;
        private int mTextureID;
        private int muMVPMatrixHandle;
        private int muSTMatrixHandle;
        private int maPositionHandle;
        private int maTextureHandle;

        private SurfaceTexture mSurface;
        private int GL_TEXTURE_EXTERNAL_OES = 0x8D65;


        VideoRender(String path) {
            mTriangleVertices = ByteBuffer
                    .allocateDirect(mTriangleVerticesData.length * FLOAT_SIZE_BYTES)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer();
            mTriangleVertices.put(mTriangleVerticesData).position(0);

            // extra
            mTextureVertices = ByteBuffer
                    .allocateDirect(
                            mTextureVerticesData.length * FLOAT_SIZE_BYTES)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer();
            mTextureVertices.put(mTextureVerticesData).position(0);

            Matrix.setIdentityM(mSTMatrix, 0);

            mMediaPlayer = new MediaPlayer();
            try {
                mMediaPlayer.setDataSource(path);
                mMediaPlayer.setOnPreparedListener(onPreparedListener);
                mMediaPlayer.setOnCompletionListener(TransparentBody.this);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public void onDrawFrame(GL10 glUnused) {

            mSurface.updateTexImage();
            mSurface.getTransformMatrix(mSTMatrix);

            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
            GLES20.glUseProgram(mProgram);

            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, mTextureID);

            mTriangleVertices.position(TRIANGLE_VERTICES_DATA_POS_OFFSET);

            GLES20.glVertexAttribPointer(maPositionHandle, 3, GLES20.GL_FLOAT,
                    false, TRIANGLE_VERTICES_DATA_STRIDE_BYTES,
                    mTriangleVertices);

            GLES20.glEnableVertexAttribArray(maPositionHandle);

            mTextureVertices.position(TRIANGLE_VERTICES_DATA_UV_OFFSET);

            GLES20.glVertexAttribPointer(maTextureHandle, 2, GLES20.GL_FLOAT,
                    false, TEXTURE_VERTICES_DATA_STRIDE_BYTES, mTextureVertices);

            GLES20.glEnableVertexAttribArray(maTextureHandle);

            Matrix.setIdentityM(mMVPMatrix, 0);

            GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mMVPMatrix, 0);
            GLES20.glUniformMatrix4fv(muSTMatrixHandle, 1, false, mSTMatrix, 0);

            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

            GLES20.glFinish();
        }


        public void onSurfaceChanged(GL10 glUnused, int width, int height) {

            GLES20.glViewport(0, 0, width, height);
            Matrix.frustumM(projectionMatrix, 0, -1.0f, 1.0f, -1.0f, 1.0f,
                    1.0f, 10.0f);
        }


        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {

            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

            mProgram = createProgram(VideoShaders.VERTEX_SHADER, VideoShaders.FRAGMENT_SHADER);
            if (mProgram == 0) {
                return;
            }

            maPositionHandle = GLES20
                    .glGetAttribLocation(mProgram, "aPosition");

            if (maPositionHandle == -1) {
                throw new RuntimeException(
                        "Could not get attrib location for aPosition");
            }

            maTextureHandle = GLES20.glGetAttribLocation(mProgram,
                    "aTextureCoord");
            if (maTextureHandle == -1) {
                throw new RuntimeException(
                        "Could not get attrib location for aTextureCoord");
            }

            muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram,
                    "uMVPMatrix");
            if (muMVPMatrixHandle == -1) {
                throw new RuntimeException(
                        "Could not get attrib location for uMVPMatrix");
            }

            muSTMatrixHandle = GLES20.glGetUniformLocation(mProgram,
                    "uSTMatrix");
            if (muSTMatrixHandle == -1) {
                throw new RuntimeException(
                        "Could not get attrib location for uSTMatrix");
            }

            int[] textures = new int[1];
            GLES20.glGenTextures(1, textures, 0);

            mTextureID = textures[0];
            GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, mTextureID);
            checkGlError("glBindTexture mTextureID");

            GLES20.glTexParameterf(GL_TEXTURE_EXTERNAL_OES,
                    GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameterf(GL_TEXTURE_EXTERNAL_OES,
                    GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

            mSurface = new SurfaceTexture(mTextureID);

            mMediaPlayer.setSurface(new Surface(mSurface));
            if (!prepared) {
                mMediaPlayer.prepareAsync();
            }
        }


        private int loadShader(int shaderType, String source) {
            int shader = GLES20.glCreateShader(shaderType);
            if (shader != 0) {
                GLES20.glShaderSource(shader, source);
                GLES20.glCompileShader(shader);
                int[] compiled = new int[1];
                GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS,
                        compiled, 0);
                if (compiled[0] == 0) {
                    GLES20.glDeleteShader(shader);
                    shader = 0;
                }
            }
            return shader;
        }


        private int createProgram(String vertexSource, String fragmentSource) {
            int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
            if (vertexShader == 0) {
                return 0;
            }
            int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER,
                    fragmentSource);
            if (pixelShader == 0) {
                return 0;
            }

            int program = GLES20.glCreateProgram();
            if (program != 0) {
                GLES20.glAttachShader(program, vertexShader);
                checkGlError("glAttachShader");
                GLES20.glAttachShader(program, pixelShader);
                checkGlError("glAttachShader");
                GLES20.glLinkProgram(program);
                int[] linkStatus = new int[1];
                GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS,
                        linkStatus, 0);
                if (linkStatus[0] != GLES20.GL_TRUE) {

                    GLES20.glDeleteProgram(program);
                    program = 0;
                }
            }
            return program;
        }


        private void checkGlError(String op) {
            int error;
            while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
                throw new RuntimeException(op + ": glError " + error);
            }
        }


        MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                prepared = true;
                mp.seekTo(ON_HELLO_SPEAK_START);
                mp.start();
            }
        };
    }
}
