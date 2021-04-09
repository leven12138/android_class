package com.example.fifth_class;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fifth_class.database.TaskContract;
import com.example.fifth_class.database.TaskDbHelper;

public class createTask extends AppCompatActivity {
    private EditText mEditText;
    private Button mButton;

    private TaskDbHelper mDbHelper;
    private SQLiteDatabase mDataBase;

    private static final String TAG = "inform";

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_add);

        mDbHelper = new TaskDbHelper (this);
        mDataBase = mDbHelper.getWritableDatabase ();
        Log.d (TAG, mDataBase.getPath ().toString ());

        mEditText = (EditText) findViewById(R.id.input);
        mEditText.setFocusable (true);
        mEditText.requestFocus ();

        mButton = (Button) findViewById (R.id.confirm);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mEditText.getText ().toString ();
                if (content == "") {
                    Toast.makeText (createTask.this, "没有输入内容", Toast.LENGTH_SHORT).show ();
                }else {
                    boolean flag = insertIntoDb (content);
                    if (flag) {
                        Toast.makeText (createTask.this, "成功新增任务", Toast.LENGTH_SHORT).show ();
                        setResult (Activity.RESULT_OK);
                        finish ();
                    }else {
                        Toast.makeText (createTask.this, "新增任务失败", Toast.LENGTH_SHORT).show ();
                    }
                }
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

    private boolean insertIntoDb (String content) {
        if (mDataBase == null) {
            return false;
        }
        Log.d (TAG, TaskContract.SQL_CREATE_ENTRIES);

        ContentValues values = new ContentValues ();
        values.put (TaskContract.TaskTable.COLUMN_NAME_CONTENT, content);
        values.put (TaskContract.TaskTable.COLUMN_NAME_DATE, System.currentTimeMillis ());
        values.put (TaskContract.TaskTable.COLUMN_NAME_ACTIVATE, 1);
        long ID = mDataBase.insert (TaskContract.TaskTable.TABLE_NAME, null, values);

        return ID != -1;
    }
}
