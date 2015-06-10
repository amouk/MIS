package com.example.ise.mis;

import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;


public class XmlActivity extends ActionBarActivity {

    private DBAdapter noticeDB;

    private EditText edTextXml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xml);

        edTextXml = (EditText)findViewById(R.id.edTextXml);

        noticeDB = new DBAdapter(this);
        noticeDB.open();

        fillXmlTextField();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_xml, menu);
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
    public void onDestroy() {
        noticeDB.close();
        super.onDestroy();
    }

    private void fillXmlTextField() {
        // get all entries from notice DB
        Cursor noticesCursor = noticeDB.getAllRowsNotice();

        // transform into XML-structure
        List<String> xml = new ArrayList<>();

        if (noticesCursor == null) {
            return;
        }

        xml.add("<notices>");
        do {
            long noticeId = noticesCursor.getLong(noticesCursor.getColumnIndex("_id"));

            xml.add("<notice>");
            xml.add("<title>");
            xml.add(noticesCursor.getString(noticesCursor.getColumnIndex("subject")));
            xml.add("</title>");
            xml.add("<text>");
            xml.add(noticesCursor.getString(noticesCursor.getColumnIndex("notice")));
            xml.add("</text>");
            xml.add("<tags>");

            // get all tags for noticeId
            Cursor tagCursor = noticeDB.getAllNoticeIdRowsTag(noticeId);
            if (tagCursor != null) {
                do {
                    xml.add("<tag>");
                    xml.add(tagCursor.getString(tagCursor.getColumnIndex("tag")));
                    xml.add("</tag>");
                } while (tagCursor.moveToNext());
                tagCursor.close();
            }


            xml.add("</tags>");
            xml.add("<locations>");

            // get all locations for noticeId
            Cursor locationCursor = noticeDB.getAllNoticeIdRowsLocation(noticeId);
            if (locationCursor != null) {
                do {
                    xml.add("<latitude>");
                    xml.add(Double.toString(locationCursor.getDouble(locationCursor.getColumnIndex("latitude"))));
                    xml.add("</latitude>");
                    xml.add("<longitude>");
                    xml.add(Double.toString(locationCursor.getDouble(locationCursor.getColumnIndex("longitude"))));
                    xml.add("</longitude>");
                } while (locationCursor.moveToNext());
                locationCursor.close();
            }


            xml.add("</locations>");
            xml.add("</notice>");
        } while (noticesCursor.moveToNext());
        xml.add("</notices>");
        noticesCursor.close();

        // write XML into EditText
        for (String xmlLine : xml) {
            edTextXml.append(xmlLine);
            edTextXml.append(System.getProperty("line.separator"));
        }
    }
}
