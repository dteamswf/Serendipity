package com.softwarefactory.teamalpha.serendipity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.Toast;

public class LoginActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

            case R.id.menu_help:
                Toast.makeText(LoginActivity.this, "Help is Selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_about:
                Toast.makeText(LoginActivity.this, "About is Selected", Toast.LENGTH_SHORT).show();
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
}
