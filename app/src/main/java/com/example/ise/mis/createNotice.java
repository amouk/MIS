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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class createNotice extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    DBAdapter noticeDB;

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
    private long noticeID;
    private double longitude = -5.35325220;
    private double latitude = 36.14491060;
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

        mTextViewNoticeSubject.setText("Create Notice Subject:");
        mTextViewNotice.setText("Create Notice:");

        mButtonEmail.setVisibility(View.INVISIBLE);
        mTextViewAddTag.setVisibility(View.INVISIBLE);
        mEditTextAddTag.setVisibility(View.INVISIBLE);
        mButtonAddTag.setVisibility(View.INVISIBLE);
        mTextViewTags.setVisibility(View.INVISIBLE);
        mListViewTags.setVisibility(View.INVISIBLE);

        buildGoogleApiClient();
        Log.i(TAG, "in onCreate of createNotice");
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
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        closeDB();
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
    public void onConnected(Bundle connectionHint) {
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

    private void openDB() {
        noticeDB = new DBAdapter(this);
        noticeDB.open();
    }

    private void closeDB() {
        noticeDB.close();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    public void onClickSaveNotice(View view) {
        final Intent intent = new Intent(this, com.example.ise.mis.editNotice.class);

        intent.putExtra("id", String.valueOf(noticeDB.insertRowNotice(
                mEditTextNoticeSubject.getText().toString(),
                mEditTextNotice.getText().toString())));

        Toast.makeText(getBaseContext(), "Notice created..", Toast.LENGTH_SHORT).show();

        startActivity(intent);
    }

    public void onMapButtonClick(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        noticeID = noticeDB.insertRowNotice(mEditTextNoticeSubject.getText().toString(),
                mEditTextNotice.getText().toString());
        noticeDB.insertRowLocation(noticeID, latitude, longitude);
        intent.putExtra("noticeID", String.valueOf(noticeID));
        Log.i(TAG,"NOTICEID: " + noticeID);
        startActivity(intent);
    }
}
