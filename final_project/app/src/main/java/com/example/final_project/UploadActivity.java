package com.example.final_project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.CamcorderProfile;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project.model.UploadResponse;
import com.example.final_project.model.Util;
import com.example.final_project.retrofitAPI.PostApi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;
    private ImageButton mShootButton;
    private ImageButton mExitButton;
    private Button mSubmitButton;
    private TextView mTimeText;

    private MediaRecorder mMediaRecorder;
    private Camera camera;
    private MediaPlayer mediaPlayer;
    private PostApi postApi;

    private String studentId;
    private String userName;
    private boolean isShoot = false;
    private int timeCount = 0;
    private String path;
    private final Handler handler = new Handler ();
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isShoot) {
                timeCount++;
                mTimeText.setText(showTimeCount(timeCount));
                handler.postDelayed(this, 1000);
            }
        }
    };
    private final Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {}
    };
    private static final String TAG = "inform";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        init ();
        mShootButton.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShoot) {
                    if (mMediaRecorder == null) {
                        mMediaRecorder = new MediaRecorder();
                    }
                    try {
                        camera.unlock ();
                        mMediaRecorder.setCamera (camera);
                        mMediaRecorder.setAudioSource (MediaRecorder.AudioSource.CAMCORDER);
                        mMediaRecorder.setVideoSource (MediaRecorder.VideoSource.CAMERA);

                        mMediaRecorder.setProfile (CamcorderProfile.get (CamcorderProfile.QUALITY_720P));
                        mMediaRecorder.setOrientationHint (90);
                        mMediaRecorder.setPreviewDisplay (mHolder.getSurface());

                        getPath();
                        mMediaRecorder.setOutputFile (path);
                        mMediaRecorder.prepare ();
                        mMediaRecorder.start ();
                        isShoot = true;
                        runnable.run ();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, e.toString());
                    }
                } else {
                    try {
                        mMediaRecorder.stop ();
                        mMediaRecorder.reset ();
                        mMediaRecorder.release ();
                        mMediaRecorder = null;
                        if (camera != null) {
                            camera.lock ();
                            camera.stopPreview ();
                            camera.release ();
                            camera = null;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    isShoot = false;

                    mShootButton.setVisibility (View.GONE);
                    mTimeText.setVisibility (View.GONE);
                    mSubmitButton.setVisibility (View.VISIBLE);
                    if (mediaPlayer == null) {
                        mediaPlayer = new MediaPlayer ();
                    }
                    mediaPlayer.reset ();
                    mediaPlayer = MediaPlayer.create (UploadActivity.this, Uri.parse (path));
                    mediaPlayer.setAudioStreamType (AudioManager.STREAM_MUSIC);
                    mediaPlayer.setDisplay (mHolder);
                    try {
                        mediaPlayer.prepare ();
                    } catch (Exception e) {
                        e.printStackTrace ();
                    }
                    mediaPlayer.start ();
                    mediaPlayer.setLooping (true);
                }
            }
        });

        mSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (camera != null) {
                    int areaX = (int) (event.getX() / mSurfaceView.getWidth() * 2000) - 1000;
                    int areaY = (int) (event.getY() / mSurfaceView.getWidth() * 2000) - 1000;

                    Rect focusArea = new Rect();
                    focusArea.left = Math.max(areaX - 100, -1000);
                    focusArea.top = Math.max(areaY - 100, -1000);
                    focusArea.right = Math.min(areaX + 100, 1000);
                    focusArea.bottom = Math.min(areaY + 100, 1000);

                    Camera.Area cameraArea = new Camera.Area(focusArea, 1000);
                    List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();
                    List<Camera.Area> focusAreas = new ArrayList<Camera.Area>();
                    Camera.Parameters mParameters = camera.getParameters();
                    if (mParameters.getMaxNumMeteringAreas() > 0) {
                        meteringAreas.add(cameraArea);
                        focusAreas.add(cameraArea);
                    }
                    mParameters.setFocusAreas(focusAreas);
                    mParameters.setMeteringAreas(meteringAreas);
                    try {
                        camera.cancelAutoFocus();
                        camera.setParameters(mParameters);
                        camera.autoFocus(autoFocusCallback);
                    } catch (Exception e) {
                    }
                }
                return false;
            }
        });

        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    @Override
    protected void onDestroy (){
        super.onDestroy();
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

    private void init () {
        mSurfaceView = (SurfaceView) findViewById (R.id.camera_view);
        mShootButton = (ImageButton) findViewById (R.id.operate_button);
        mExitButton = (ImageButton) findViewById (R.id.exit_button);
        mTimeText = (TextView) findViewById (R.id.time_text);
        mSubmitButton = (Button) findViewById (R.id.submit_button);

        mSubmitButton.setVisibility (View.INVISIBLE);

        camera = Camera.open();
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFocusMode (Camera.Parameters.FOCUS_MODE_AUTO);
        camera.setParameters (parameters);
        if (camera != null) {
            camera.setDisplayOrientation (90);
        }

        SurfaceHolder holder = mSurfaceView.getHolder ();
        holder.setType (SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback (this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api-sjtu-camp-2021.bytedance.com/homework/invoke/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        postApi = retrofit.create (PostApi.class);

        studentId = getIntent ().getStringExtra ("studentId");
        userName = getIntent ().getStringExtra ("userName");
    }

    private void submit() {
        MediaMetadataRetriever media = new MediaMetadataRetriever ();
        media.setDataSource (path);
        Bitmap bitmap = media.getFrameAtTime ();
        ByteArrayOutputStream baos = new ByteArrayOutputStream ();
        bitmap.compress (Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageData = baos.toByteArray ();
        byte[] videoData = readDataFromUri (Uri.fromFile (new File (path)));
        MultipartBody.Part image_part = MultipartBody.Part.createFormData ("cover_image", "myImage.jpg",
                RequestBody.create(MediaType.parse("multipart/form-data"), imageData));
        MultipartBody.Part video_part = MultipartBody.Part.createFormData ("video", "myVideo.mp4",
                RequestBody.create(MediaType.parse("multipart/form-data"), videoData));
        Call<UploadResponse> call = postApi.submitVideo (studentId, userName, "",
                image_part, video_part, "U0pUVS1ieXRlZGFuY2UtYW5kcm9pZA==");

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    Response<UploadResponse> response = call.execute();
                    if (response.isSuccessful()) {
                        if (response.body().success) {
                            finish();
                        }else {
                            Toast.makeText (getApplicationContext(), "上传失败", Toast.LENGTH_SHORT).show ();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d (TAG, e.toString ());
                    Toast.makeText (getApplicationContext(), "文件过大", Toast.LENGTH_SHORT).show ();
                }
                Looper.loop();
            }
        }).start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.mHolder = holder;
        try {
            camera.setPreviewDisplay (holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview ();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int i, int i1, int i2) {
        mHolder = holder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (camera != null) {
            camera.stopPreview ();
            camera.release ();
            camera = null;
        }
    }

    private void getPath () {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format (new Date());

        File storageDir = getExternalFilesDir (Environment.DIRECTORY_PICTURES);
        File media = new File(storageDir, "MEDIA_" + timeStamp + ".mp4");

        path = media.getAbsolutePath();
    }

    public String showTimeCount(int time) {
        String timeStr = "";
        int minute = time / 60;
        int second = time % 60;
        if (minute < 10) {
            timeStr += "0" + String.valueOf (minute);
        }else {
            timeStr += String.valueOf (minute);
        }
        timeStr += ":";
        if (second < 10) {
            timeStr += "0" + String.valueOf (second);
        }else {
            timeStr += String.valueOf (second);
        }

        return timeStr;
    }

    private byte[] readDataFromUri(Uri uri) {
        byte[] data = null;
        try {
            InputStream is = getContentResolver().openInputStream(uri);
            data = Util.inputStream2bytes(is);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
