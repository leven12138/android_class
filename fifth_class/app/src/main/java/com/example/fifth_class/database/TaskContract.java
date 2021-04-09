package com.example.fifth_class.database;

import android.provider.BaseColumns;

public final class TaskContract {
    private TaskContract () {}

    public static class TaskTable implements BaseColumns {
        public static final String TABLE_NAME = "task";

        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_ACTIVATE = "state";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TaskTable.TABLE_NAME + "(" +
                    TaskTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TaskTable.COLUMN_NAME_CONTENT + " TEXT, " +
                    TaskTable.COLUMN_NAME_DATE + " INTEGER, " +
                    TaskTable.COLUMN_NAME_ACTIVATE + " INTEGER)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TaskTable.TABLE_NAME;
}




