package com.example.ise.mis;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by twernicke on 5/26/2015.
 */
public class addTag extends ActionBarActivity {

    DBAdapter noticeDB;

    private EditText mEditTextAddTag;
    private ImageButton mButtonAddTag;
    private ListView mListViewTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tag);

        mEditTextAddTag = (EditText)findViewById(R.id.editText_addTag);
        mButtonAddTag = (ImageButton)findViewById(R.id.button_addTag);
        mListViewTags = (ListView)findViewById(R.id.listView_tagList);

        openDB();
        populateListViewFromDB();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    @Override
    protected void onRestart() {
        super.onRestart();
        populateListViewFromDB();
    }

    @Override
    public void onBackPressed() {
        final Intent intent = new Intent(this, com.example.ise.mis.editNotice.class);

        intent.putExtra("id", getIntent().getStringExtra("id"));

        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    private void openDB() {
        noticeDB = new DBAdapter(this);
        noticeDB.open();
    }

    private void closeDB() {
        noticeDB.close();
    }

    private void populateListViewFromDB() {
        Cursor cursor = noticeDB.getAllNoticeIdRowsTag(Long.parseLong(getIntent().getStringExtra("id")));

        //setup mapping from cursor to view fields
        String[] fromFieldNames = new String[] {DBAdapter.KEY_TAG};

        int[] toViewIDs = new int[] {R.id.textView_tag};

        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.tag_layout, cursor, fromFieldNames, toViewIDs, 0);
        mListViewTags.setAdapter(myCursorAdapter);
    }

    public void onClickAddTag(View view) {
        noticeDB.insertRowTag(Long.parseLong(getIntent().getStringExtra("id")), mEditTextAddTag.getText().toString());
        onRestart();
    }
}
