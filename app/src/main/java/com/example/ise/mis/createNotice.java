package com.example.ise.mis;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;

/**
 * Created by twernicke on 5/24/2015.
 */
public class createNotice extends ActionBarActivity {

    DBAdapter noticeDB;

    private EditText mEditTextNoticeSubject;
    private EditText mEditTextNotice;
    private ImageButton mButtonEmail;
    private ImageButton mButtonTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit_notice);

        mEditTextNoticeSubject = (EditText)findViewById(R.id.editText_noticeSubject);
        mEditTextNotice = (EditText)findViewById(R.id.edNotice);
        mButtonEmail = (ImageButton)findViewById(R.id.button_main_eMail);
        mButtonTag = (ImageButton)findViewById(R.id.button_tag);

        openDB();
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
    public void onBackPressed() {
        final Intent intent = new Intent(this, com.example.ise.mis.MainActivity.class);

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

    public void onClickSaveNotice(View view) {

        noticeDB.insertRowNotice(mEditTextNoticeSubject.getText().toString(),
                            mEditTextNotice.getText().toString());

        Intent intent = new Intent(this, com.example.ise.mis.MainActivity.class);
        startActivity(intent);
    }

    public void onMapButtonClick(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void onClickEmail(View view) {
        final Intent intent = new Intent(this, com.example.ise.mis.sendHighlightedNotice.class);

        intent.putExtra("id", String.valueOf(noticeDB.insertRowNotice(
                        mEditTextNoticeSubject.getText().toString(),
                        mEditTextNotice.getText().toString())));
        intent.putExtra("subject", mEditTextNoticeSubject.getText().toString());
        intent.putExtra("notice", mEditTextNotice.getText().toString());

        startActivity(intent);
    }

    public void onClickTag(View view) {
        final Intent intent = new Intent(this, com.example.ise.mis.addTag.class);

        intent.putExtra("id", noticeDB.insertRowNotice(
                        mEditTextNoticeSubject.getText().toString(),
                        mEditTextNotice.getText().toString()));

        startActivity(intent);
    }
}
