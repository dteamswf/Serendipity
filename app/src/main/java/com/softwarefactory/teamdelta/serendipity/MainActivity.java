package com.softwarefactory.teamdelta.serendipity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends ActionBarActivity {

/*
* Created by Riku Suomela 2016
*
* This class is the main activity of the application aka here the users will spend most of their time.
* This activity contains the functionality for recording and saving audio files. Additionally in this
* class the saved audio files are listed in a list view. This class also provides access to Google maps
* for the user.
*
* The code base in this class regarding the audio recording functionality ONLY was retrieved from
* http://developer.android.com/guide/topics/media/audio-capture.html
* The original example code has been modified to fit the needs of this project. For example the
* whole UI is not defined at runtime but rather a couple of buttons are defined at runtime and
* assigned to the XML layout
*/
    private static final String LOG_TAG = "AudioRecordTest";

    private static String mFileName = "recording_.3gp";
    private static File storagePath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Serendipity" + "/" + mFileName);
    //private static File audioFilePath= new File(storagePath + "/" + mFileName);
    private String timeStamp = null;

    private RecordButton mRecordButton = null;
    private MediaRecorder mRecorder = null;

    private PlayButton   mPlayButton = null;
    private MediaPlayer mPlayer = null;

    private List<String> myList;

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }
    // Method for playing the saved audio
    //TODO: Make this play the selected audio because currently each saved recording has unique id
    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(storagePath.toString());
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }
    // Method for releasing the mediaplayer instance when not needed
    private void stopPlaying() {
        if (mPlayer != null){
            mPlayer.release();
            mPlayer = null;
        } else{
        }
    }
    // Method for recording audio
    private void startRecording() {
        // Initialize a timestamp for the recording
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        // Initializing Media Recorder instance
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        // If directory exists we save there and if not we create a new one and save there
        if(storagePath.isDirectory()) {
            mRecorder.setOutputFile(storagePath.toString() + timeStamp + ".3gp");
        }else{
            // Directory structure is built if needed
            storagePath.mkdir();
            mRecorder.setOutputFile(storagePath.toString() + timeStamp + ".3gp");

        }
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
        mRecorder.start();
    }
    // Method for stopping the recording and releasing the meadiarecorder instance
    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        Toast.makeText(getApplicationContext(), "Recording finished!",
                Toast.LENGTH_SHORT).show();
    }
    // Define functonality for dynamic buttons
    class RecordButton extends Button {
        boolean mStartRecording = true;
        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    setText("Stop recording");
                } else {
                    setText("Record");
                }
                mStartRecording = !mStartRecording;
            }
        };

        public RecordButton(Context ctx) {
            super(ctx);
            setText("Record");
            setOnClickListener(clicker);
        }
    }
    // Define functonality for dynamic buttons
    class PlayButton extends Button {
        boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    setText("Stop playing");
                } else {
                    setText("Play");
                }
                mStartPlaying = !mStartPlaying;
            }
        };

        public PlayButton(Context ctx) {
            super(ctx);
            setText("Play");
            setOnClickListener(clicker);
        }
    }

    // onCreate initializes the activity and here buttons for the UI are defined at runtime,
    // dynamically to allow some flexibility with the changing states of the buttons
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Define buttons and set parameters
        mRecordButton = new RecordButton(this);
        mRecordButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mPlayButton = new PlayButton(this);
        mPlayButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        // Get container for buttons from the xml and add the buttons to it
        LinearLayout container = (LinearLayout)findViewById(R.id.buttonContainer);
        container.addView(mRecordButton);
        container.addView(mPlayButton);

        // Set up array adapter for listview to list the saved recordings
        ListView listView = (ListView) findViewById(R.id.listViewRecordings);
        myList = new ArrayList<String>();

        //Check if a folder for the audio exists, if not then creates one
        File directory = Environment.getExternalStorageDirectory();
        File newDirectory = new File( directory + "/Serendipity" );
        if (!newDirectory.isDirectory()){
            newDirectory.mkdir();
        }
        File list[] = newDirectory.listFiles();
        if (list != null) {
            for (int i = 0; i < list.length; i++) {
                myList.add(list[i].getName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, myList);
            listView.setAdapter(adapter); //Set all the file in the list.
        } else{
            Toast.makeText(MainActivity.this,
                    "The Serendipity directory is empty: fill it up by recording sounds!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }
    //When mapsButton is pressed, launch MapsActivity
    public void launchMap(View view){
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(intent);
    }

}
