package com.example.ise.mis;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by twernicke on 5/24/2015.
 */
public class sendHighlightedNotice extends ActionBarActivity {

    private EditText mEditTextEmailAddress;
    private EditText mEditTextEmailSubject;
    private EditText mEditTextHighlightedNotice;
    private Button mButtonEmailClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_highlighted_notice);

        mEditTextEmailAddress = (EditText)findViewById(R.id.editText_emailAddress);
        mEditTextEmailSubject =(EditText)findViewById(R.id.editText_emailSubject);
        mEditTextHighlightedNotice = (EditText)findViewById(R.id.editText_highlightedNotice);
        mButtonEmailClient = (Button)findViewById(R.id.button_openEmailClient);

        mEditTextEmailSubject.setText(getIntent().getStringExtra("subject"));
        mEditTextHighlightedNotice.setText(createHighlightedNotice(getIntent().getStringExtra("notice")));
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

    public void onClickEmailClient(View view) {
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mEditTextEmailAddress.getText().toString()});
        intent.putExtra(Intent.EXTRA_SUBJECT, mEditTextEmailSubject.getText().toString());
        intent.putExtra(Intent.EXTRA_TEXT, mEditTextHighlightedNotice.getText());
        startActivity(Intent.createChooser(intent, "Choose eMail Client:"));
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
            highlightedNotice += notice.substring((int)indexStartEndSymbol.get(i), (int)indexStartEndSymbol.get(i+1)+1);
        }

        return highlightedNotice;
    }
}
