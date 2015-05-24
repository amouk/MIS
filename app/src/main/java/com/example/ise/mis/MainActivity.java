package com.example.ise.mis;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity {

    private Button mButtonEmail;
    private EditText mEditTextNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonEmail = (Button)findViewById(R.id.button_main_eMail);
        mEditTextNotice = (EditText)findViewById(R.id.edNotice);
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

    public void onMapButtonClick(View view) {
        Intent intent = new Intent(this, map.class);
        startActivity(intent);
    }

    public void onClickEmail(View view) {
        final Intent intent = new Intent(this, com.example.ise.mis.sendHighlightedNotice.class);
        mButtonEmail.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                intent.putExtra("notice", mEditTextNotice.getText().toString());
                                                startActivity(intent);
                                            }
                                        }
        );
    }
}
