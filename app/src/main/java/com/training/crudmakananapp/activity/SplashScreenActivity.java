package com.training.crudmakananapp.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.training.crudmakananapp.R;
import com.training.crudmakananapp.helper.SessionManager;

public class SplashScreenActivity extends AppCompatActivity {

    private SessionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                manager = new SessionManager(SplashScreenActivity.this);
                manager.checkLogin();
                finish();
            }
        }, 3000);

    }
}
