package com.example.mlabsystem2.setup_2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditPatientDetails extends AppCompatActivity {

    TextView name1, phone1, add1, add2, add3;
    EditText editName1, editPhone1, editAdd1, editAdd2, editAdd3;
    Button edit, save;
    ConstraintLayout l1, l2;

    FirebaseFirestore db;

    ArrayList<String> details;

    String patient_uid;
    String TAG = "MEEEEEEE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_details);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
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


        name1 = findViewById(R.id.nameText);
        phone1 = findViewById(R.id.phoneText);
        add1 = findViewById(R.id.addressText1);
        add2 = findViewById(R.id.addressText2);
        add3 = findViewById(R.id.addressText3);
        edit = findViewById(R.id.EditB);


        editName1 = findViewById(R.id.EditName);
        editPhone1 = findViewById(R.id.EditPhone);
        editAdd1 = findViewById(R.id.EditAddress1);
        editAdd2 = findViewById(R.id.EditAddress2);
        editAdd3 = findViewById(R.id.EditAddress3);
        save = findViewById(R.id.Save);

        l1 = findViewById(R.id.layout1);
        l2 = findViewById(R.id.layout2);

        l2.setVisibility(l2.GONE);
        l1.setVisibility(l1.VISIBLE);
        loadDetails();
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

                    details = new ArrayList<>();

                    Map<String, Object> pDetails = (Map<String, Object>) snapshot.get("Details");

                    if (pDetails != null) {

                        name1.setText(pDetails.get("Name").toString());
                        phone1.setText(pDetails.get("phNumber").toString());
                        add1.setText(pDetails.get("addrLine1").toString());
                        add2.setText(pDetails.get("addrLine2").toString());
                        add3.setText(pDetails.get("addrLine3").toString());

                        editName1.setText(pDetails.get("Name").toString());
                        editPhone1.setText(pDetails.get("phNumber").toString());
                        editAdd1.setText(pDetails.get("addrLine1").toString());
                        editAdd2.setText(pDetails.get("addrLine2").toString());
                        editAdd3.setText(pDetails.get("addrLine3").toString());
                    } else {
                        l1.setVisibility(l1.GONE);
                        l2.setVisibility(l2.VISIBLE);
                    }
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }

    public void saveDetails(View view) {

        Map<String, Object> user = new HashMap<>();

        user.put("Name", editName1.getText().toString());
        user.put("phNumber", editPhone1.getText().toString());
        user.put("addrLine1", editAdd1.getText().toString());
        user.put("addrLine2", editAdd2.getText().toString());
        user.put("addrLine3", editAdd3.getText().toString());

        Map<String, Object> Details = new HashMap<String, Object>();
        Details.put("Details", user);

        // Add a new document with a generated ID
        db.collection("Patients")
                .document(patient_uid)
                .update(Details)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error adding document", e);
                    }
                });

        Intent intent = getIntent();
        if (intent.getBooleanExtra("isFromScanner", false)) {
            Intent in = new Intent(this, PatientProfileNew.class);
            startActivity(in);
            return;
        }

        l2.setVisibility(l2.GONE);
        l1.setVisibility(l1.VISIBLE);
    }

    public void editDetails(View view) {

        l1.setVisibility(l1.GONE);
        l2.setVisibility(l2.VISIBLE);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, PatientProfileNew.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
