package com.example.mlabsystem2.dialerfinal;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Main4Activity extends AppCompatActivity {
    private static  int SPLASH_TIME_OUT=2500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }


        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent intent=new Intent(Main4Activity.this,Main3Activity.class);
                startActivity(intent);
                finish();

            }
        },SPLASH_TIME_OUT);
    }



}
