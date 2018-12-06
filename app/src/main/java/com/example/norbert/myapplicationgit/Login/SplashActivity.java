package com.example.norbert.myapplicationgit.Login;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.norbert.myapplicationgit.Login.LoginActivity;
import com.example.norbert.myapplicationgit.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        ImageView splashimageview = findViewById(R.id.splash_imageView);
        ProgressBar spashprogresbar = findViewById(R.id.splash_progressBar);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //https://stackoverflow.com/questions/40956556/how-to-keep-login-state-and-logout-state-in-android-firebase
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        },4*1000); //4 Second Delay
    }
}
