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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by twernicke on 5/24/2015.
 */
public class editNotice extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    DBAdapter noticeDB;

    private EditText mEditTextNoticeSubject;
    private EditText mEditTextNotice;
    private ImageButton mButtonEmail;
    private ImageButton mButtonTag;
    private Button mButtonEmail;
    private GoogleApiClient mGoogleApiClient;
    private double longitude = 6.65550220;
    private double latitude = 50.94519450;
    public static final String TAG = createNotice.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit_notice);

        mEditTextNoticeSubject = (EditText)findViewById(R.id.editText_noticeSubject);
        mEditTextNotice = (EditText)findViewById(R.id.edNotice);
        mButtonEmail = (ImageButton)findViewById(R.id.button_main_eMail);
        mButtonTag = (ImageButton)findViewById(R.id.button_tag);

        buildGoogleApiClient();
        Log.i(TAG, "in onCreate of editNotice");
        openDB();

        setSubjectAndNotice();
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

    private void setSubjectAndNotice() {
        Cursor c = noticeDB.getRowNotice(Long.parseLong(getIntent().getStringExtra("id")));
        mEditTextNoticeSubject.setText(c.getString(noticeDB.COL_SUBJECT));
        mEditTextNotice.setText(c.getString(noticeDB.COL_NOTICE));
    }

    public void onClickSaveNotice(View view) {

        noticeDB.updateRowNotice(Long.parseLong(getIntent().getStringExtra("id")),
                mEditTextNoticeSubject.getText().toString(),
                mEditTextNotice.getText().toString());

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
        final Intent intent = new Intent(this, sendHighlightedNotice.class);

        noticeDB.updateRowNotice(Long.parseLong(getIntent().getStringExtra("id")),
                mEditTextNoticeSubject.getText().toString(),
                mEditTextNotice.getText().toString());
        intent.putExtra("id", getIntent().getStringExtra("id"));
        intent.putExtra("subject", mEditTextNoticeSubject.getText().toString());
        intent.putExtra("notice", mEditTextNotice.getText().toString());

        startActivity(intent);
    }

    public void onClickTag(View view) {
        final Intent intent = new Intent(this, com.example.ise.mis.addTag.class);

        noticeDB.updateRowNotice(Long.parseLong(getIntent().getStringExtra("id")),
                mEditTextNoticeSubject.getText().toString(),
                mEditTextNotice.getText().toString());
        intent.putExtra("id", getIntent().getStringExtra("id"));

        startActivity(intent);
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
}
