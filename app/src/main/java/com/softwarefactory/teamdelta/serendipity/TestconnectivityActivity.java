package com.softwarefactory.teamdelta.serendipity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.json.JSONObject;

import java.net.URL;

public class TestconnectivityActivity extends AppCompatActivity {
    public final static String testURL = "http://httpbin.org/get";
    JSONObject jsonobject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testconnectivity);
    }

    //When Test Connection button is pressed, the following functionality is executed
    //TODO This functionality needs to wrapped in an AsyncTask beause networking cannot be run on the main thread and: Implement an AsyncTask!
    public void testConnection(View view){
        jsonobject = JSONfunctions.getJSONfromURL(testURL);
        System.out.print("SERVER JSON DUMP: " + jsonobject.toString());
    }
}
