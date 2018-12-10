package com.jakebliss.gauchoassistant.gauchoassistant.chat;


import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
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
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakebliss.gauchoassistant.gauchoassistant.LoginActivity;
import com.jakebliss.gauchoassistant.gauchoassistant.R;
import com.jakebliss.gauchoassistant.gauchoassistant.WidgetActivity;

import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import static android.app.Activity.RESULT_OK;


public class ChatFragment extends Fragment implements View.OnClickListener, AIListener{
    private RecyclerView mRecyclerViewChat;
    private EditText mETxtMessage;
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    private ChatRecyclerAdapter mChatRecyclerAdapter;
    private static final int RC_PHOTO_PICKER = 2;

    private Button mSendButton;
    private String schedule = new String("");

    private TextView txvResult;
    private TextToSpeech mTTS;
    private Toolbar myToolbar;
    private String quarter_calendar = new String();
    private String menu = new String();
    AIService aiService;
    private Button mSendButtonCancel;
    private String quarter_calendar_data = new String("quarter_calendar");
    private String menu_data = new String("menu");
    private List<String> menu_items;
    private List<String> quarter_info;
    private ImageView mSettingsButton;
    private ImageView mVoiceButton;
    private Context context;
    String Class2 = new String();
    String Class3 = new String();
    String Class4 = new String();
    String Loc2 = new String();
    String Loc3 = new String();
    String ClassHeading = new String();
    String Loc4 = new String();



    public static ChatFragment newInstance() {
        Bundle args = new Bundle();
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);

        return fragment;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_chat, container, false);
        bindViews(fragmentView);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = mdformat.format(calendar.getTime());
        strDate = "https://api.ucsb.edu/dining/menu/v1/"+strDate+"T00%3A00%3A00-08%3A00/carrillo/dinner";
        Log.d("pap", strDate);
        getResponseData("https://api.ucsb.edu/academics/quartercalendar/v1/quarters/current", quarter_calendar_data);
        getResponseData(strDate, menu_data);
        schedule = readClasses(getContext());
        String class1 = new String();
        String class2 = new String();
        String class3 = new String();
        String class4= new String();
        int first = schedule.indexOf("****");
        int second = schedule.indexOf("****", first + 1);
        int third = schedule.indexOf("****", second + 1);
        int fourth = schedule.indexOf("****", third + 1);
        Log.d("omer", schedule);
        if(schedule.contains("Title0")){
            class1 = schedule.substring(schedule.indexOf("Title0"), schedule.indexOf("****0"));
            Log.d("omer", class1);

        }
        if(schedule.contains("Title1")){
            class2 = schedule.substring(schedule.indexOf("Title1"), schedule.indexOf("****1"));
            Log.d("omer", class2);
        }
        if(schedule.contains("Title2")){
            class3 = schedule.substring(schedule.indexOf("Title2"), schedule.indexOf("****2"));
            Log.d("omer", class3);
        }
        if(schedule.contains("Title3")){
            class4 = schedule.substring(schedule.indexOf("Title3"), schedule.indexOf("****3"));
            Log.d("omer", class4);
        }

        ClassHeading = new String(class1.substring(6,16) + " at " + class1.substring(class1.indexOf("Time") + 4, class1.indexOf("Time") + 11));
        Log.d("omer", ClassHeading);
        String ClassLocation = new String(class1.substring(class1.indexOf("Location") + 8));
        ClassLocation = ClassLocation.substring(0, ClassLocation.length() -1);

        Log.d("omer", ClassLocation);
        if(class2.length() > 0){
            Class2 = new String(class2.substring(6, 16));
            Loc2 = new String( class2.substring(class2.indexOf("Time") + 4, class2.indexOf("Time") + 11));
            Log.d("omer", Class2 + Loc2);

        }
        if(class3.length() > 0){
            Class3 = new String(class3.substring(6, 16));
            Loc3 = new String( class3.substring(class3.indexOf("Time") + 4, class3.indexOf("Time") + 11));
            Log.d("omer", Class3 + Loc3);
        }
        if(class4.length() > 0){
            Class4 = new String(class4.substring(6, 16));
            Loc4 = new String( class4.substring(class4.indexOf("Time") + 4, class4.indexOf("Time") + 11));
            Log.d("omer", Class4 + Loc4);
        }
        mTTS = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
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
        int permission = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.RECORD_AUDIO);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest();
        }
        final AIConfiguration config = new AIConfiguration("5a85cfe047924337b807adbec5ffbe4e",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
        //aiService = AIService.getService(getContext(), config);
        //aiService.setListener(this);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"product_sans_regular.ttf");
        TextView textfield = fragmentView.findViewById(R.id.textViewG);
        textfield.setTypeface(tf,Typeface.NORMAL);
        return fragmentView;
    }
    protected void makeRequest() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.RECORD_AUDIO},
                101);
    }
    private void bindViews(View view) {
        mRecyclerViewChat = (RecyclerView) view.findViewById(R.id.recycler_view_chat);
        mETxtMessage = (EditText) view.findViewById(R.id.edit_text_message);
        mSendButton = (Button) view.findViewById(R.id.sendButton);
        mSendButtonCancel = (Button) view.findViewById(R.id.sendButtonCancel);
        mSettingsButton = (ImageView) view.findViewById(R.id.image2);
        mVoiceButton = (ImageView) view.findViewById(R.id.image3);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
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
    public void nlpSpeech() {
        aiService.startListening();
    }
    private void speak(String text) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
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
    public void getSpeechInput() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(getContext(), "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getContext(), "input.schedule " + result1.getStringParameter("date"), Toast.LENGTH_SHORT).show();
        }else if(result1.getAction().equals("input.nextclasslocation"))
        {
            Toast.makeText(getContext(), "input.nextclasslocation", Toast.LENGTH_SHORT).show();
        }else if(result1.getAction().equals("input.passtime"))
        {
            Toast.makeText(getContext(), "input.passtime", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String nickname = sharedPreferences.getString("nickname","");
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //txvResult.setText(result.get(0));


                    //speak(result.get(0));

                    if(result.get(0).contains("schedule") || result.get(0).contains("what is my schedule") ||  (result.get(0).contains("classes") &&  result.get(0).contains("class")))
                    {
                        String response = ClassHeading + "." + "Then you have " + Class2 + " at " + Loc2 + "." + Class3 + " at " + Loc3 + ".";
                        speak("Retrieving schedule information for you friend. Your schedule is." + response);
                        Toast.makeText(getContext(), "retrieving schedule information", Toast.LENGTH_SHORT).show();

                    }
                     else if(result.get(0).contains("pass") || result.get(0).contains("where is my next class")|| (result.get(0).contains("get") &&  result.get(0).contains("next class")))
                    {
                        String speak = new String("Pass 1 began " + quarter_info.get(0).substring(11) + ".pass 2 begins on " + quarter_info.get(1).substring(11) + ".pass 3 begins " + quarter_info.get(2).substring(11));
                        speak("Here are the pass times for this quarter friend. " + speak);
                        Toast.makeText(getContext(), "retrieving class location for you, " + nickname, Toast.LENGTH_SHORT).show();
                    }
                     else if(result.get(0).contains("menu") || result.get(0).contains("where is my next class")|| (result.get(0).contains("get") &&  result.get(0).contains("next class")))
                    {
                        String speak = new String(menu_items.toString());

                        speak("Here is the dinner menu at carrillo. " + speak);
                        Toast.makeText(getContext(), "retrieving class location for you, " + nickname, Toast.LENGTH_SHORT).show();
                    }
                    else{
                        speak("I did not catch that, please repeat yourself.");

                    }
                }
                break;
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
    private void init() {
        // Enable Send button when there's text to send

        mETxtMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setVisibility(View.VISIBLE);
                    mSendButton.setEnabled(true);
                    mSendButtonCancel.setVisibility(View.GONE);
                } else {
                    mSendButton.setVisibility(View.GONE);
                    mSendButton.setEnabled(false);
                    mSendButtonCancel.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mETxtMessage.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mChatRecyclerAdapter == null) {
                    mChatRecyclerAdapter = new ChatRecyclerAdapter(new ArrayList<ChatMessage>());
                    mRecyclerViewChat.setAdapter(mChatRecyclerAdapter);
                }
                mChatRecyclerAdapter.add(makeNewChatMessageUser(mETxtMessage.getText().toString()));
                String response = new String("Sorry, I don't know that. Ask me something else.");
                if(mETxtMessage.getText().toString().contains("menu")){
                    response = "Here is the dinner menu at Carrillo today \n" + menu_items.get(0) + '\n' + menu_items.get(1) + '\n' + menu_items.get(2) + '\n' + menu_items.get(3) + '\n' + menu_items.get(4) + '\n' + "Plus much more.";
                }
                else if(mETxtMessage.getText().toString().contains("schedule") || mETxtMessage.getText().toString().contains("class")){
                    response = ClassHeading + "." + "Then you have " + Class2 + " at " + Loc2 + "." + Class3 + " at " + Loc3 + ".";
                    response = "Retrieving schedule information for you friend. Your schedule is." + response;
                }
                else if(mETxtMessage.getText().toString().contains("pass")){
                    response = "Here are the pass times for this quarter \n" + quarter_info.get(0) + '\n' + quarter_info.get(1) + '\n' + quarter_info.get(2);
                }

                mChatRecyclerAdapter.add(makeNewChatMessageAssistant(response));
                mRecyclerViewChat.smoothScrollToPosition(mChatRecyclerAdapter.getItemCount() - 1);
                mETxtMessage.setText("");
            }
        });
        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("settings", "clicked");
                Intent myIntent = new Intent(getContext(), WidgetActivity.class);
                getContext().startActivity(myIntent);
            }
        });
        mVoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("settings", "clicked");
                getSpeechInput();
            }
        });

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
                                        Log.d("oiecjke", menu_data);
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
                                    Log.d("oiecjke", quarter_calendar);
                                } else {
                                    jsonReader.nextNull(); // note on this
                                }
                            }
                        }
                        if(data_type.equals("menu")){
                            menu_items = parseMenu(menu_data);
                        }
                        else{
                            quarter_info = parseQuarter(quarter_calendar);

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
    private List<String> parseMenu(String raw_data){
        List<String> ret = new ArrayList<>();
        List<String> items = Arrays.asList(raw_data.split("\\s*,\\s*"));
        for(int i = 0; i < items.size(); i++){
            if(items.get(i).contains("name")){
                items.set(i, items.get(i).substring(5));
                ret.add(items.get(i));
                Log.d("food", items.get(i));
            }
        }
        return ret;
    }

    private List<String> parseQuarter(String raw_data){
        List<String> items = Arrays.asList(raw_data.split("\\s*,\\s*"));
        List<String> ret = new ArrayList<>();
        for(int i = 0; i < items.size(); i++){
            if(items.get(i).contains("pass1")){
                items.set(i, items.get(i).substring(10));
                String pass1 = new String("Pass 1: " + items.get(i).toString());
                ret.add(pass1);
                Log.d("food", items.get(i));
            }
            if(items.get(i).contains("pass2")){
                items.set(i, items.get(i).substring(10));
                String pass2 = new String("Pass 2: " + items.get(i).toString());
                ret.add(pass2);
                Log.d("food", items.get(i));
            }
            if(items.get(i).contains("pass3")){
                items.set(i, items.get(i).substring(10));
                String pass3 = new String("Pass 3: " + items.get(i).toString());
                ret.add(pass3);
                Log.d("food", items.get(i));
            }
        }
        return ret;
    }
    private ChatMessage makeNewChatMessageUser(String message) {
        ChatMessage chatMessage = new ChatMessage(message, "typeUserChat");
        return chatMessage;
    }
    private ChatMessage makeNewChatMessageAssistant(String message) {
        ChatMessage chatMessage = new ChatMessage(message, "typeAssistantChat");
        return chatMessage;
    }
    private String readClasses(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("my_schedule.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
    @Override
    public void onClick(View v) {
        Log.d("settings", "clicked");
    }

}
