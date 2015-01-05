package com.melorriaga.contentproviderexample;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.melorriaga.contentproviderexample.contentprovider.TodoContentProvider;
import com.melorriaga.contentproviderexample.database.TodoDatabaseHelper;

public class TodoDetailActivity extends ActionBarActivity {

    private Spinner categorySpinner;
    private EditText summaryEditText;
    private EditText descriptionEditText;

    private Uri todoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_detail_activity);

        categorySpinner = (Spinner) findViewById(R.id.todo_category);
        summaryEditText = (EditText) findViewById(R.id.todo_summary);
        descriptionEditText = (EditText) findViewById(R.id.todo_description);

        // check for extras (update scenario)
        todoUri = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            todoUri = extras.getParcelable(TodoContentProvider.CONTENT_ITEM_TYPE);
            if (todoUri != null) {
                loadTodoData(todoUri);
            }
        }

        Button button = (Button) findViewById(R.id.todo_save_or_update_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (summaryEditText.getText().length() == 0 ||
                        descriptionEditText.getText().length() == 0) {
                    Toast.makeText(TodoDetailActivity.this, R.string.fill_fields,
                            Toast.LENGTH_SHORT).show();
                } else {
                    saveOrUpdateTodo();
                }
            }
        });
    }

    private void loadTodoData(Uri todoUri) {
        String[] projection = {
                TodoDatabaseHelper.TodoTable.COLUMN_CATEGORY,
                TodoDatabaseHelper.TodoTable.COLUMN_SUMMARY,
                TodoDatabaseHelper.TodoTable.COLUMN_DESCRIPTION
        };
        Cursor cursor = getContentResolver().query(todoUri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            String category = cursor.getString(cursor.getColumnIndex(
                    TodoDatabaseHelper.TodoTable.COLUMN_CATEGORY));
            for (int i = 0; i < categorySpinner.getCount(); i++) {
                String item = (String) categorySpinner.getItemAtPosition(i);
                if (item.equalsIgnoreCase(category)) {
                    categorySpinner.setSelection(i);
                    break;
                }
            }

            String summary = cursor.getString(cursor.getColumnIndex(
                    TodoDatabaseHelper.TodoTable.COLUMN_SUMMARY));
            summaryEditText.setText(summary);

            String description = cursor.getString(cursor.getColumnIndex(
                    TodoDatabaseHelper.TodoTable.COLUMN_DESCRIPTION));
            descriptionEditText.setText(description);

            cursor.close();
        }
    }

    private void saveOrUpdateTodo() {
        String category = (String) categorySpinner.getSelectedItem();
        String summary = summaryEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        ContentValues values = new ContentValues();
        values.put(TodoDatabaseHelper.TodoTable.COLUMN_CATEGORY, category);
        values.put(TodoDatabaseHelper.TodoTable.COLUMN_SUMMARY, summary);
        values.put(TodoDatabaseHelper.TodoTable.COLUMN_DESCRIPTION, description);

        if (todoUri == null) {
            // save scenario
            getContentResolver().insert(TodoContentProvider.CONTENT_URI, values);
        } else {
            // update scenario
            getContentResolver().update(todoUri, values, null, null);
        }

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.todo_detail_menu, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
