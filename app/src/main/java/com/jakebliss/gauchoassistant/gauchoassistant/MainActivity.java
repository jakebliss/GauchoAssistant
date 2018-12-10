package com.jakebliss.gauchoassistant.gauchoassistant;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import java.util.ArrayList;
import java.util.Locale;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class MainActivity extends AppCompatActivity implements AIListener {
    private TextView txvResult;
    private TextToSpeech mTTS;
    private Toolbar myToolbar;
    AIService aiService;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txvResult = (TextView) findViewById(R.id.txvResult);
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Locale.ENGLISH);

                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    } else {

                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO);

        if (permission != PackageManager.PERMISSION_GRANTED) {

            makeRequest();
        }

        final AIConfiguration config = new AIConfiguration("5a85cfe047924337b807adbec5ffbe4e",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(this, config);
        aiService.setListener(this);

    }
    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                101);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 101: {

                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {


                } else {

                }
                return;
            }
        }
    }

    public void nlpSpeech(View v) {
        aiService.startListening();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.toolbar,menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int res_id = item.getItemId();
        if(res_id == R.id.action_settings)
        {
            Toast.makeText(this, "You clicked settings!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }



        return true;
    }

    private void speak(String text) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String assistantSpeed = sharedPreferences.getString("assistant_speed","Normal");

        float pitch = 0.5f;
        float speed = 0.5f;


        if(assistantSpeed.equals("Slow"))
        {
             pitch = 0.5f;
             speed = 0.1f;
        }else if (assistantSpeed.equals("Normal"))
        {
            pitch = 0.5f;
            speed = 0.5f;
        }else if (assistantSpeed.equals("Fast"))
        {
            pitch = 0.5f;
            speed = 1f;
        }


        mTTS.setPitch(pitch);
        mTTS.setSpeechRate(speed);

        mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
    public void getSpeechInput(View view) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String nickname = sharedPreferences.getString("nickname","");

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txvResult.setText(result.get(0));


                    speak(result.get(0));

                    if(result.get(0).contains("what's my schedule") || result.get(0).contains("what is my schedule") ||  (result.get(0).contains("get") &&  result.get(0).contains("schedule")))
                    {
                        speak("retrieving schedule information for you " + nickname);
                        Toast.makeText(this, "retrieving schedule information", Toast.LENGTH_SHORT).show();

                    }
                    if(result.get(0).contains("where's my next class") || result.get(0).contains("where is my next class")|| (result.get(0).contains("get") &&  result.get(0).contains("next class")))
                    {
                        speak("retrieving class location");
                        Toast.makeText(this, "retrieving class location for you, " + nickname, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }

    }

    @Override
    public void onResult(AIResponse result) {
            Log.v("NLP RESULT: ", result.toString());
            Result result1 = result.getResult();
            txvResult.setText("Query " + result1.getResolvedQuery()+" action: " + result1.getAction() + " response: " + result1.getFulfillment().getSpeech() + " params: " + result1.getParameters());
            speak(result1.getFulfillment().getSpeech());

            if(result1.getAction().equals("input.schedule"))
            {
                Toast.makeText(this, "input.schedule " + result1.getStringParameter("date"), Toast.LENGTH_SHORT).show();

            }else if(result1.getAction().equals("input.nextclasslocation"))
            {
                Toast.makeText(this, "input.nextclasslocation", Toast.LENGTH_SHORT).show();

            }else if(result1.getAction().equals("input.passtime"))
            {
                Toast.makeText(this, "input.passtime", Toast.LENGTH_SHORT).show();

            }



    }

    @Override
    public void onError(AIError error) {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }
}
