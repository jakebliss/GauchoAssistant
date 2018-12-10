package com.jakebliss.gauchoassistant.gauchoassistant.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toolbar;

import com.jakebliss.gauchoassistant.gauchoassistant.R;

public class ChatActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ChatActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //hide action bar
        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#232323"));
        }
        bindViews();
        init();
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

