package com.melorriaga.contentproviderexample.contentprovider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.melorriaga.contentproviderexample.database.TodoDatabaseHelper;

public class TodoContentProvider extends ContentProvider {

    // database
    private TodoDatabaseHelper database;

    // the authority for this content provider
    private static final String AUTHORITY = TodoContentProvider.class.getPackage().getName();
    // the base path for this content provider
    private static final String BASE_PATH = "todos";

    // the uri for this content provider
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    public static final String CONTENT_DIR_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "." + BASE_PATH;
    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "." + BASE_PATH;

    // used by the uri matcher
    private static final int TODOS = 1;
    private static final int TODOS_ID = 2;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, BASE_PATH, TODOS);
        URI_MATCHER.addURI(AUTHORITY, BASE_PATH + "/#", TODOS_ID);
    }

    @Override
    public boolean onCreate() {
        database = new TodoDatabaseHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        int uriType = URI_MATCHER.match(uri);
        switch (uriType) {
            case TODOS:
                return CONTENT_DIR_TYPE;
            case TODOS_ID:
                return CONTENT_ITEM_TYPE;
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        long id = 0;

        int uriType = URI_MATCHER.match(uri);
        switch (uriType) {
            case TODOS:
                id = database.getWritableDatabase().insert(
                        TodoDatabaseHelper.TodoTable.TABLE_TODO, null, values);
                break;
            default:
                break;
        }

        // notify registered observers
        getContext().getContentResolver().notifyChange(uri, null);

        // return the uri for the newly inserted item
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // use query builder
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        // set the table
        queryBuilder.setTables(TodoDatabaseHelper.TodoTable.TABLE_TODO);

        int uriType = URI_MATCHER.match(uri);
        switch (uriType) {
            case TODOS:
                break;
            case TODOS_ID:
                // add the 'where' condition
                queryBuilder.appendWhere(TodoDatabaseHelper.TodoTable.COLUMN_ID + " = "
                        + uri.getLastPathSegment());
                break;
            default:
                break;
        }

        // perform the query
        Cursor cursor = queryBuilder.query(database.getReadableDatabase(), projection,
                selection, selectionArgs, null, null, sortOrder);

        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // return a cursor with the results of the query
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        int rowsUpdated = 0;

        int uriType = URI_MATCHER.match(uri);
        switch (uriType) {
            case TODOS:
                rowsUpdated = database.getWritableDatabase().update(
                        TodoDatabaseHelper.TodoTable.TABLE_TODO,
                        values,
                        selection,
                        selectionArgs);
                break;
            case TODOS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = database.getWritableDatabase().update(
                            TodoDatabaseHelper.TodoTable.TABLE_TODO,
                            values,
                            TodoDatabaseHelper.TodoTable.COLUMN_ID + " = " + id,
                            null);
                } else {
                    rowsUpdated = database.getWritableDatabase().update(
                            TodoDatabaseHelper.TodoTable.TABLE_TODO,
                            values,
                            TodoDatabaseHelper.TodoTable.COLUMN_ID + " = " + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                break;
        }

        // notify registered observers
        getContext().getContentResolver().notifyChange(uri, null);

        // return the number of updated rows
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int rowsDeleted = 0;

        int uriType = URI_MATCHER.match(uri);
        switch (uriType) {
            case TODOS:
                rowsDeleted = database.getWritableDatabase().delete(
                        TodoDatabaseHelper.TodoTable.TABLE_TODO,
                        selection,
                        selectionArgs);
                break;
            case TODOS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = database.getWritableDatabase().delete(
                            TodoDatabaseHelper.TodoTable.TABLE_TODO,
                            TodoDatabaseHelper.TodoTable.COLUMN_ID + " = " + id,
                            null);
                } else {
                    rowsDeleted = database.getWritableDatabase().delete(
                            TodoDatabaseHelper.TodoTable.TABLE_TODO,
                            TodoDatabaseHelper.TodoTable.COLUMN_ID + " = " + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                break;
        }

        // notify registered observers
        getContext().getContentResolver().notifyChange(uri, null);

        // return the number of deleted rows
        return rowsDeleted;
    }

}
