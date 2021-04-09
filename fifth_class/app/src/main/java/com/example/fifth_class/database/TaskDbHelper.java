package com.example.fifth_class.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.example.fifth_class.database.TaskContract.SQL_CREATE_ENTRIES;
import static com.example.fifth_class.database.TaskContract.SQL_DELETE_ENTRIES;

public class TaskDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Task.db";
    private static final String TAG = "inform";

    public TaskDbHelper (Context context) {
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate (SQLiteDatabase db) {
        Log.d (TAG, "heihei " + SQL_CREATE_ENTRIES);
        db.execSQL (SQL_CREATE_ENTRIES);
    }

    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL (SQL_DELETE_ENTRIES);
        onCreate (db);
    }

    public void onDowngrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade (db, oldVersion, newVersion);
    }
}
