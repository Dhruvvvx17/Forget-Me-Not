package com.example.mlabsystem2.dialerfinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;import com.MIL.Game.UnityPlayerActivity;


public class Games extends AppCompatActivity {
//CardView game1,game2,game3,game4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);
        Intent intent = new Intent(this, UnityPlayerActivity.class);
        startActivity(intent);

    }
}
