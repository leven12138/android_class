package com.example.final_project.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project.R;
import com.example.final_project.model.Share;

import java.util.ArrayList;
import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    @NonNull
    private List<Share> mItems = new ArrayList<>();
    private final VideoOperator videoOperator;
    private static final String TAG = "inform";

    public VideoListAdapter (VideoOperator videoOperator) {
        this.videoOperator = videoOperator;
    }

    @Override
    public ItemViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from (parent.getContext()).inflate (R.layout.item_view, parent, false);
        return new ItemViewHolder (itemView, videoOperator);
    }

    @Override
    public void onBindViewHolder (@NonNull ItemViewHolder holder, int position) { holder.bind (mItems.get (position)); }

    @Override
    public int getItemCount() { return mItems.size (); }

    public void notifyItems (@NonNull List<Share> items) {
        mItems.clear ();
        mItems.addAll (items);
        notifyDataSetChanged ();
    }
}
