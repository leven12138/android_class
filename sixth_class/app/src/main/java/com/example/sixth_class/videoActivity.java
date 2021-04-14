package com.example.sixth_class;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class videoActivity extends AppCompatActivity {
    private VideoView mVideoView;
    private Button mButtonPlay;
    private Button mButtonPause;
    private SeekBar mProgressBar;
    private TextView mProgressText;

    public static final int UPDATE_VIDEO_NUM = 1;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_video);

        mVideoView = (VideoView) findViewById (R.id.video_view);
        mButtonPlay = (Button) findViewById (R.id.play_button);
        mButtonPause = (Button) findViewById (R.id.pause_button);
        mProgressBar = (SeekBar) findViewById (R.id.progress_bar);
        mProgressText = (TextView) findViewById (R.id.progress_text);

        mButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoView.start ();
                handler.sendEmptyMessage (UPDATE_VIDEO_NUM);
            }
        });
        mButtonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoView.pause ();
                handler.removeMessages (UPDATE_VIDEO_NUM);
            }
        });

        mVideoView.getHolder ().setFormat (PixelFormat.TRANSPARENT);
        mVideoView.setZOrderOnTop (true);
        mVideoView.setVideoPath (getVideoPath (R.raw.big_buck_bunny));

        mProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateTime (progress, mVideoView.getDuration ());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeMessages (UPDATE_VIDEO_NUM);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int newTime = mProgressBar.getProgress ();

                mVideoView.seekTo (newTime);
                handler.sendEmptyMessage (UPDATE_VIDEO_NUM);
            }
        });
    }

    private Handler handler = new Handler () {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage (msg);
            if (msg.what == UPDATE_VIDEO_NUM) {
                int currentTime = mVideoView.getCurrentPosition ();
                int totalTime = mVideoView.getDuration ();

                updateTime (currentTime, totalTime);

                mProgressBar.setMax (totalTime);
                mProgressBar.setProgress (currentTime);

                handler.sendEmptyMessageDelayed (UPDATE_VIDEO_NUM, 500);
            }
        }
    };

    private void updateTime (int current_millisecond, int total_millisecond) {
        int tmp = current_millisecond / 1000;
        int hour = tmp / 3600;
        int minute = tmp % 3600 / 60;
        int second = tmp % 60;

        String time = null;
        if (hour != 0) {
            time = String.format("%02d:%02d:%02d", hour, minute, second);
        } else {
            time = String.format("%02d:%02d", minute, second);
        }
        time += "/";

        tmp = total_millisecond / 1000;
        hour = tmp / 3600;
        minute = tmp % 3600 / 60;
        second = tmp % 60;
        if (hour != 0) {
            time += String.format("%02d:%02d:%02d", hour, minute, second);
        } else {
            time += String.format("%02d:%02d", minute, second);
        }

        mProgressText.setText (time);
    }

    private String getVideoPath (int resId) {
        return "android.resource://" + this.getPackageName() + "/" + resId;
    }
}
