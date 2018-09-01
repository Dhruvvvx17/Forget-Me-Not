package com.example.mlabsystem2.setup_2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class q8 extends AppCompatActivity {
    private CheckBox day;
    private CheckBox date;
    private Button month;
    private int mMediumAnimationDuration;
   public static int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q8);
        day= findViewById(R.id.day);
        date=findViewById(R.id.date);
        month=findViewById(R.id.button);
        day.setAlpha(0f);
        date.setAlpha(0f);
        month.setAlpha(0f);
        mMediumAnimationDuration = getResources().getInteger(
                android.R.integer.config_mediumAnimTime);
        day.setAlpha(0f);

        day.animate()
                .alpha(1f)
                .setDuration(mMediumAnimationDuration);

        date.setAlpha(0f);

        date.animate()
                .alpha(1f)
                .setDuration(mMediumAnimationDuration);

        month.setAlpha(0f);

        month.animate()
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
    public void inst(View v){
        Intent i = new Intent(this, instr.class);
        i.putExtra("blah",score);
        startActivity(i);
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
            Intent i = new Intent(this, q9.class);
            i.putExtra("blah",score);
            startActivity(i);

    }


}
