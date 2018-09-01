package com.example.mlabsystem2.setup_2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class img extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img);

    }
    public void but(View v){
        Intent intent=getIntent();
        int score=intent.getIntExtra("blah",0);

        Intent i=new Intent(this,q9.class);
        i.putExtra("blah",score);
        startActivity(i);
    }
}
