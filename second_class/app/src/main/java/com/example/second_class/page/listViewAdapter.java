package com.example.second_class.page;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.second_class.R;

import java.util.ArrayList;
import java.util.List;

public class listViewAdapter extends RecyclerView.Adapter<itemViewHolder> {
    @NonNull
    private List<String> mItems = new ArrayList<> ();
    private static final String TAG = "inform";

    @Override
    public itemViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from (parent.getContext()).inflate (R.layout.recycle_item, parent, false);
        return new itemViewHolder (itemView);
    }

    @Override
    public void onBindViewHolder (@NonNull itemViewHolder holder, int position) { holder.bind (mItems.get (position)); }

    @Override
    public int getItemCount() { return mItems.size (); }

    public void notifyItems (@NonNull List<String> items) {
        mItems.clear ();
        mItems.addAll (items);
        notifyDataSetChanged ();
    }
}
