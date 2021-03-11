package com.example.second_class.page;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.second_class.R;

public class itemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView mItemView;
    private static final String TAG = "inform";

    public itemViewHolder (@NonNull View itemView) {
        super (itemView);
        mItemView = itemView.findViewById (R.id.mRecycle_item);
        itemView.setOnClickListener (this);
    }

    public void bind (String text) { mItemView.setText (text); }

    @Override
    public void onClick (View v) {
        Log.d (TAG, "you click " + mItemView.getText ().toString ());
    }
}
