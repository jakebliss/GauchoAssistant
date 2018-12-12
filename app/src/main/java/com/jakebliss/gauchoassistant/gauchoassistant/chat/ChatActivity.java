package com.jakebliss.gauchoassistant.gauchoassistant.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import android.os.AsyncTask;
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

        import java.util.ArrayList;
        import java.util.Locale;
import android.os.Build;
import android.os.Bundle;
import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import java.util.Calendar;

import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.jakebliss.gauchoassistant.gauchoassistant.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import io.reactivex.disposables.CompositeDisposable;

public class ChatActivity extends AppCompatActivity implements AIListener {
    private Toolbar mToolbar;
    private String quarter_calendar = new String();
    private String menu = new String();
    private String quarter_calendar_data = new String("quarter_calendar");
    private String menu_data = new String("menu");
    private TextView txvResult;
    private TextToSpeech mTTS;
    private Toolbar myToolbar;
    AIService aiService;


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ChatActivity.class);
        context.startActivity(intent);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
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

        final CompositeDisposable compositeDisposable = new CompositeDisposable();
        //hide action bar
        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#232323"));
        }
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = "Current Date : " + mdformat.format(calendar.getTime());
        strDate += "T00:00:00-08:00";
        Log.d("api", strDate);
        //Make REST API calls to the UCSB dev apis
        getResponseData("https://api.ucsb.edu/academics/quartercalendar/v1/quarters/current", quarter_calendar_data);
        getResponseData("https://api.ucsb.edu/dining/menu/v1/2018-12-09T00%3A00%3A00-08%3A00/de-la-guerra/brunch", menu_data);

        Log.d("api", quarter_calendar);
        bindViews();
        init();
    }
    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                101);
    }
    public void nlpSpeech(View v) {
        aiService.startListening();
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

    private String getResponseData(final String endpoint, final String data_type){
        String data = new String("null");
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                URL githubEndpoint = null;
                try {
                    githubEndpoint = new URL(endpoint);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpsURLConnection myConnection =
                            (HttpsURLConnection) githubEndpoint.openConnection();
                    myConnection.setRequestProperty("User-Agent", "gaucho-assistant");
                    myConnection.setRequestProperty("accept",
                            "application/json");
                    myConnection.setRequestProperty("ucsb-api-version",
                            "1.0");
                    myConnection.setRequestProperty("ucsb-api-key",
                            "8IQgXsvmA6OuIjuGkVT3t9oYf2MDZbbS");
                    if (myConnection.getResponseCode() == 200) {
                        Log.d("api", "gotit");
                        InputStream responseBody = myConnection.getInputStream();
                        InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                        JsonReader jsonReader = new JsonReader(responseBodyReader);
                        List<Object> obj = new ArrayList<Object>();

                        if(data_type.equals("menu")){
                            jsonReader.beginArray();
                            while(jsonReader.hasNext()){
                                jsonReader.beginObject(); // Start processing the JSON object
                                while (jsonReader.hasNext()) {
                                    String key = jsonReader.nextName(); // Fetch the next key
                                    if (jsonReader.peek() != JsonToken.NULL) {
                                            menu_data += "," + key + ":" + jsonReader.nextString();
                                            Log.d("api", menu_data);
                                    } else {
                                        jsonReader.nextNull(); // note on this
                                    }
                                }
                                jsonReader.endObject();
                            }
                            jsonReader.endArray();
                        }
                        else {
                            jsonReader.beginObject(); // Start processing the JSON object
                            while (jsonReader.hasNext()) { // Loop through all keys
                                String key = jsonReader.nextName(); // Fetch the next key
                                // Check if desired key
                                if (jsonReader.peek() != JsonToken.NULL) {
                                        quarter_calendar += "," + key + ":" + jsonReader.nextString();
                                        Log.d("api", quarter_calendar);
                                } else {
                                    jsonReader.nextNull(); // note on this
                                }
                            }
                        }
                        jsonReader.close();
                        myConnection.disconnect();
                    } else {
                        // Error handling code goes here
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        return data;
    }


    private void bindViews() {
    }

    private void init() {
        // set the register screen fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_content_chat,
                ChatFragment.newInstance(),
                ChatFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }
}

