package com.example.lenovo.salestest;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class IntroActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        frameLayout = (FrameLayout)findViewById(R.id.layout1);
        animationDrawable =(AnimationDrawable)frameLayout.getBackground();
        animationDrawable.setEnterFadeDuration(4500);
        animationDrawable.setExitFadeDuration(4500);
        animationDrawable.start();
        transitionTimer();

    }

    public void transition(){
        Intent intent = new Intent(this, startActivity.class);
        startActivity(intent);
    }

    public void transitionTimer(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                transition();
            }
        }, 8000);
    }
}
