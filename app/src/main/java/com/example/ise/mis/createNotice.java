package com.example.ise.mis;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by twernicke on 5/24/2015.
 */
public class createNotice extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    DBAdapter noticeDB;

    private EditText mEditTextNoticeSubject;
    private EditText mEditTextNotice;
    private Button mButtonEmail;
    private GoogleApiClient mGoogleApiClient;
    private long noticeID;
    private double longitude = -5.35325220;
    private double latitude = 36.14491060;
    public static final String TAG = createNotice.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit_notice);

        mEditTextNoticeSubject = (EditText)findViewById(R.id.editText_noticeSubject);
        mEditTextNotice = (EditText)findViewById(R.id.edNotice);
        mButtonEmail = (Button)findViewById(R.id.button_main_eMail);

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

        noticeID = noticeDB.insertRowNotice(mEditTextNoticeSubject.getText().toString(),
                            mEditTextNotice.getText().toString());

        Intent intent = new Intent(this, com.example.ise.mis.MainActivity.class);
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

    public void onClickEmail(View view) {
        final Intent intent = new Intent(this, com.example.ise.mis.sendHighlightedNotice.class);
        mButtonEmail.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                intent.putExtra("subject", mEditTextNoticeSubject.getText().toString());
                                                intent.putExtra("notice", mEditTextNotice.getText().toString());
                                                startActivity(intent);
                                            }
                                        }
        );
    }
}
