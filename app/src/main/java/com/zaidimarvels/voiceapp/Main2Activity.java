package com.zaidimarvels.voiceapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private TextToSpeech tts;
    private SpeechRecognizer speechRecog;
    Button Example;
    FloatingActionButton fab;
    // For demonstration purposes, "Basics of Computer" is the only course that accepts input from the user
    public String commands = "Greetings Learners. Available commands are. List courses. Check status. Resum learning. see more. say. repeat. to repeat. commands.";
    public String courses = "Recommended Learning." + "Basics of Computer." + "." +
                                "Advanced Python." + "." + "Deep Dive: Python." + "." +
                                "Intro To JavaScript." + "." + "Advanced JavaScript." + "." +
                                "Deep Dive: JavaScript." + "." + "Want to hear more courses?." +
                                "Say." + "More Courses." + "." + "Want to check your learning status."  +
                                "Say." + "Check status." + "." + "Want to Resume your previous learning." +
                                "Say." + "." + "resume Learning.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Example = findViewById(R.id.Example);
        Example.setOnClickListener(this);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        initializeTextToSpeech();
        initializeSpeechRecognizer();
    }

    private void initializeSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            speechRecog = SpeechRecognizer.createSpeechRecognizer(this);
            speechRecog.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int error) {

                }

                @Override
                public void onResults(Bundle results) {
                    List<String> result_arr = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    processResult(result_arr.get(0));
                }

                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
        }
    }

    private void processResult(String result_message) {
        result_message = result_message.toLowerCase();

//        Handle at least four sample cases

//        First: What is your Name?
//        Second: What is the time?
//        Third: Is the earth flat or a sphere?
//        Fourth: Open a browser and open url
        if (result_message.indexOf("basics of computer") != -1){
            speak("you choose basics of computer.");
            Intent intent = new Intent(Main2Activity.this,
                    com.zaidimarvels.voiceapp.learning_interface.class);
            startActivity(intent);
        } else if (result_message.indexOf("go back") != -1){
            Intent intent = new Intent(Main2Activity.this,
                    com.zaidimarvels.voiceapp.MainActivity.class);
            startActivity(intent);
        } else if (result_message.indexOf("list courses") != -1) {
            speak(courses);
        } else if (result_message.indexOf("repeat") != -1) {
            speak(commands);
        }

    }

    private void initializeTextToSpeech() {
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (tts.getEngines().size() == 0 ){
                    Toast.makeText(Main2Activity.this, getString(R.string.tts_no_engines),Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    tts.setLanguage(Locale.US);
                    speak(commands);
//                    while (tts.isSpeaking());
//                    triggerFab();
                }
            }
        });
    }

    private void speak(String message) {
        if(Build.VERSION.SDK_INT >= 21){
            tts.speak(message,TextToSpeech.QUEUE_FLUSH,null,null);
        } else {
            tts.speak(message, TextToSpeech.QUEUE_FLUSH,null);
        }
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

    @Override
    protected void onPause() {
        super.onPause();
        tts.shutdown();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeSpeechRecognizer();
        initializeTextToSpeech();
    }

    private void triggerFab() {
        if (ContextCompat.checkSelfPermission(Main2Activity.this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Main2Activity.this,
                    Manifest.permission.RECORD_AUDIO)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(Main2Activity.this,
                        new String[]{Manifest.permission.RECORD_AUDIO},MY_PERMISSIONS_REQUEST_RECORD_AUDIO);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
            speechRecog.startListening(intent);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.Example) {
            startActivity(new Intent(Main2Activity.this, learning_interface.class));
            Toast.makeText(this, "clicked", Toast.LENGTH_LONG).show();
        } else if (view.getId() == R.id.fab) {
            if (ContextCompat.checkSelfPermission(Main2Activity.this,
                    Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(Main2Activity.this,
                        Manifest.permission.RECORD_AUDIO)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(Main2Activity.this,
                            new String[]{Manifest.permission.RECORD_AUDIO},MY_PERMISSIONS_REQUEST_RECORD_AUDIO);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                // Permission has already been granted
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
                if (tts.isSpeaking()) {
                    tts.stop();
                    speechRecog.startListening(intent);
                } else {
                    speechRecog.startListening(intent);

                }

            }
        }
    }
}
