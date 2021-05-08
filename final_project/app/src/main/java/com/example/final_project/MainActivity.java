package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.final_project.model.Share;
import com.example.final_project.myFragment.HomePage;
import com.example.final_project.myFragment.MainPage;
import com.example.final_project.myFragment.VideoPage;
import com.example.final_project.view.VideoOperator;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements VideoPage.SearchUserOp {
    private ViewPager mPager;
    private FrameLayout video_frag;
    private VideoPage videoPage = null;
    private ImageButton toUploadAct;
    private TextView mainText;
    private TextView homeText;

    private VideoOperator videoOperator;
    private MainPage mainPage;
    private HomePage homePage;

    private String studentId = null;
    private String userName = null;
    private static final int PAGE_COUNT = 2;
    private final static int PERMISSION_REQUEST_CODE = 101;
    private final static int LOG_ACTIVITY = 110;
    private final static int LOG_SUCCESS_RESPONSE = 111;
    private final static int UPLOAD_ACTIVITY = 220;
    private final static String TAG = "inform";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPager = (ViewPager) findViewById (R.id.page_view);
        video_frag = (FrameLayout) findViewById (R.id.video_container);
        toUploadAct = (ImageButton) findViewById (R.id.upload_act);
        mainText = (TextView) findViewById (R.id.main_tag);
        homeText = (TextView) findViewById (R.id.home_tag);

        mainText.setTextColor(Color.WHITE);
        homeText.setTextColor(Color.parseColor("#c1c1c1"));

        videoOperator = new VideoOperator() {
            @Override
            public void setVideoView (Share share) {
                MainActivity.this.setVideoView (share);
            }

            @Override
            public void quitVideoView () {
                MainActivity.this.quitVideoView ();
            }
        };

        video_frag.setVisibility (View.GONE);
        mainPage = new MainPage (videoOperator);
        homePage = new HomePage (videoOperator);
        mPager.setAdapter(new FragmentPagerAdapter (getSupportFragmentManager ()) {
            @NonNull
            @Override
            public Fragment getItem (int position) {
                switch (position) {
                    case 0:
                        return mainPage;
                    default:
                        return homePage;
                }
            }

            @Override
            public int getCount() {
                return PAGE_COUNT;
            }

            @Override
            public CharSequence getPageTitle (int position) {
                return "page " + (position + 1);
            }
        });

        mPager.clearOnPageChangeListeners ();
        mPager.addOnPageChangeListener (new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mainText.setTextColor(Color.WHITE);
                        homeText.setTextColor(Color.parseColor("#c1c1c1"));
                        break;

                    case 1:
                        if (studentId == null) {
                            startActivityForResult (new Intent (MainActivity.this, LogActivity.class), LOG_ACTIVITY);
                        }
                        mainText.setTextColor(Color.parseColor("#c1c1c1"));
                        homeText.setTextColor(Color.WHITE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        mainText.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPager.getCurrentItem() == 1) {
                    mPager.setCurrentItem (0);
                }
            }
        });

        homeText.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPager.getCurrentItem() == 0) {
                    mPager.setCurrentItem (1);
                }
            }
        });

        toUploadAct.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean camera_per = ActivityCompat.checkSelfPermission (MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
                boolean record_per = ActivityCompat.checkSelfPermission (MainActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
                if (!camera_per || !record_per){
                    List<String> permission = new ArrayList<String>();
                    if (!camera_per) {
                        permission.add(Manifest.permission.CAMERA);
                    }
                    if (!record_per) {
                        permission.add(Manifest.permission.RECORD_AUDIO);
                    }
                    ActivityCompat.requestPermissions(MainActivity.this, permission.toArray(new String[permission.size()]), PERMISSION_REQUEST_CODE);
                }else {
                    if (studentId != null && userName != null) {
                        Intent intent = new Intent (MainActivity.this, UploadActivity.class);
                        intent.putExtra ("studentId", studentId);
                        intent.putExtra ("userName", userName);
                        startActivityForResult (intent, UPLOAD_ACTIVITY);
                    }else {
                        Toast.makeText(MainActivity.this, "尚未登录", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void setVideoView (Share share) {
        if (videoPage == null) {
            videoPage = new VideoPage (share, videoOperator);
        }else {
            videoPage.notifyData (share);
        }

        mPager.setVisibility (View.GONE);
        video_frag.setVisibility (View.VISIBLE);
        getSupportFragmentManager ()
                .beginTransaction ()
                .add(R.id.video_container, videoPage)
                .commit ();
    }

    private void quitVideoView () {
        getSupportFragmentManager ()
                .beginTransaction ()
                .remove (videoPage)
                .commit ();
        video_frag.setVisibility (View.GONE);
        mPager.setVisibility (View.VISIBLE);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        switch (requestCode) {
            case LOG_ACTIVITY:
                if (resultCode == LOG_SUCCESS_RESPONSE) {
                    this.studentId = data.getStringExtra("studentId");
                    this.userName = data.getStringExtra("userName");
                    homePage.setIdentity(studentId, userName);
                }
                break;
            case UPLOAD_ACTIVITY:
                homePage.updateView();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult (requestCode, permissions, grantResults);

        boolean hasPermission = true;
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                hasPermission = false;
                break;
            }
        }
        if (hasPermission) {
            Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
            if (studentId != null && userName != null) {
                Intent intent = new Intent (MainActivity.this, UploadActivity.class);
                intent.putExtra ("studentId", studentId);
                intent.putExtra ("userName", userName);
                startActivityForResult (intent, UPLOAD_ACTIVITY);
            }else {
                Toast.makeText(MainActivity.this, "尚未登录", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "权限获取失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void searchUser (String userId) {
        mainPage.updateData (userId);
    }
}