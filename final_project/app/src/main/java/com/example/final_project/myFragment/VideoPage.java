package com.example.final_project.myFragment;

import android.content.Context;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.final_project.R;
import com.example.final_project.model.Share;
import com.example.final_project.view.VideoOperator;
import com.facebook.drawee.backends.pipeline.Fresco;

public class VideoPage extends Fragment {
    private View view;

    private VideoView mVideoView;
    private ImageButton mQuitButton;
    private TextView mNameText;
    private SeekBar mProgressBar;
    private TextView mProgressText;

    private String tName;
    private String tId;
    private String videoUrl;
    private final VideoOperator videoOperator;
    private SearchUserOp searchUserOp;
    public static final int UPDATE_VIDEO_NUM = 1;
    private final static String TAG = "inform";

    public VideoPage (Share share, VideoOperator videoOperator) {
        this.tId = share.getStudentId();
        this.tName = share.getUserName();
        this.videoUrl = share.getVideoUrl();
        this.videoOperator = videoOperator;
    }

    @Override
    public void onAttach (Context context) {
        super.onAttach (context);
        if (context instanceof SearchUserOp) {
            searchUserOp = (SearchUserOp) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater,
                              @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        Fresco.initialize(getActivity());
        view = inflater.inflate(R.layout.frag_video, container, false);

        initVideo ();

        mQuitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoOperator.quitVideoView ();
            }
        });

        mNameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchUserOp.searchUser (tId);
                videoOperator.quitVideoView ();
            }
        });

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

        mVideoView.setOnPreparedListener (new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (!mp.isPlaying ()) {
                    mp.start ();
                    mp.setLooping (true);
                    handler.sendEmptyMessage (UPDATE_VIDEO_NUM);
                }
            }
        });

        mVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVideoView.isPlaying ()) {
                    mVideoView.pause ();
                    handler.removeMessages (UPDATE_VIDEO_NUM);
                }else {
                    mVideoView.start ();
                    handler.sendEmptyMessage (UPDATE_VIDEO_NUM);
                }
            }
        });
        mVideoView.setVideoURI (Uri.parse (videoUrl));

        return view;
    }

    @Override
    public void onDestroyView (){
        super.onDestroyView ();
        mVideoView.stopPlayback ();
        handler.removeMessages (UPDATE_VIDEO_NUM);
        mVideoView.setOnPreparedListener (null);
        mVideoView.suspend ();
    }

    @Override
    public void onDetach () {
        super.onDetach();
        searchUserOp = null;
    }

    private void initVideo () {
        mVideoView = (VideoView) view.findViewById (R.id.video_view);
        mQuitButton = (ImageButton) view.findViewById (R.id.quit_button);
        mNameText = (TextView) view.findViewById (R.id.owner_name);
        mProgressBar = (SeekBar) view.findViewById (R.id.progress_bar);
        mProgressText = (TextView) view.findViewById (R.id.progress_text);

        mVideoView.getHolder ().setFormat (PixelFormat.TRANSPARENT);
        mVideoView.setZOrderOnTop (true);

        mNameText.setText (tName);
    }

    public void notifyData (Share share) {
        mVideoView.stopPlayback ();
        this.tId = share.getStudentId();
        this.tName = share.getUserName();
        this.videoUrl = share.getVideoUrl ();
        mVideoView.setVideoURI (Uri.parse (videoUrl));
        mNameText.setText (tName);
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

                handler.sendEmptyMessageDelayed (UPDATE_VIDEO_NUM, 200);
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

    public interface SearchUserOp {
        void searchUser (String userId);
    }
}
