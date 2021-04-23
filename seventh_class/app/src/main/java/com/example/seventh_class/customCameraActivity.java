package com.example.seventh_class;

import android.Manifest;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import pub.devrel.easypermissions.EasyPermissions;

public class customCameraActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;
    private Button mShootButton;
    private Button mPlayButton;
    private MediaRecorder mMediaRecorder;
    private Camera camera;
    private MediaPlayer mediaPlayer;

    private boolean isShoot = false;
    private boolean isPlay = false;
    private String path;
    private static final String TAG = "inform";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activity_custom_camera);

        mSurfaceView = (SurfaceView) findViewById (R.id.camera_view);
        mShootButton = (Button) findViewById (R.id.shoot);
        mPlayButton = (Button) findViewById (R.id.play);

        SurfaceHolder holder = mSurfaceView.getHolder ();
        holder.setType (SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback (this);

        mShootButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlay) {
                    if (mediaPlayer != null) {
                        isPlay = false;
                        mediaPlayer.stop ();
                        mediaPlayer.reset ();
                        mediaPlayer.release ();
                        mediaPlayer = null;
                    }
                }

                if (!isShoot) {
                    if (mMediaRecorder == null) {
                        mMediaRecorder = new MediaRecorder();
                    }

                    camera = Camera.open ();
                    Camera.Parameters parameters = camera.getParameters ();
                    parameters.setFocusMode (Camera.Parameters.FOCUS_MODE_AUTO);
                    camera.setParameters (parameters);
                    camera.startPreview ();
                    if (camera != null) {
                        camera.setDisplayOrientation (90);
                        camera.unlock ();
                        mMediaRecorder.setCamera (camera);
                    }

                    try {
                        mMediaRecorder.setAudioSource (MediaRecorder.AudioSource.CAMCORDER);
                        mMediaRecorder.setVideoSource (MediaRecorder.VideoSource.CAMERA);

                        mMediaRecorder.setProfile (CamcorderProfile.get (CamcorderProfile.QUALITY_720P));
                        mMediaRecorder.setOrientationHint (90);
                        mMediaRecorder.setPreviewDisplay (mHolder.getSurface ());

                        getPath ();
                        mMediaRecorder.setOutputFile (path);
                        mMediaRecorder.prepare ();
                        mMediaRecorder.start ();
                        isShoot = true;
                        mShootButton.setText ("结束");
                    } catch (Exception e) {
                        e.printStackTrace ();
                        Log.d (TAG, e.toString());
                    }
                } else {
                    try {
                        mMediaRecorder.stop ();
                        mMediaRecorder.reset ();
                        mMediaRecorder.release ();
                        mMediaRecorder = null;
                        mShootButton.setText ("开始");
                        if (camera != null) {
                            camera.stopPreview ();
                            camera.release ();
                            camera = null;
                        }
                    } catch (Exception e) {
                        e.printStackTrace ();
                    }
                    isShoot = false;
                }
            }
        });

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShoot) {
                    try {
                        mMediaRecorder.stop ();
                        mMediaRecorder.reset ();
                        mMediaRecorder.release ();
                        mMediaRecorder = null;
                        mShootButton.setText ("开始");
                        if (camera != null) {
                            camera.stopPreview ();
                            camera.release ();
                            camera = null;
                        }
                    } catch (Exception e) {
                        e.printStackTrace ();
                    }
                    isShoot = false;
                }

                isPlay = true;
                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer ();
                }
                mediaPlayer.reset ();
                Uri uri = Uri.parse (path);
                mediaPlayer = MediaPlayer.create (customCameraActivity.this, uri);
                mediaPlayer.setAudioStreamType (AudioManager.STREAM_MUSIC);
                mediaPlayer.setDisplay (mHolder);
                try {
                    mediaPlayer.prepare ();
                } catch (Exception e) {
                    e.printStackTrace ();
                }
                mediaPlayer.start ();
            }
        });
    }

    private void getPath () {
        String timeStamp = new SimpleDateFormat ("yyyyMMdd_HHmmss").format (new Date ());

        File storageDir = getExternalFilesDir (Environment.DIRECTORY_PICTURES);
        File media = new File(storageDir, "MEDIA_" + timeStamp + ".mp4");

        path = media.getAbsolutePath();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mHolder = holder;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int i, int i1, int i2) {
        mHolder = holder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mSurfaceView = null;
        mHolder = null;
        if (mMediaRecorder != null) {
            mMediaRecorder.release ();
            mMediaRecorder = null;
        }
        if (camera != null) {
            camera.stopPreview ();
            camera.release ();
            camera = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.release ();
            mediaPlayer = null;
        }
    }
}
