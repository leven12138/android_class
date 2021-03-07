package com.example.first_class;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView mItemView;
    private static final String TAG = "MainActivity";

    public ItemViewHolder (@NonNull View itemView) {
        super (itemView);
        mItemView = itemView.findViewById (R.id.mRecycle_item);
        itemView.setOnClickListener (this);
    }

    public void bind (String text) {mItemView.setText (text);}

    @Override
    public void onClick (View v) {
        Intent intent = new Intent (v.getContext (), ExampleActivity.class);
        intent.putExtra ("extra", mItemView.getText ().toString ());
        v.getContext ().startActivity (intent);
        Log.d (TAG, "go to " + mItemView.getText ().toString ());
    }
}
