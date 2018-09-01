package com.example.mlabsystem2.setup_2;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class patient_add extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_add);

        EditText name = (EditText)findViewById(R.id.name);
        Button save = (Button)findViewById(R.id.save);
        final PatientDb dbHelper = new PatientDb(this);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = String.valueOf(R.id.name);
                dbHelper.insertnewName(name);
                Intent returnintent = new Intent();
                returnintent.putExtra("Name",name);
                setResult(Activity.RESULT_OK,returnintent);
                finish();
            }
        });
    }
}
