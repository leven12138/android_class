package com.example.fifth_class;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fifth_class.task.Task;
import com.example.fifth_class.task.TaskOperator;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class itemViewHolder extends RecyclerView.ViewHolder {
    private CheckBox mState;
    private TextView mContent;
    private TextView mDate;
    private ImageButton mDelete;
    private final TaskOperator operator;
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat ("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH);
    private static final String TAG = "inform";

    public itemViewHolder (@NonNull View itemView, TaskOperator operator) {
        super (itemView);
        this.operator = operator;
        mState = (CheckBox) itemView.findViewById (R.id.item_state);
        mContent = (TextView) itemView.findViewById (R.id.item_content);
        mDate = (TextView) itemView.findViewById (R.id.item_date);
        mDelete = (ImageButton) itemView.findViewById (R.id.item_del);
    }

    public void bind (final Task task) {
        mState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                task.setActivate ( !isChecked );
                operator.updateTask (task);
            }
        });

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operator.deleteTask (task);
            }
        });

        mContent.setText (task.getContent ());
        mDate.setText (SIMPLE_DATE_FORMAT.format (task.getDate ()));

        if (task.isActivate ()) {
            mContent.setTextColor (Color.BLACK);
            mContent.setPaintFlags (mContent.getPaintFlags () & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }else {
            mContent.setTextColor (Color.GRAY);
            mContent.setPaintFlags (mContent.getPaintFlags () | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }
}
