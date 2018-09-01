package com.example.mlabsystem2.setup_2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class end extends AppCompatActivity {
    private TextView txt;
    private TextView txt1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        Intent intent=getIntent();
        int score=intent.getIntExtra("blah",0);
        txt=findViewById(R.id.result);
        txt1=findViewById(R.id.explain);
        txt.setText("Total: "+score);
        if(score>=24){

            txt1.setText("Everything is fine, you suffer from no cognitive impairment. You do not require this app.");
        }
        else if(score>=20){
           txt1.setText("It appears that patient suffers from mild cognitive impairment, this app should be helpful in " +
                   "managing day to day activities. " + "Please do verify results with a certified doctor as well." +
                   " If in the future the patient feels uncomfortable with the app take the test again to verify" +
                   " cognitive impairment or consult your doctor.");
        }
        else if(score>=18){
            txt1.setText("It appears that patient suffers from moderate cognitive impairment, this app should be helpful in " +
                    "managing day to day activities, But caretaker must be present more often, in case help is required" +
                    "(tutorial mode is always on). Please do verify results with a certified doctor as well.");
        }
        else{
            txt1.setText("We apologize, but this app cannot help. We hope the best for you and your patient.");
        }

    }
    public void finis(View v){
        Intent intent10 = new Intent(this, PatientProfileNew.class);
        startActivity(intent10);
    }
}
