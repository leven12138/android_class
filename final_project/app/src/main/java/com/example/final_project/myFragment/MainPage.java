package com.example.final_project.myFragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.final_project.MainActivity;
import com.example.final_project.R;
import com.example.final_project.model.DownloadResponse;
import com.example.final_project.model.Share;
import com.example.final_project.retrofitAPI.GetApi;
import com.example.final_project.view.VideoListAdapter;
import com.example.final_project.view.VideoOperator;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainPage extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private View view;
    private GetApi api;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mViewList;
    private GridLayoutManager layoutManager;
    private VideoListAdapter adapter;
    private VideoOperator videoOperator;

    private List<Share> shares;
    private final static String TAG = "inform";

    public MainPage (VideoOperator videoOperator) {
        this.videoOperator = videoOperator;
    }

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater,
                              @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        Fresco.initialize (getActivity ());
        view = inflater.inflate(R.layout.frag_main, container, false);

        initMain ();
        updateData ("");

        return view;
    }

    private void initMain () {
        layoutManager = new GridLayoutManager(getActivity(), 2);
        layoutManager.setOrientation (RecyclerView.VERTICAL);
        adapter = new VideoListAdapter(videoOperator);
        mViewList = view.findViewById (R.id.video_recycle);
        mViewList.setLayoutManager (layoutManager);
        mViewList.setAdapter (adapter);
        mSwipeRefreshLayout = view.findViewById (R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener (this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api-sjtu-camp-2021.bytedance.com/homework/invoke/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create (GetApi.class);
    }

    public void updateData (String studentId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                shares = DownloadFromNet (studentId);
                if (shares != null && !shares.isEmpty()) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyItems (shares);
                        }
                    });
                }
            }
        }).start();
    }

    private List<Share> DownloadFromNet (String studentId){
        List<Share> results = null;

        Call<DownloadResponse> call = api.getVideo (studentId, "", "U0pUVS1ieXRlZGFuY2UtYW5kcm9pZA==");
        try {
            Response<DownloadResponse> response = call.execute();
            if (response.isSuccessful()) {
                results = response.body().feeds;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d (TAG, "error");
        }

        return results;
    }

    @Override
    public void onRefresh () {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                shares.clear();
                updateData ("");
                mSwipeRefreshLayout.setRefreshing (false);
            }
        }, 1000);
    }
}
