package com.example.mlabsystem2.setup_2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Quiz extends AppCompatActivity {
    private View intro;
    private View intro2;
    private View tutorial;
//    private View q2;
//    private View q1;
//    private View q2;
//    private View q1;
//    private View q2;
//    private View q1;
//    private View q2;
    private int mMediumAnimationDuration;
    private int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        intro = findViewById(R.id.intro);
        intro2= findViewById(R.id.intro2);
        tutorial=findViewById(R.id.tut);
        intro2.setVisibility(View.GONE);
        tutorial.setVisibility(View.GONE);
        mMediumAnimationDuration = getResources().getInteger(
                android.R.integer.config_mediumAnimTime);
    }

    public void but(View v) {
        if (flag == 1) {
            tutorial.setAlpha(0f);
            tutorial.setVisibility(View.VISIBLE);
            tutorial.animate()
                    .alpha(1f)
                    .setDuration(mMediumAnimationDuration)
                    .setListener(null);
            intro2.animate()
                    .alpha(0f)
                    .setDuration(mMediumAnimationDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            intro2.setVisibility(View.GONE);
                        }
                    });

        } else if (flag >1) {
            Intent intent = new Intent(this, Questions.class);
            startActivity(intent);
        }

        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        else if(flag==0){
            intro2.setAlpha(0f);
            intro2.setVisibility(View.VISIBLE);

            // Animate the content view to 100% opacity, and clear any animation
            // listener set on the view.
            intro2.animate()
                    .alpha(1f)
                    .setDuration(mMediumAnimationDuration)
                    .setListener(null);

            // Animate the loading view to 0% opacity. After the animation ends,
            // set its visibility to GONE as an optimization step (it won't
            // participate in layout passes, etc.)
            intro.animate()
                    .alpha(0f)
                    .setDuration(mMediumAnimationDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            intro.setVisibility(View.GONE);
                        }
                    });
        }
        flag++;
    }

}





