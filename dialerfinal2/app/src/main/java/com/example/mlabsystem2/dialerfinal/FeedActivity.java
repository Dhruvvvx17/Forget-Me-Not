package com.example.mlabsystem2.dialerfinal;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Layout;
import android.view.View;
import android.widget.Button;

public class FeedActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.news:{
                Intent news=new Intent(this,Newsfeed.class);
                startActivity(news);
            }break;
            case R.id.music: {
                Intent intent = new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);            }
            }
        }

    }

