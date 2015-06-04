package com.example.ise.mis;

import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

public class editNotice extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    DBAdapter noticeDB;
    private Cursor mCursor;

    private TextView mTextViewNoticeSubject;
    private EditText mEditTextNoticeSubject;
    private TextView mTextViewNotice;
    private EditText mEditTextNotice;
    private ImageButton mButtonEmail;

    private TextView mTextViewAddTag;
    private EditText mEditTextAddTag;
    private ImageButton mButtonAddTag;
    private TextView mTextViewTags;
    private ListView mListViewTags;

    private GoogleApiClient mGoogleApiClient;
    private double longitude = 6.65550220;
    private double latitude = 50.94519450;
    public static final String TAG = createNotice.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit_notice);

        mTextViewNoticeSubject = (TextView)findViewById(R.id.textView_noticeSubject);
        mEditTextNoticeSubject = (EditText)findViewById(R.id.editText_noticeSubject);
        mTextViewNotice = (TextView)findViewById(R.id.lbNotice);
        mEditTextNotice = (EditText)findViewById(R.id.edNotice);
        mButtonEmail = (ImageButton)findViewById(R.id.button_main_eMail);
        mTextViewAddTag = (TextView)findViewById(R.id.textView_addTag);
        mEditTextAddTag = (EditText)findViewById(R.id.editText_addTag);
        mButtonAddTag = (ImageButton)findViewById(R.id.button_addTag);
        mTextViewTags = (TextView)findViewById(R.id.textView_tags);
        mListViewTags = (ListView)findViewById(R.id.listView_tags);

        mTextViewNoticeSubject.setText("Edit Notice Subject:");
        mTextViewNotice.setText("Edit Notice:");

        buildGoogleApiClient();
        Log.i(TAG, "in onCreate of editNotice");

        openDB();

        setSubjectAndNotice();
        populateListViewFromDB();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
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
        mEditTextAddTag.setText("");
        populateListViewFromDB();
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
        mCursor.close();
    }

    private void openDB() {
        noticeDB = new DBAdapter(this);
        noticeDB.open();
    }

    private void closeDB() {
        noticeDB.close();
    }

    private void setSubjectAndNotice() {
        Cursor c = noticeDB.getRowNotice(Long.parseLong(getIntent().getStringExtra("id")));
        mEditTextNoticeSubject.setText(c.getString(noticeDB.COL_SUBJECT));
        mEditTextNotice.setText(c.getString(noticeDB.COL_NOTICE));
    }

    private void populateListViewFromDB() {
        mCursor = noticeDB.getAllNoticeIdRowsTag(Long.parseLong(getIntent().getStringExtra("id")));

        //setup mapping from cursor to view fields
        String[] fromFieldNames = new String[] {DBAdapter.KEY_TAG};

        int[] toViewIDs = new int[] {R.id.textView_tag};

        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(getBaseContext(), R.layout.tag_layout, mCursor, fromFieldNames, toViewIDs, 0);
        mListViewTags.setAdapter(myCursorAdapter);
    }

    public void onClickSaveNotice(View view) {

        noticeDB.updateRowNotice(Long.parseLong(getIntent().getStringExtra("id")),
                mEditTextNoticeSubject.getText().toString(),
                mEditTextNotice.getText().toString());

        Toast.makeText(getBaseContext(), "Notice updated..", Toast.LENGTH_SHORT).show();

        onRestart();
    }

    public void onMapButtonClick(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        noticeDB.updateRowNotice(Long.parseLong(getIntent().getStringExtra("id")), mEditTextNoticeSubject.getText().toString(),
                mEditTextNotice.getText().toString());
        noticeDB.insertRowLocation(Long.parseLong(getIntent().getStringExtra("id")), latitude, longitude);
        intent.putExtra("noticeID", getIntent().getStringExtra("id"));
        Log.i(TAG, "NOTICEID: " + getIntent().getStringExtra("id"));
        startActivity(intent);
    }

    public void onClickEmail(View view) {
        final Intent intent = new Intent(Intent.ACTION_SEND);

        noticeDB.updateRowNotice(Long.parseLong(getIntent().getStringExtra("id")),
                mEditTextNoticeSubject.getText().toString(),
                mEditTextNotice.getText().toString());

        Toast.makeText(getBaseContext(), "Notice saved..", Toast.LENGTH_SHORT).show();

        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_SUBJECT, mEditTextNoticeSubject.getText().toString());
        intent.putExtra(Intent.EXTRA_TEXT, createHighlightedNotice(mEditTextNotice.getText().toString()));

        startActivity(Intent.createChooser(intent, "Choose eMail Client:"));
    }

    public void onClickAddTag(View view) {
        if(noticeDB.insertRowUniqueTag(Long.parseLong(getIntent().getStringExtra("id")), mEditTextAddTag.getText().toString()) >= 0) {
            Toast.makeText(getBaseContext(), "Tag added..", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), "Tag already exists..", Toast.LENGTH_SHORT).show();
        }
        onRestart();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        //if (mLastLocation != null) {
        //Log.i(TAG, "current latitude: " + String.valueOf(mLastLocation.getLatitude()));
        //Log.i(TAG, "current longitude: " + String.valueOf(mLastLocation.getLongitude()));
        //noticeDB.insertRowLocation(noticeID, mLastLocation.getLatitude(), String.valueOf(mLastLocation.getLongitude());
        //noticeDB.insertRowLocation(noticeID, 36.14491060, -5.35325220);
        //}
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private String createHighlightedNotice(String notice) {
        String highlightedNotice = new String();

        int startEnd = 0;
        char[] parseSymbols = {'<', '>'};
        ArrayList<Integer> indexStartEndSymbol = new ArrayList<Integer>();

        for(int i=0; i<notice.length(); i++) {
            if(notice.charAt(i) == parseSymbols[startEnd]) {
                indexStartEndSymbol.add((Integer)i);
                startEnd = 1 - startEnd;
            }
        }

        for(int i=0; i<indexStartEndSymbol.size()-1; i=i+2) {
            highlightedNotice += notice.substring((int)indexStartEndSymbol.get(i)+1, (int)indexStartEndSymbol.get(i+1)) + "\n";
        }

        return highlightedNotice;
    }
}
