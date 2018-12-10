package com.jakebliss.gauchoassistant.gauchoassistant;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class WidgetActivity extends AppCompatActivity {
    private String saved_settings = new String();
    private Boolean settings = true;
    private Boolean classes = true;
    private Boolean menus = true;
    private Boolean passtimes = true;
    private int fontsize = 1;
    private Boolean divider1 = true;
    private Boolean darkfont = true;
    private Boolean colors = true;
    private Boolean background = true;
    private Boolean dark_background = true;
    private Boolean darkfont1 = true;
    private String init_data = new String("settings:1,classes:1,menus:1,passtimes:1,divider1:1,darkfont:0,fontsize:1,colors:1,background:1,dark_background:1,darkfont1:0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Restore saved instance
        //writeSettings(init_data, getApplicationContext());

        if(savedInstanceState != null) {
            settings = savedInstanceState.getBoolean("settings");
            classes = savedInstanceState.getBoolean("classes");
            menus = savedInstanceState.getBoolean("menus");
            passtimes = savedInstanceState.getBoolean("passtimes");
            divider1 = savedInstanceState.getBoolean("divider1");
            darkfont = savedInstanceState.getBoolean("darkfont");
            fontsize = savedInstanceState.getInt("fontsize");
            Log.d("settings", Boolean.toString(settings));
        }

        //hide action bar
        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#232323"));
        }

        //Read in persistent data


        setContentView(R.layout.widget_settings);
        Typeface tf = Typeface.createFromAsset(getAssets(),"product_sans_regular.ttf");
        TextView textfield = findViewById(R.id.general);
        textfield.setTypeface(tf,Typeface.NORMAL);
        textfield = findViewById(R.id.switchClasses);
        textfield.setTypeface(tf,Typeface.NORMAL);
        textfield = findViewById(R.id.switchMainBg);
        textfield.setTypeface(tf,Typeface.NORMAL);
        textfield = findViewById(R.id.switchBackgrounds);
        textfield.setTypeface(tf,Typeface.NORMAL);
        textfield = findViewById(R.id.switchPasstimes);
        textfield.setTypeface(tf,Typeface.NORMAL);
        textfield = findViewById(R.id.switchMenus);
        textfield.setTypeface(tf,Typeface.NORMAL);
        textfield = findViewById(R.id.switchSettings);
        textfield.setTypeface(tf,Typeface.NORMAL);
        textfield = findViewById(R.id.switchFont);
        textfield.setTypeface(tf,Typeface.NORMAL);
        textfield = findViewById(R.id.switchMainBgColor);
        textfield.setTypeface(tf,Typeface.NORMAL);



        final RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.widget_layout);
        ImageView settingsIcon = (ImageView) findViewById(R.id.image4);
        final Context context = this;

        //Read in current widget settings
        saved_settings = readSettings(context);
        if(saved_settings.length() <= 0){
            writeSettings(init_data, getApplicationContext());
        }
        Log.d("settings", saved_settings);
        Log.d("settings", init_data);


        //Settings
        if(saved_settings.length() > 0) {
            char s = saved_settings.charAt(saved_settings.indexOf("settings:") + 9);
            Switch settingsSwitch = findViewById(R.id.switchSettings);
            Log.d("settings", String.valueOf(s));
            if(s == '1'){
                settings = true;
            }else{
                settings = false;
            }
            settingsSwitch.setChecked(settings);
        }

        //Classes
        if(saved_settings.length() > 0) {
            char s = saved_settings.charAt(saved_settings.indexOf("classes:") + 8);
            Switch settingsSwitch = findViewById(R.id.switchClasses);
            Log.d("settings", String.valueOf(s));
            if(s == '1'){
                classes = true;
            }else{
                classes = false;
            }
            settingsSwitch.setChecked(classes);
        }

        //Menus
        if(saved_settings.length() > 0) {
            char s = saved_settings.charAt(saved_settings.indexOf("menus:") + 6);
            Switch settingsSwitch = findViewById(R.id.switchMenus);
            Log.d("settings", String.valueOf(s));
            if(s == '1'){
                menus = true;
            }else{
                menus = false;
            }
            settingsSwitch.setChecked(menus);
        }

        //Passtimes
        if(saved_settings.length() > 0) {
            char s = saved_settings.charAt(saved_settings.indexOf("passtimes:") + 10);
            Switch settingsSwitch = findViewById(R.id.switchPasstimes);
            Log.d("settings", String.valueOf(s));
            if(s == '1'){
                passtimes = true;
            }else{
                passtimes = false;
            }
            settingsSwitch.setChecked(passtimes);
        }
        //Background
        if(saved_settings.length() > 0) {
            char s = saved_settings.charAt(saved_settings.indexOf("background:") + 11);
            Switch settingsSwitch = findViewById(R.id.switchMainBg);
            Log.d("settings", String.valueOf(s));
            if(s == '1'){
                background = true;
            }else{
                background = false;
            }
            settingsSwitch.setChecked(background);
        }
        //Dark Background
        if(saved_settings.length() > 0) {
            char s = saved_settings.charAt(saved_settings.indexOf("dark_background:") + 16);
            Switch settingsSwitch = findViewById(R.id.switchMainBgColor);
            Log.d("settings", String.valueOf(s));
            if(s == '1'){
                dark_background = true;
            }else{
                dark_background = false;
            }
            settingsSwitch.setChecked(dark_background);
        }

        //Background colors
        if(saved_settings.length() > 0) {
            char s = saved_settings.charAt(saved_settings.indexOf("colors:") + 7);
            Switch settingsSwitch = findViewById(R.id.switchBackgrounds);
            Log.d("settings", String.valueOf(s));
            if(s == '1'){
                colors = true;
            }else{
                colors = false;
            }
            settingsSwitch.setChecked(colors);
        }

        //Dark fonts
        if(saved_settings.length() > 0) {
            char s = saved_settings.charAt(saved_settings.indexOf("darkfont:") + 9);
            Switch settingsSwitch = findViewById(R.id.switchFont);
            Log.d("settings", String.valueOf(s));
            if(s == '1'){
                darkfont = true;
            }else{
                darkfont = false;
            }
            settingsSwitch.setChecked(darkfont);
        }
        //Dark fonts header
        if(saved_settings.length() > 0) {
            char s = saved_settings.charAt(saved_settings.indexOf("darkfont1:") + 10);
            Switch settingsSwitch = findViewById(R.id.switchFont2);
            Log.d("settings", String.valueOf(s));
            if(s == '1'){
                darkfont1 = true;
            }else{
                darkfont1 = false;
            }
            settingsSwitch.setChecked(darkfont1);
        }


        //Settings Toggle
        final Switch settingsToggle = (Switch) findViewById(R.id.switchSettings);
        settingsToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.d("Log", "Checked");
                    remoteViews.setViewVisibility(R.id.image4, View.VISIBLE);
                    settings = true;
                    String new_data = saved_settings.replace("settings:0", "settings:1");
                    saved_settings = new_data;
                    writeSettings(new_data, getApplicationContext());

                } else {
                    remoteViews.setViewVisibility(R.id.image4, View.GONE);
                    settings = false;
                    String new_data = saved_settings.replace("settings:1", "settings:0");
                    saved_settings = new_data;
                    writeSettings(new_data, getApplicationContext());

                }

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                ComponentName thisWidget = new ComponentName(context, ExampleAppWidgetProvider.class);
                appWidgetManager.updateAppWidget(thisWidget, remoteViews);

            }
        });

        //Classes Toggle
        final Switch classesToggle = (Switch) findViewById(R.id.switchClasses);
        classesToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.d("Log", "Checked");
                    remoteViews.setViewVisibility(R.id.mainclass, View.VISIBLE);
                    remoteViews.setViewVisibility(R.id.mainclasslocation, View.VISIBLE);
                    remoteViews.setViewVisibility(R.id.class2, View.VISIBLE);
                    remoteViews.setViewVisibility(R.id.class3, View.VISIBLE);
                    remoteViews.setViewVisibility(R.id.class4, View.VISIBLE);
                    remoteViews.setViewVisibility(R.id.divider, View.VISIBLE);
                    classes = true;
                    String new_data = saved_settings.replace("classes:0", "classes:1");
                    saved_settings = new_data;
                    writeSettings(new_data, getApplicationContext());


                } else {
                    remoteViews.setViewVisibility(R.id.mainclass, View.GONE);
                    remoteViews.setViewVisibility(R.id.mainclasslocation, View.GONE);
                    remoteViews.setViewVisibility(R.id.class2, View.GONE);
                    remoteViews.setViewVisibility(R.id.class3, View.GONE);
                    remoteViews.setViewVisibility(R.id.class4, View.GONE);
                    remoteViews.setViewVisibility(R.id.divider, View.GONE);
                    classes = false;
                    String new_data = saved_settings.replace("classes:1", "classes:0");
                    saved_settings = new_data;
                    writeSettings(new_data, getApplicationContext());

                }

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                ComponentName thisWidget = new ComponentName(context, ExampleAppWidgetProvider.class);
                appWidgetManager.updateAppWidget(thisWidget, remoteViews);

            }
        });

        //Menus Toggle
        final Switch menusToggle = (Switch) findViewById(R.id.switchMenus);
        menusToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.d("Log", "Checked");
                    remoteViews.setViewVisibility(R.id.class5, View.VISIBLE);
                    remoteViews.setViewVisibility(R.id.class6, View.VISIBLE);


                    menus = true;
                    String new_data = saved_settings.replace("menus:0", "menus:1");
                    saved_settings = new_data;
                    writeSettings(new_data, getApplicationContext());

                } else {
                    remoteViews.setViewVisibility(R.id.class5, View.GONE);
                    remoteViews.setViewVisibility(R.id.class6, View.GONE);

                    menus = false;
                    String new_data = saved_settings.replace("menus:1", "menus:0");
                    saved_settings = new_data;
                    writeSettings(new_data, getApplicationContext());

                }

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                ComponentName thisWidget = new ComponentName(context, ExampleAppWidgetProvider.class);
                appWidgetManager.updateAppWidget(thisWidget, remoteViews);

            }
        });

        //Passtimes Toggle
        final Switch passtimesToggle = (Switch) findViewById(R.id.switchPasstimes);
        passtimesToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.d("Log", "Checked");
                    remoteViews.setViewVisibility(R.id.pass1, View.VISIBLE);
                    remoteViews.setViewVisibility(R.id.pass2, View.VISIBLE);
                    remoteViews.setViewVisibility(R.id.pass3, View.VISIBLE);
                    passtimes = true;
                    String new_data = saved_settings.replace("passtimes:0", "passtimes:1");
                    saved_settings = new_data;
                    writeSettings(new_data, getApplicationContext());

                } else {
                    remoteViews.setViewVisibility(R.id.pass1, View.GONE);
                    remoteViews.setViewVisibility(R.id.pass2, View.GONE);
                    remoteViews.setViewVisibility(R.id.pass3, View.GONE);

                    passtimes = false;
                    String new_data = saved_settings.replace("passtimes:1", "passtimes:0");
                    saved_settings = new_data;
                    writeSettings(new_data, getApplicationContext());

                }

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                ComponentName thisWidget = new ComponentName(context, ExampleAppWidgetProvider.class);
                appWidgetManager.updateAppWidget(thisWidget, remoteViews);

            }
        });
        //Background toggle
        final Switch bgToggle = (Switch) findViewById(R.id.switchMainBg);
        bgToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.d("Log", "Checked");
                    int color = 0;
                    if(colors){
                        remoteViews.setViewVisibility(R.id.bgcolor1, View.VISIBLE);
                        remoteViews.setViewVisibility(R.id.bgcolor2, View.GONE);
                    }else{
                        remoteViews.setViewVisibility(R.id.bgcolor2, View.VISIBLE);
                        remoteViews.setViewVisibility(R.id.bgcolor1, View.GONE);
                    }
                    background = true;

                    String new_data = saved_settings.replace("background:0", "background:1");
                    saved_settings = new_data;
                    writeSettings(new_data, getApplicationContext());

                } else {
                    remoteViews.setViewVisibility(R.id.bgcolor1, View.GONE);
                    remoteViews.setViewVisibility(R.id.bgcolor2, View.GONE);
                    String new_data = saved_settings.replace("background:1", "background:0");
                    saved_settings = new_data;
                    writeSettings(new_data, getApplicationContext());

                }

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                ComponentName thisWidget = new ComponentName(context, ExampleAppWidgetProvider.class);
                appWidgetManager.updateAppWidget(thisWidget, remoteViews);

            }
        });

        //Background Color toggle
        final Switch bgcToggle = (Switch) findViewById(R.id.switchMainBgColor);
        bgcToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    remoteViews.setViewVisibility(R.id.bgcolor1, View.VISIBLE);
                    remoteViews.setViewVisibility(R.id.bgcolor2, View.GONE);
                    dark_background = true;
                    String new_data = saved_settings.replace("dark_background:0", "dark_background:1");
                    saved_settings = new_data;
                    writeSettings(new_data, getApplicationContext());

                } else {
                    remoteViews.setViewVisibility(R.id.bgcolor1, View.GONE);
                    remoteViews.setViewVisibility(R.id.bgcolor2, View.VISIBLE);
                    String new_data = saved_settings.replace("dark_background:1", "dark_background:0");
                    saved_settings = new_data;
                    writeSettings(new_data, getApplicationContext());

                }
                if(!background){
                    remoteViews.setViewVisibility(R.id.bgcolor1, View.GONE);
                    remoteViews.setViewVisibility(R.id.bgcolor2, View.GONE);
                }

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                ComponentName thisWidget = new ComponentName(context, ExampleAppWidgetProvider.class);
                appWidgetManager.updateAppWidget(thisWidget, remoteViews);

            }
        });

        //Background Colors toggle
        final Switch bgcsToggle = (Switch) findViewById(R.id.switchBackgrounds);
        bgcsToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    remoteViews.setInt(R.id.class2, "setBackgroundColor", Color.parseColor("#00000000"));
                    remoteViews.setInt(R.id.class3, "setBackgroundColor", Color.parseColor("#00000000"));
                    remoteViews.setInt(R.id.class4, "setBackgroundColor", Color.parseColor("#00000000"));
                    remoteViews.setInt(R.id.class5, "setBackgroundColor", Color.parseColor("#00000000"));
                    remoteViews.setInt(R.id.class6, "setBackgroundColor", Color.parseColor("#00000000"));
                    remoteViews.setInt(R.id.pass1, "setBackgroundColor", Color.parseColor("#00000000"));
                    remoteViews.setInt(R.id.pass2, "setBackgroundColor", Color.parseColor("#00000000"));
                    remoteViews.setInt(R.id.pass3, "setBackgroundColor", Color.parseColor("#00000000"));



                    String new_data = saved_settings.replace("colors:0", "colors:1");
                    saved_settings = new_data;
                    writeSettings(new_data, getApplicationContext());

                } else {
                    remoteViews.setInt(R.id.class2, "setBackgroundColor", Color.parseColor("#d32f2f"));
                    remoteViews.setInt(R.id.class3, "setBackgroundColor", Color.parseColor("#0288d1"));
                    remoteViews.setInt(R.id.class4, "setBackgroundColor", Color.parseColor("#388e3c"));
                    remoteViews.setInt(R.id.class5, "setBackgroundColor", Color.parseColor("#f57c00"));
                    remoteViews.setInt(R.id.class6, "setBackgroundColor", Color.parseColor("#f57c00"));
                    remoteViews.setInt(R.id.pass1, "setBackgroundColor", Color.parseColor("#673ab7"));
                    remoteViews.setInt(R.id.pass2, "setBackgroundColor", Color.parseColor("#673ab7"));
                    remoteViews.setInt(R.id.pass3, "setBackgroundColor", Color.parseColor("#673ab7"));



                    String new_data = saved_settings.replace("colors:1", "colors:0");
                    saved_settings = new_data;
                    writeSettings(new_data, getApplicationContext());

                }
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                ComponentName thisWidget = new ComponentName(context, ExampleAppWidgetProvider.class);
                appWidgetManager.updateAppWidget(thisWidget, remoteViews);

            }
        });

        //Font toggle
        final Switch darkfontToggle = (Switch) findViewById(R.id.switchFont);
        darkfontToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    remoteViews.setTextColor(R.id.class2, Color.BLACK);
                    remoteViews.setTextColor(R.id.class3, Color.BLACK);
                    remoteViews.setTextColor(R.id.class4, Color.BLACK);
                    remoteViews.setTextColor(R.id.class5, Color.BLACK);
                    remoteViews.setTextColor(R.id.class6, Color.BLACK);
                    remoteViews.setTextColor(R.id.pass1, Color.BLACK);
                    remoteViews.setTextColor(R.id.pass2, Color.BLACK);
                    remoteViews.setTextColor(R.id.pass3, Color.BLACK);
                    darkfont = true;
                    String new_data = saved_settings.replace("darkfont:0", "darkfont:1");
                    saved_settings = new_data;
                    writeSettings(new_data, getApplicationContext());

                } else {
                    remoteViews.setTextColor(R.id.class2, Color.WHITE);
                    remoteViews.setTextColor(R.id.class3, Color.WHITE);
                    remoteViews.setTextColor(R.id.class4, Color.WHITE);
                    remoteViews.setTextColor(R.id.class5, Color.WHITE);
                    remoteViews.setTextColor(R.id.class6, Color.WHITE);
                    remoteViews.setTextColor(R.id.pass1, Color.WHITE);
                    remoteViews.setTextColor(R.id.pass2, Color.WHITE);
                    remoteViews.setTextColor(R.id.pass3, Color.WHITE);
                    String new_data = saved_settings.replace("darkfont:1", "darkfont:0");
                    saved_settings = new_data;
                    writeSettings(new_data, getApplicationContext());

                }

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                ComponentName thisWidget = new ComponentName(context, ExampleAppWidgetProvider.class);
                appWidgetManager.updateAppWidget(thisWidget, remoteViews);

            }
        });

        //Font toggle
        final Switch darkfont1Toggle = (Switch) findViewById(R.id.switchFont2);
        darkfont1Toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    remoteViews.setTextColor(R.id.mainclass, Color.BLACK);
                    remoteViews.setTextColor(R.id.mainclasslocation, Color.BLACK);
                    Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.ic_settings_black_24dp);
                    remoteViews.setInt(R.id.image4, "setImageResource", R.drawable.ic_settings_black_24dp);
                    darkfont1 = true;
                    String new_data = saved_settings.replace("darkfont1:0", "darkfont1:1");
                    saved_settings = new_data;
                    writeSettings(new_data, getApplicationContext());

                } else {
                    darkfont1 = false;
                    remoteViews.setTextColor(R.id.mainclass, Color.WHITE);
                    remoteViews.setTextColor(R.id.mainclasslocation, Color.WHITE);
                    Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                            R.drawable.ic_settings_white_24dp);
                    remoteViews.setInt(R.id.image4, "setImageResource", R.drawable.ic_settings_white_24dp);
                    String new_data = saved_settings.replace("darkfont1:1", "darkfont1:0");
                    saved_settings = new_data;
                    writeSettings(new_data, getApplicationContext());

                }

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                ComponentName thisWidget = new ComponentName(context, ExampleAppWidgetProvider.class);
                appWidgetManager.updateAppWidget(thisWidget, remoteViews);

            }
        });
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putBoolean("settings", settings);
        savedInstanceState.putBoolean("classes", classes);
        savedInstanceState.putBoolean("menus", menus);
        savedInstanceState.putBoolean("passtimes", passtimes);
        savedInstanceState.putBoolean("divider1", divider1);
        savedInstanceState.putBoolean("darkfont", darkfont);
        savedInstanceState.putInt("fontsize", fontsize);

    }


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        settings = savedInstanceState.getBoolean("settings");
        classes = savedInstanceState.getBoolean("classes");
        menus = savedInstanceState.getBoolean("menus");
        passtimes = savedInstanceState.getBoolean("passtimes");
        divider1 = savedInstanceState.getBoolean("divider1");
        darkfont = savedInstanceState.getBoolean("darkfont");
        fontsize = savedInstanceState.getInt("fontsize");
        Log.d("settings", Boolean.toString(settings));
    }

    private void writeSettings(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("widget_settings.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String readSettings(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("widget_settings.txt");

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
}


/*
android:shadowColor="#7F000000"
                    android:shadowDx="3"
                    android:shadowDy="3"
                    android:shadowRadius="0.01"
 */