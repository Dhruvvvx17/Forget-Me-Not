package com.example.mlabsystem2.setup_2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class PatientProfile extends AppCompatActivity {

    private TextView txt_location, txt_customize, txt_schedule; //clickables
    private FloatingActionButton fabEdit;
    private TextView tvname, tvphnumber, tvaddress;
    public static String DEFAULT_NAME = "N/A", DEFAULT_PHNUMBER = "N/A", DEFAULT_ADDRESS = "N/A";

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String name = prefs.getString("name", DEFAULT_NAME);
        String phnumber = prefs.getString("phnumber", DEFAULT_PHNUMBER);
        String address = prefs.getString("address", DEFAULT_ADDRESS);

        if (name.equals(DEFAULT_NAME) && phnumber.equals(DEFAULT_PHNUMBER) && address.equals(DEFAULT_ADDRESS)) {
            Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();

        } else {
            tvname.setText(name);
            tvphnumber.setText(phnumber);
            tvaddress.setText(address);
            DEFAULT_NAME = name;
            DEFAULT_PHNUMBER = phnumber;
            DEFAULT_ADDRESS = address;
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientprofile);

        txt_location = (TextView) findViewById(R.id.location);
        txt_schedule = (TextView) findViewById(R.id.schedule);
        txt_customize = (TextView) findViewById(R.id.customize);
        fabEdit = (FloatingActionButton) findViewById(R.id.fabEdit);

        tvname = (TextView) findViewById(R.id.tvName);
        tvphnumber = (TextView) findViewById(R.id.tvPhNumber);
        tvaddress = (TextView) findViewById(R.id.tvAddress);

        //tvname.setText(EditDetailsActivity.Name);


        // text behaves as buttons

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientProfile.this, PatientProfileNew.class);
                startActivity(intent);
            }
        });

        txt_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientProfile.this, LocationActivity.class);
                startActivity(intent);
            }
        });

        txt_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientProfile.this, ScheduleMain.class);
                startActivity(intent);

            }
        });

        txt_customize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientProfile.this, CustomizeMenu.class);
                startActivity(intent);

            }
        });


    }


}
