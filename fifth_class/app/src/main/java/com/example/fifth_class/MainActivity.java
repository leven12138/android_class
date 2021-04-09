package com.example.fifth_class;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.example.fifth_class.database.TaskContract;
import com.example.fifth_class.database.TaskDbHelper;
import com.example.fifth_class.task.Task;
import com.example.fifth_class.task.TaskOperator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecycler;
    private listViewAdapter mAdapter;
    private FloatingActionButton mAddButton;

    private TaskDbHelper mDbHelper;
    private SQLiteDatabase mDataBase;

    private static final String TAG = "inform";
    private static final int REQUEST_CODE_ADD = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecycler = (RecyclerView) findViewById (R.id.mRecycle);
        mRecycler.setLayoutManager (new LinearLayoutManager (this));

        mDbHelper = new TaskDbHelper (this);
        mDataBase = mDbHelper.getWritableDatabase ();

        mAdapter = new listViewAdapter(new TaskOperator() {
            @Override
            public void updateTask(Task task) {
                MainActivity.this.updateTask (task);
            }

            @Override
            public void deleteTask(Task task) {
                MainActivity.this.deleteTask (task);
            }
        });
        mRecycler.setAdapter (mAdapter);
        mAdapter.notifyItems (getDataFromDB ());

        mAddButton = (FloatingActionButton) findViewById (R.id.add_button);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult (new Intent (MainActivity.this, createTask.class), REQUEST_CODE_ADD);
            }
        });
    }

    @Override
    protected void onDestroy () {
        super.onDestroy ();
        mDataBase.close ();
        mDataBase = null;
        mDbHelper.close ();
        mDbHelper = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD
                && resultCode == Activity.RESULT_OK) {
            mAdapter.notifyItems (getDataFromDB ());
        }
    }

    private List<Task> getDataFromDB () {
        if (mDataBase == null) {
            return Collections.emptyList ();
        }
        List<Task> get = new LinkedList<> ();
        Cursor cursor = null;
        try {
            cursor = mDataBase.query (TaskContract.TaskTable.TABLE_NAME, null,
                    null, null,
                    null, null,
                    null);

            while (cursor.moveToNext ()){
                long id = cursor.getLong (cursor.getColumnIndex (TaskContract.TaskTable._ID));
                String content = cursor.getString (cursor.getColumnIndex (TaskContract.TaskTable.COLUMN_NAME_CONTENT));
                long dateMs = cursor.getLong (cursor.getColumnIndex (TaskContract.TaskTable.COLUMN_NAME_DATE));
                int intAct = cursor.getInt (cursor.getColumnIndex (TaskContract.TaskTable.COLUMN_NAME_ACTIVATE));

                Task task = new Task (id);
                task.setContent (content);
                task.setDate (new Date (dateMs));
                task.setActivate (intAct != 0);

                get.add (task);
            }
        } finally {
            if (cursor != null) {
                cursor.close ();
            }
        }

        return get;
    }

    private void updateTask (Task task) {
        if (mDataBase == null) {
            return;
        }
        ContentValues values = new ContentValues();
        values.put (TaskContract.TaskTable.COLUMN_NAME_ACTIVATE, task.isActivate()? 1:0);

        int rows = mDataBase.update(TaskContract.TaskTable.TABLE_NAME, values,
                TaskContract.TaskTable._ID + "=?",
                new String[]{ String.valueOf (task.id)});

        if (rows > 0) {
            mAdapter.notifyItems (getDataFromDB ());
        }
    }

    private void deleteTask (Task task) {
        if (mDataBase == null) {
            return;
        }
        int rows = mDataBase.delete (TaskContract.TaskTable.TABLE_NAME,
                TaskContract.TaskTable._ID + "=?",
                new String[]{ String.valueOf (task.id)});

        if (rows > 0) {
            mAdapter.notifyItems (getDataFromDB ());
        }
    }
}