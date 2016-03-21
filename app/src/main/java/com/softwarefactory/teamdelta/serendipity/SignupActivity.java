package com.softwarefactory.teamdelta.serendipity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

/*
* Created by Riku Suomela 2016
*
* This class is used for handling all the new user signup activities and functionalities for and
* with the applications own database. This class does NOT have anything to do with Facebook login.
*/
public class SignupActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }
    //When Signup button is pressed, the signup process is initiated and MainActivity started
    public void signup(View view){
        //TODO: Perform signup functions
        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(intent);
    }

}
