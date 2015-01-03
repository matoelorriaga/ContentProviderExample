package com.melorriaga.contentproviderexample.database;

import android.database.sqlite.SQLiteDatabase;

public class TodoTable {

    // database table
    public static final String TABLE_TODO = "todo";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_SUMMARY = "summary";
    public static final String COLUMN_DESCRIPTION = "description";

    // database creation sql statement
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

    private static final String DATABASE_DROP = String.format(
            "DROP TABLE IF EXISTS %s",
            TABLE_TODO);

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL(DATABASE_DROP);
        onCreate(database);
    }

}
