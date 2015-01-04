package com.melorriaga.contentproviderexample;

import android.content.ContentValues;
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

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_detail_activity);

        categorySpinner = (Spinner) findViewById(R.id.todo_category);
        summaryEditText = (EditText) findViewById(R.id.todo_summary);
        descriptionEditText = (EditText) findViewById(R.id.todo_description);

        button = (Button) findViewById(R.id.todo_save_or_update_button);
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

    private void saveOrUpdateTodo() {
        String category = (String) categorySpinner.getSelectedItem();
        String summary = summaryEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        ContentValues values = new ContentValues();
        values.put(TodoDatabaseHelper.TodoTable.COLUMN_CATEGORY, category);
        values.put(TodoDatabaseHelper.TodoTable.COLUMN_SUMMARY, summary);
        values.put(TodoDatabaseHelper.TodoTable.COLUMN_DESCRIPTION, description);

        getContentResolver().insert(TodoContentProvider.CONTENT_URI, values);

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
