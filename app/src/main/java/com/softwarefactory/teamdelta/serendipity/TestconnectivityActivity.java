package com.softwarefactory.teamdelta.serendipity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import java.net.URL;

/*
* Created by Riku Suomela 2016
*
* This class is used for doing a server connection proof of concept (POC)
* The developer can sandbox network functionalities from this class without messing up the actual UI
* TODO: When the functionality is finished, move it to the right place or refactor this class
*/
public class TestconnectivityActivity extends AppCompatActivity {
    // Final for a TEST server URL
    public final static String testURL = "http://jsonplaceholder.typicode.com/posts";
    JSONObject jsonobject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testconnectivity);
    }

    // When Test Connection button is pressed, the following functionality is executed
    public void testConnection(View view){
        new HTTPCall().execute();
    }

    // This class is used for running the network functionality on a different thread (outside main)
    private class HTTPCall extends AsyncTask<String, String, String> {
        //This method executes the functionality inside it in a separate thread
        @Override
        protected String doInBackground(String... params) {
            jsonobject = JSONfunctions.getJSONfromURL(testURL);
            String jsonDump = jsonobject.toString();
            Log.d("Json Dump", jsonDump);
            return jsonDump;
        }
        // This method takes the result of the doInBackground method and executes it in the main / UI thread
        protected void onPostExecute(String result){
            TextView textView = (TextView)findViewById(R.id.jsonTextView);
            textView.setText(result);
        }
    }
}
