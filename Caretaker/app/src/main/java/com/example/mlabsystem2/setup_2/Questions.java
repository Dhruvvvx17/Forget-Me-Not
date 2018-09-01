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

public class Questions extends AppCompatActivity {
    private View q1;
    private CheckBox day;
    private CheckBox date;
    private CheckBox season;
    private CheckBox year;
    private CheckBox month;
    public static int score;
//    private View q1;
//    private View q2;
//    private View q1;
//    private View q2;
    private int mMediumAnimationDuration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        q1 = findViewById(R.id.q1);
        day= findViewById(R.id.day);
        date=findViewById(R.id.date);
        season = findViewById(R.id.season);
        month= findViewById(R.id.month);
        year=findViewById(R.id.year);
        day.setVisibility(View.GONE);
        date.setVisibility(View.GONE);
        season.setVisibility(View.GONE);
        year.setVisibility(View.GONE);
        month.setVisibility(View.GONE);
        //date.setVisibility(View.GONE);
        mMediumAnimationDuration = 2000;
        score=0;
        day.setAlpha(0f);
        day.setVisibility(View.VISIBLE);
        day.animate()
                .alpha(1f)
                .setDuration(2000);
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
        year.setAlpha(0f);
        year.setVisibility(View.VISIBLE);
        year.animate()
                .alpha(1f)
                .setDuration(mMediumAnimationDuration);
        month.setAlpha(0f);
        month.setVisibility(View.VISIBLE);
        month.animate()
                .alpha(1f)
                .setDuration(mMediumAnimationDuration);

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
    public void but(View v){
        Log.d("bleh","score:"+score);
           Intent i=new Intent(this, q2.class);
           i.putExtra("blah",score);
           startActivity(i);
//           day.animate()
//                   .alpha(0f)
//                   .setDuration(mMediumAnimationDuration)
//                   .setListener(new AnimatorListenerAdapter() {
//                       @Override
//                       public void onAnimationEnd(Animator animation) {
//                           //day.setVisibility(View.GONE);
//                           if(day.isChecked()){
//                               day.toggle();
//                           }
//                           day.setText(R.string.country);
//                           day.animate().alpha(1f).setDuration(mMediumAnimationDuration);
//                       }
//                   });
//           date.animate()
//                   .alpha(0f)
//                   .setDuration(mMediumAnimationDuration)
//                   .setListener(new AnimatorListenerAdapter() {
//                       @Override
//                       public void onAnimationEnd(Animator animation) {
//                           //day.setVisibility(View.GONE);
//                           //day.setVisibility(View.GONE);
//                           if(date.isChecked()){
//
//                               date.toggle();
//                           }
//                           date.setText(R.string.place);
//                           date.animate().alpha(1f).setDuration(mMediumAnimationDuration);
//                       }
//                   });
//           year.animate()
//                   .alpha(0f)
//                   .setDuration(mMediumAnimationDuration)
//                   .setListener(new AnimatorListenerAdapter() {
//                       @Override
//                       public void onAnimationEnd(Animator animation) {
//                           //day.setVisibility(View.GONE);
//                           //day.setVisibility(View.GONE);
//                           if(year.isChecked()){
//                               year.toggle();
//                           }
//                           year.setText(R.string.floor);
//                           year.animate().alpha(1f).setDuration(mMediumAnimationDuration);
//                       }
//                   });
//           season.animate()
//                   .alpha(0f)
//                   .setDuration(mMediumAnimationDuration)
//                   .setListener(new AnimatorListenerAdapter() {
//                       @Override
//                       public void onAnimationEnd(Animator animation) {
//                           //day.setVisibility(View.GONE);
//                           //day.setVisibility(View.GONE);
//                           if(season.isChecked()){
//                               season.toggle();
//                           }
//                           season.setText(R.string.state);
//                           season.animate().alpha(1f).setDuration(mMediumAnimationDuration);
//                       }
//                   });
//           month.animate()
//                   .alpha(0f)
//                   .setDuration(mMediumAnimationDuration)
//                   .setListener(new AnimatorListenerAdapter() {
//                       @Override
//                       public void onAnimationEnd(Animator animation) {
//                           //day.setVisibility(View.GONE);
//                           //day.setVisibility(View.GONE);
//                           if(month.isChecked()){
//                               month.toggle();
//                           }
//                           month.setText(R.string.city);
//                           month.animate().alpha(1f).setDuration(mMediumAnimationDuration);
//                       }
//                   });



    }

}

