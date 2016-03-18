package com.softwarefactory.teamdelta.serendipity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class LoginActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize the SDK before executing any other operations,
        // especially, if you're using Facebook UI elements.
        FacebookSdk.sdkInitialize(getApplicationContext());
        // TODO: Finalize implementation of Facebook login integration
        // https://developers.facebook.com/docs/facebook-login/android
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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

    //Launches MainActivity when login button is clicked
    public void login(View view){
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
}
