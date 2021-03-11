package com.example.second_class.page;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.second_class.R;

public class pageOne extends Fragment {
    private LottieAnimationView mWaveLottie;
    private SeekBar mSeekBar;
    private CheckBox mCheckBox;
    private static final String TAG = "inform";

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater,
                              @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate (R.layout.page_one, container, false);
        mWaveLottie = (LottieAnimationView) view.findViewById (R.id.pageOne_wave);

        mCheckBox = (CheckBox) view.findViewById (R.id.pageOne_check);
        mSeekBar = (SeekBar) view.findViewById (R.id.pageOne_seek);
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mWaveLottie.playAnimation();
                    mSeekBar.setProgress (100);
                }
                else {
                    mWaveLottie.cancelAnimation ();
                    mSeekBar.setProgress (0);
                }
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener () {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 100) { mCheckBox.setChecked (true); }
                else { mCheckBox.setChecked (false); }
                Log.d (TAG, "进度" + progress);
                mWaveLottie.setProgress (progress/100f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d (TAG, "touch" );
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d (TAG, "end");
            }
        });

        return view;
    }
}
