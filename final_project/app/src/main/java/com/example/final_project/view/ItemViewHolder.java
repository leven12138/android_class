package com.example.final_project.view;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project.R;
import com.example.final_project.model.Share;
import com.facebook.drawee.view.SimpleDraweeView;

public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private SimpleDraweeView coverImg;
    private TextView Name;
    private String videoUri;
    private Share mShare;
    private final VideoOperator videoOperator;

    private final static String TAG = "inform";

    public ItemViewHolder (@NonNull View itemView, VideoOperator videoOperator) {
        super(itemView);
        this.videoOperator = videoOperator;
        coverImg = itemView.findViewById (R.id.cover_img);
        Name = itemView.findViewById (R.id.user_name);
        itemView.setOnClickListener (this);
    }

    public void bind (Share share){
        coverImg.setImageURI (share.getImageUrl ());
        Name.setText (share.getUserName ());
        this.videoUri = share.getVideoUrl ();
        this.mShare = share;
    }

    @Override
    public void onClick (View v) {
        videoOperator.setVideoView (mShare);
        Log.d (TAG, Name.getText().toString());
    }
}
