package com.softwarefactory.teamdelta.serendipity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/*
* Created by Riku Suomela 2016
*
* This class is an android activity for dealing with signing up new users and logging in existing
* users. Signing up and logging can be done currently either by using the applications own database
* or by using Facebook login when enabled by the user.
*/
public class LoginActivity extends ActionBarActivity {
    private CallbackManager callbackManager;

    private LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize the SDK before executing any other operations,
        // especially, if you're using Facebook UI elements.
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = (LoginButton)findViewById(R.id.loginButtonFacebook);

        // Creating and registering callbacks for the Facebook login button
        // Toasts provide feedback for users about the actions, Success, Cancel or Error
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(LoginActivity.this,
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token: "
                                + loginResult.getAccessToken().getToken(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Login attempt cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(LoginActivity.this, "Login attempt failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Handling the login activity by overriding the existing onActivityResult method and passing
    // its parameters to the callbackmanagers method, respectively
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Act on the basis of which item is selected in the menu
        switch (item.getItemId())
        {
            case R.id.action_settings:
                Toast.makeText(LoginActivity.this, "Settings selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_help:
                Toast.makeText(LoginActivity.this, "Help is Selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_about:
                Toast.makeText(LoginActivity.this, "About is Selected", Toast.LENGTH_SHORT).show();
                return true;
            // Launch testConnectivity sandboxing class when this menu item is clicked
            case R.id.action_testConnectivity:
                Intent intent = new Intent(this, TestconnectivityActivity.class);
                this.startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Launches MainActivity when ByPass button is clicked
    // TODO: REMOVE WHEN REST API LOGIN FUNCTIONALITY IS COMPLETED
    public void toMainMenu(View view){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    //Launches SignupActivity when signup button is clicked
    public void signup(View view){
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }

    // App Events let you measure installs on your mobile app ads, create high value audiences for
    // targeting, and view analytics including user demographics.
    // Dashboard accessible at https://www.facebook.com/analytics/1154915717882750/
    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    // To accurately track the time people spend in the app
    // Dashboard accessible at https://www.facebook.com/analytics/1154915717882750/
    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    /*
     * Following is the functonality for creating a RESTful HTTP connection and handling the login data
     *
     */

    public String session_name;
    public String session_id;
    public String usernameEntered;
    public String passwordEntered;


    //background task to login into Drupal
    private class LoginProcess extends AsyncTask<String, Integer, Integer>{

        protected Integer doInBackground(String... params) {

            HttpClient httpclient = new DefaultHttpClient();

            //set the remote endpoint URL
            HttpPost httppost = new HttpPost("http://aruns.ihostfull.com/drupal/rest/user/login");

            // TODO: Check if CSRF validation is required here and how to do it
            /*
             * 1st part of the three part session authentication chain
             */
            try {

                //get the UI elements for username and password
                EditText username= (EditText) findViewById(R.id.usernameEdit);
                EditText password= (EditText) findViewById(R.id.passwordEdit);


                JSONObject json = new JSONObject();
                //extract the username and password from UI elements and create a JSON object
                json.put("username", usernameEntered);
                json.put("password", passwordEntered);

                //add serialised JSON object into POST request
                StringEntity se = new StringEntity(json.toString());
                //set request content type
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                httppost.setEntity(se);

                //send the POST request
                HttpResponse response = httpclient.execute(httppost);

                //read the response from Services endpoint
                String jsonResponse = EntityUtils.toString(response.getEntity());
                Log.d("JSON DUMP", jsonResponse);

                JSONObject jsonObject = new JSONObject(jsonResponse);
                Log.d("Json Object:", jsonObject.toString());
                //read the session information
                session_name=jsonObject.getString("session_name");
                Log.d("Session name: ", session_name);
                session_id=jsonObject.getString("sessid");
                Log.d("Session id: ", session_id);

                return 0;

            }catch (Exception e) {
                Log.v("Error logging in", e.getMessage());
            }

            /*
             * 2nd part of the three part session authentication chain
             */
            HttpGet httpGet = new HttpGet("http://aruns.ihostfull.com/drupal/services/session/token");
            httpGet.setHeader("Cookie", session_id);

            JSONArray json = new JSONArray();
            try {
                HttpResponse response = httpclient.execute(httpGet);

                //read the response and convert it into JSON array
                json = new JSONArray(EntityUtils.toString(response.getEntity()));
                //return the JSON array for post processing to onPostExecute function
                Log.d("2nd JSON DUMP", json.toString());
            }catch (Exception e) {
                Log.v("Error retrieving token", e.getMessage());
            }

            return 0;
        }

        protected void onPreExecute() {
            //get the UI elements for username and password
            EditText username= (EditText) findViewById(R.id.usernameEdit);
            EditText password= (EditText) findViewById(R.id.passwordEdit);
            usernameEntered = username.getText().toString().trim();
            passwordEntered = password.getText().toString().trim();
        }


        protected void onPostExecute(Integer result) {

            Toast.makeText(LoginActivity.this, "SESSION_ID: " + session_id + "\n" + "SESSION_NAME: " + session_name , Toast.LENGTH_SHORT).show();


            //TODO: Use the following intent based functionality instead of the toast above after testing is done!

           /* //create an intent to start the ListActivity
            Intent intent = new Intent(LoginActivity.this, ListActivity.class);
            //pass the session_id and session_name to ListActivity
            intent.putExtra("SESSION_ID", session_id);
            intent.putExtra("SESSION_NAME", session_name);
            //start the ListActivity
            startActivity(intent);*/
        }
    }

    //click listener for doLogin button
    public void login(View view){
        new LoginProcess().execute();
    }

}
