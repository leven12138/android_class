package com.example.first_class;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    @NonNull
    private List<String> mItems = new ArrayList<> ();

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder (LayoutInflater.from (parent.getContext ()).inflate (R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder (@NonNull ItemViewHolder holder, int postion) {
        holder.bind (mItems.get (postion));
    }

    @Override
    public int getItemCount () {return mItems.size ();}

    public void notifyItems (@NonNull List<String> items) {
        mItems.clear ();
        mItems.addAll (items);
        notifyDataSetChanged ();
    }
}
