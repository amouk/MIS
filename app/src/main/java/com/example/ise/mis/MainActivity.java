package com.example.ise.mis;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    DBAdapter noticeDB;

    private Button mButtonCreateNotice;
    private ListView mListViewNotices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonCreateNotice = (Button)findViewById(R.id.button_addNotice);
        mListViewNotices = (ListView)findViewById(R.id.listView_notices);

        openDB();
        populateListViewFromDB();

        onListViewNoticeClick();
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
        Cursor cursor = noticeDB.getAllRowsNotice();

        //setup mapping from cursor to view fields
        String[] fromFieldNames = new String[] {DBAdapter.KEY_ROWID, DBAdapter.KEY_SUBJECT};

        int[] toViewIDs = new int[] {R.id.textView_noticeId, R.id.textView_noticeSubject};

        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.item_layout, cursor, fromFieldNames, toViewIDs, 0);
        mListViewNotices.setAdapter(myCursorAdapter);
    }

    public void onListViewNoticeClick() {
        final Intent intent = new Intent(this, com.example.ise.mis.editNotice.class);
        mListViewNotices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long idInDB) {
                Log.w("--- onItemClick ---: ","DB id: " + String.valueOf(idInDB));
                intent.putExtra("id", String.valueOf(idInDB));
                startActivity(intent);
            }
        });
    }

    public void onClickCreateNotice(View view) {
        final Intent intent = new Intent(this, com.example.ise.mis.createNotice.class);
        startActivity(intent);
    }
}
