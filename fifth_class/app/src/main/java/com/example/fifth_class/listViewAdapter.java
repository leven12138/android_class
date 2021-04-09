package com.example.fifth_class;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fifth_class.task.Task;
import com.example.fifth_class.task.TaskOperator;

import java.util.ArrayList;
import java.util.List;

public class listViewAdapter extends RecyclerView.Adapter<itemViewHolder> {
    @NonNull
    private List<Task> mTasks = new ArrayList<>();
    private final TaskOperator operator;
    private static final String TAG = "inform";

    public listViewAdapter (TaskOperator operator) {
        this.operator = operator;
    }

    @Override
    public itemViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from (parent.getContext()).inflate (R.layout.recycle_item, parent, false);
        return new itemViewHolder (itemView, operator);
    }

    @Override
    public void onBindViewHolder (@NonNull itemViewHolder holder, int position) { holder.bind (mTasks.get (position)); }

    @Override
    public int getItemCount() { return mTasks.size (); }

    public void notifyItems (@NonNull List<Task> tasks) {
        mTasks.clear ();
        if (tasks != null) {
            mTasks.addAll (tasks);
        }
        notifyDataSetChanged ();
    }
}
