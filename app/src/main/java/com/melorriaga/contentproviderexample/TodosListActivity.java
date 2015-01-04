package com.melorriaga.contentproviderexample;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.melorriaga.contentproviderexample.contentprovider.TodoContentProvider;
import com.melorriaga.contentproviderexample.database.TodoDatabaseHelper;

public class TodosListActivity extends ActionBarActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private SimpleCursorAdapter simpleCursorAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todos_list_activity);

        listView = (ListView) findViewById(R.id.todo_list);

        loadData();
    }

    private void loadData() {
        String[] fieldsData = {
                TodoDatabaseHelper.TodoTable.COLUMN_SUMMARY
        };
        int[] fieldsResource = {
                R.id.todo_list_item
        };

        getLoaderManager().initLoader(0, null, this);
        simpleCursorAdapter = new SimpleCursorAdapter(this,
                R.layout.todo_list_item, null, fieldsData, fieldsResource, 0);

        listView.setAdapter(simpleCursorAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.todos_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_create) {
            openCreateTodoActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openCreateTodoActivity() {
        Intent intent = new Intent(TodosListActivity.this, TodoDetailActivity.class);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                TodoDatabaseHelper.TodoTable.COLUMN_ID,
                TodoDatabaseHelper.TodoTable.COLUMN_SUMMARY
        };
        CursorLoader cursorLoader = new CursorLoader(this,
                TodoContentProvider.CONTENT_URI, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        simpleCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        simpleCursorAdapter.swapCursor(null);
    }

}
