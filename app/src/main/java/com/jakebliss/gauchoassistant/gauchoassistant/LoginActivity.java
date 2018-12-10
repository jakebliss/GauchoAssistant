package com.jakebliss.gauchoassistant.gauchoassistant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_login);
        Typeface tf = Typeface.createFromAsset(getAssets(),"product_sans_regular.ttf");
        TextView textfield = findViewById(R.id.general2);
        textfield.setTypeface(tf,Typeface.NORMAL);
        textfield = findViewById(R.id.editText);
        textfield.setTypeface(tf,Typeface.NORMAL);
        textfield = findViewById(R.id.editText2);
        textfield.setTypeface(tf,Typeface.NORMAL);
        textfield = findViewById(R.id.textView2);
        textfield.setTypeface(tf,Typeface.NORMAL);
        textfield = findViewById(R.id.button);
        textfield.setTypeface(tf,Typeface.NORMAL);
    }
}
