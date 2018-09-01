package com.example.mlabsystem2.setup_2;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Patient_list extends AppCompatActivity {

    private ArrayList<String> patientNames = new ArrayList<>();
    private ArrayList<String> Names = new ArrayList<>();
    PatientDb dbHelper;
    private PatientViewAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;



    FloatingActionButton add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);

        dbHelper = new PatientDb(this);
        loadNameList();

        add = (FloatingActionButton) findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), patient_add.class);
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("Name");
            }
        }
    }

    private void loadNameList() {
        patientNames = dbHelper.getNameList();
        if (adapter == null) {
            recyclerView = (RecyclerView) findViewById(R.id.patient_list);
            layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new PatientViewAdapter(patientNames, this);
            recyclerView.setAdapter(adapter);
        } else {
            Names.addAll(patientNames);
            adapter.notifyDataSetChanged();

        }
    }
}
