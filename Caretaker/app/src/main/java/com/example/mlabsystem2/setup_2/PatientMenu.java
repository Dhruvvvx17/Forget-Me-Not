package com.example.mlabsystem2.setup_2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class PatientMenu extends AppCompatActivity {

    FirebaseFirestore db;
    String uid, patient_uid, p_name;

    LinearLayout ll;
    Button add_patient;
    TextView patient_name;

    int SCAN_BARCODE = 1021;
    String TAG = "MEEEE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_menu);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setBackgroundColor(getResources().getColor(R.color.darkBlue));

        ll = findViewById(R.id.patient_1);
        add_patient = findViewById(R.id.add_patient);
        patient_name = findViewById(R.id.patient_name);

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        uid = prefs.getString("uid", "");
        patient_uid = prefs.getString("patient_uid", "");
        p_name = prefs.getString("patient_name", "undefined");

        if (patient_uid.equals("")) {
            ll.setVisibility(ll.GONE);
            add_patient.setText(R.string.add_patient);
//            addPatient();
        } else {
            ll.setVisibility(ll.VISIBLE);
            patient_name.setText(p_name);
            add_patient.setText(R.string.change_patient);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

    public void SelectProfile(View view) {

        Intent intent = new Intent(getApplicationContext(), PatientProfileNew.class);
        startActivity(intent);
    }

    private void addPatient() {
        Intent bc_scanner = new Intent(this, BarCodeScanner.class);
        startActivityForResult(bc_scanner, SCAN_BARCODE);
    }

    public void addPatient(View view) {
        addPatient();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data.getBooleanExtra("isModified", false)) {
//            Intent intent = getIntent();
//            finish();
//            startActivity(intent);

            Intent intent = new Intent(this,EditPatientDetails.class);
            intent.putExtra("isFromScanner",true);
            startActivity(intent);
        }

    }
}
