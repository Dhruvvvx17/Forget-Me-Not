package com.example.mlabsystem2.setup_2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

public class q3 extends AppCompatActivity {
    private CheckBox day;
    private CheckBox date;
    private CheckBox season;
    public static int score;
//    private CheckBox year;
//    private CheckBox month;
    private int mMediumAnimationDuration;
    private int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q3);
        day= findViewById(R.id.day);
        date=findViewById(R.id.date);
        season = findViewById(R.id.season);

        day.setVisibility(View.GONE);
        date.setVisibility(View.GONE);
        season.setVisibility(View.GONE);

        mMediumAnimationDuration = 2000;
        day.setAlpha(0f);
        day.setVisibility(View.VISIBLE);
        day.animate()
                .alpha(1f)
                .setDuration(mMediumAnimationDuration);

        date.setAlpha(0f);
        date.setVisibility(View.VISIBLE);
        date.animate()
                .alpha(1f)
                .setDuration(mMediumAnimationDuration);
        season.setAlpha(0f);
        season.setVisibility(View.VISIBLE);
        season.animate()
                .alpha(1f)
                .setDuration(mMediumAnimationDuration);
        Intent intent = getIntent();
        score = intent.getIntExtra("blah",0);
        Context context = getApplicationContext();
        CharSequence text = "Tap here for next question";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 0);
        toast.show();

    }
    public void score( View v){
        CheckBox check = (CheckBox)v;
        if(check.isChecked()){
            score++;
        }
        else if(!check.isChecked()){
            score--;
        }
    }
    public void but(View v) {
        Log.d("bleh","score:"+score);
        Intent i = new Intent(this, q4.class);
        i.putExtra("blah",score);
        startActivity(i);

}

}
