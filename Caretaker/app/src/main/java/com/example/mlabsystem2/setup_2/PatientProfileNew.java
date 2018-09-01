package com.example.mlabsystem2.setup_2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.ArrayList;
import java.util.Map;

public class PatientProfileNew extends AppCompatActivity implements View.OnClickListener {

    String TAG = "MEEEE";
    Button  trackLocation;
    FirebaseFirestore db;
    String patient_uid;
    TextView patientName, patientNumber;
    ArrayList<String> coordinates=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_profile_new);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myToolbar.setBackgroundColor(getResources().getColor(R.color.darkBlue));
        myToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.White), PorterDuff.Mode.SRC_ATOP);

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        patient_uid = prefs.getString("patient_uid", "");

        final DocumentReference docRef = db.collection("Patients").document(patient_uid);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {


            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                    coordinates = new ArrayList<>();
                    Map<String, Object> details = (Map<String, Object>) snapshot.get("coordinates");
                    Log.d("MEEEE", "onComplete: " + details);
                    if (details != null) {
                        coordinates.add(details.get("latitude").toString());
                        coordinates.add(details.get("longitude").toString());


                    }

                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });

        trackLocation=findViewById(R.id.button3);
        patientName = findViewById(R.id.patient_name);
        patientNumber = findViewById(R.id.patient_number);
        trackLocation.setOnClickListener(this);

        loadDetails();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadDetails() {

        final DocumentReference docRef = db.collection("Patients").document(patient_uid);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                    Map<String, Object> Details = (Map<String, Object>) snapshot.get("Details");

                    if (Details != null) {
                        String Name, phNumber;
                        Name = Details.get("Name").toString();
                        phNumber = Details.get("phNumber").toString();

                        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        if (phNumber != null) {
                            patientNumber.setText(phNumber);
                            editor.putString("patient_number", phNumber);
                        }
                        if (Name != null) {
                            patientName.setText(Name);
                            editor.putString("patient_name", Name);
                        }
                        editor.apply();
                    }
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, PatientMenu.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void LaunchMap(View view,ArrayList<String>coordinates) {

            Uri gmmIntentUri = Uri.parse("google.navigation:q="+Double.parseDouble(coordinates.get(0))+","+Double.parseDouble(coordinates.get(1)));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
            }


    public void launchScheduleActivity(View view) {
        Intent intent = new Intent(this, ScheduleMain.class);
        startActivity(intent);
    }

    public void launchEmergencyContacts(View view) {
        Intent intent = new Intent(this, EditContacts.class);
        startActivity(intent);
    }

    public void launchNewsFeedActivity(View view) {
        Intent newfeedi = new Intent(this, EditInterests.class);
        startActivity(newfeedi);
    }

    public void launchMusicPrefsActivity(View view) {
//        Intent newfeedi = new Intent(this, MusicPreferences.class);
//        startActivity(newfeedi);
    }

    public void editDetails(View view) {
        Intent intent = new Intent(this, EditPatientDetails.class);
        startActivity(intent);
    }
    public void quiz(View v){
        Intent intent01 = new Intent(this, Quiz.class);
        startActivity(intent01);
    }

    @Override
    public void onClick(View v) {
        LaunchMap(v,coordinates);
    }
    public void detect(View v){
        Intent is=new Intent(this,MainActivity_aws1.class);
        startActivity(is);
    }
}
