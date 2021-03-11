package com.example.second_class.page;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.second_class.R;

import java.util.ArrayList;
import java.util.List;

public class pageThree extends Fragment {
    private RecyclerView mUserViewer;
    private LottieAnimationView mWaveLoading;
    private listViewAdapter mAdapter = new listViewAdapter ();
    private static final String TAG = "inform";

    public View onCreateView (@NonNull LayoutInflater inflater,
                              @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        Log.d (TAG, "success");
        View view = inflater.inflate (R.layout.page_three, container, false);

        mUserViewer = (RecyclerView) view.findViewById (R.id.mRecycle);
        mUserViewer.setLayoutManager (new LinearLayoutManager (getActivity ()));
        mUserViewer.setAdapter (mAdapter);
        mWaveLoading = (LottieAnimationView) view.findViewById (R.id.animation_loading);
        mUserViewer.setAlpha (0);

        List<String> items = new ArrayList<> ();
        for (int i = 1; i <= 100; i++) {
            items.add ("用户" + String.valueOf (i));
        }
        mAdapter.notifyItems (items);

        ObjectAnimator view_out = ObjectAnimator.ofFloat (mWaveLoading, "alpha", 1, 0);
        view_out.setDuration (2500);
        view_out.setRepeatCount (0);
        view_out.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mWaveLoading.setVisibility (View.GONE);
            }
        });

        ObjectAnimator view_in = ObjectAnimator.ofFloat (mUserViewer, "alpha", 0, 1);
        view_in.setDuration (2500);
        view_in.setRepeatCount (0);

        AnimatorSet animatorSet = new AnimatorSet ();
        animatorSet.playSequentially (view_out, view_in);
        animatorSet.start ();

        return view;
    }
}
