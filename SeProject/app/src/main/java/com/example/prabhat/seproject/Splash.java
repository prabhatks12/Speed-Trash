package com.example.prabhat.seproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;


public class Splash extends AppCompatActivity {

    private static int time=4000;
    protected void onCreate(Bundle instance){

        super.onCreate(instance);
        setContentView(R.layout.splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i= new Intent(Splash.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        },time);

    }
}
