package com.example.seventh_class;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class sysCameraActivity extends AppCompatActivity {
    private ViewPager mPager = null;
    LayoutInflater layoutInflater = null;
    List<View> pages = new ArrayList<View> ();
    private ImageView mImageView;
    private VideoView mVideoView;
    private Button mPhotoButton;
    private Button mFilmButton;
    private String mCurrentPhotoPath;

    public static final int REQUEST_IMAGE_CAPTURE = 101;
    public static final int REQUEST_VIDEO_CAPTURE = 102;
    private static final String TAG = "inform";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activity_system_camera);

        layoutInflater = getLayoutInflater ();
        mPager = (ViewPager) findViewById (R.id.view_pager);
        mImageView = (ImageView) layoutInflater.inflate (R.layout.image_item, null);
        mVideoView = (VideoView) layoutInflater.inflate (R.layout.video_item, null);
        mPhotoButton = (Button) findViewById (R.id.take_photo);
        mFilmButton = (Button) findViewById (R.id.shoot_film);

        viewAdapter adapter = new viewAdapter ();
        pages.add (mImageView);
        pages.add (mVideoView);
        adapter.setData (pages);
        mPager.setAdapter (adapter);

        mPhotoButton.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePhotoIntent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
                File photoFile = null;
                try {
                    photoFile = createImageFile ();
                } catch (IOException ex) {

                }
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile (sysCameraActivity.this, "com.example.seventh_class.fileprovider", photoFile);
                    takePhotoIntent.putExtra (MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult (takePhotoIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        mFilmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shootFilmIntent = new Intent (MediaStore.ACTION_VIDEO_CAPTURE);
                if (shootFilmIntent.resolveActivity (getPackageManager()) != null) {
                    startActivityForResult (shootFilmIntent, REQUEST_VIDEO_CAPTURE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            int imageW = mImageView.getWidth();
            int imageH = mImageView.getHeight();

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;

            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            int scaleFactor = Math.min(photoW / imageW, photoH / imageH);

            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap imageBitmap = (Bitmap) BitmapFactory.decodeFile (mCurrentPhotoPath, bmOptions);
            mImageView.setImageBitmap (imageBitmap);
        } else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoURI = data.getData ();
            mVideoView.setVideoURI (videoURI);
            mVideoView.start ();
        }
    }

    private File createImageFile () throws IOException {
        String timeStamp = new SimpleDateFormat ("yyyyMMdd_HHmmss").format (new Date ());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = getExternalFilesDir (Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile (imageFileName, ".jpeg", storageDir);

        mCurrentPhotoPath = image.getAbsolutePath ();
        return image;
    }
}
