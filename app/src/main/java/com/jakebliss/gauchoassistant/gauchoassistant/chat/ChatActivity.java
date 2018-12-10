package com.jakebliss.gauchoassistant.gauchoassistant.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import java.util.Calendar;

import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
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

public class ChatActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private String quarter_calendar = new String();
    private String menu = new String();
    private String quarter_calendar_data = new String("quarter_calendar");
    private String menu_data = new String("menu");

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ChatActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
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

