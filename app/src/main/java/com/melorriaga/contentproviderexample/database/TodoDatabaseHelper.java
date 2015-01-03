package com.melorriaga.contentproviderexample.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TodoDatabaseHelper extends SQLiteOpenHelper {

    public static class TodoTable {

        // database table
        public static final String TABLE_TODO = "todo";

        // database columns
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_SUMMARY = "summary";
        public static final String COLUMN_DESCRIPTION = "description";

        // database create statement
        private static final String DATABASE_CREATE = String.format(
                "CREATE TABLE %s (" +
                        "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT NOT NULL, " +
                        "%s TEXT NOT NULL, " +
                        "%s TEXT NOT NULL" +
                        ");",
                TABLE_TODO,
                COLUMN_ID,
                COLUMN_CATEGORY,
                COLUMN_SUMMARY,
                COLUMN_DESCRIPTION);

        // database drop statement
        private static final String DATABASE_DROP = String.format(
                "DROP TABLE IF EXISTS %s",
                TABLE_TODO);

    }

    // database name
    private static final String DATABASE_NAME = "todotable.db";
    // database version
    private static final int DATABASE_VERSION = 1;

    // constructor
    public TodoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // this method is called during the creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(TodoTable.DATABASE_CREATE);
    }

    // this method is called during an upgrade if the database,
    // e.g. if you increase the database version
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL(TodoTable.DATABASE_DROP);
        database.execSQL(TodoTable.DATABASE_CREATE);
    }

}
