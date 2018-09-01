package com.example.mlabsystem2.setup_2;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Source;

import java.util.HashMap;
import java.util.Map;

public class EditContacts extends AppCompatActivity {

    FirebaseFirestore db;

    private EditText num1, num2, num3, name1, name2, name3;
    private ImageButton edit, save;
    public String patient_uid;
    String TAG = "MEEEEEE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contacts);


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

        num1 = findViewById(R.id.Phone_number1);
        num2 = findViewById(R.id.Phone_number2);
        num3 = findViewById(R.id.Phone_number3);
        name1 = findViewById(R.id.Name1);
        name2 = findViewById(R.id.Name2);
        name3 = findViewById(R.id.Name3);
        edit = findViewById(R.id.edit);
        save = findViewById(R.id.save);

        num1.setEnabled(false);
        num1.setCursorVisible(false);
        num2.setEnabled(false);
        num2.setCursorVisible(false);
        num3.setEnabled(false);
        num3.setCursorVisible(false);
        name1.setEnabled(false);
        name1.setCursorVisible(false);
        name2.setEnabled(false);
        name2.setCursorVisible(false);
        name3.setEnabled(false);
        name3.setCursorVisible(false);

        edit.setVisibility(edit.VISIBLE);
        save.setVisibility(save.GONE);

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        patient_uid = prefs.getString("patient_uid", "");
        Log.d("MEEEEEEEE", patient_uid);

        loadContacts();

    }

    public void editContacts(View view) {
        num1.setEnabled(true);
        num1.setCursorVisible(true);
        num2.setEnabled(true);
        num2.setCursorVisible(true);
        num3.setEnabled(true);
        num3.setCursorVisible(true);
        name1.setEnabled(true);
        name1.setCursorVisible(true);
        name2.setEnabled(true);
        name2.setCursorVisible(true);
        name3.setEnabled(true);
        name3.setCursorVisible(true);

        edit.setVisibility(edit.GONE);
        save.setVisibility(save.VISIBLE);
        Log.d(TAG, "after editContacts: ");
    }

    public void saveContacts(View view) {
        Log.d("MEEEE", "saveContacts: ");
        num1.setEnabled(false);
        num1.setCursorVisible(false);
        num2.setEnabled(false);
        num2.setCursorVisible(false);
        num3.setEnabled(false);
        num3.setCursorVisible(false);
        name1.setEnabled(false);
        name1.setCursorVisible(false);
        name2.setEnabled(false);
        name2.setCursorVisible(false);
        name3.setEnabled(false);
        name3.setCursorVisible(false);

        edit.setVisibility(edit.VISIBLE);
        save.setVisibility(save.GONE);


        //Updating emergency contacts on Firebase
        Map<String, Object> contacts = new HashMap<String, Object>();
        contacts.put("name1", String.valueOf(name1.getText()));
        contacts.put("num1", String.valueOf(num1.getText()));
        contacts.put("name2", String.valueOf(name2.getText()));
        contacts.put("num2", String.valueOf(num2.getText()));
        contacts.put("name3", String.valueOf(name3.getText()));
        contacts.put("num3", String.valueOf(num3.getText()));

        Map<String, Object> EmergencyContacts = new HashMap<String, Object>();
        EmergencyContacts.put("EmergencyContacts", contacts);

        // Add a new document with a generated ID
        db.collection("Patients")
                .document(patient_uid)
                .update(EmergencyContacts)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {
                        Log.d("MEEEEEEEEEEEEEEE", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MEEE", "Error adding document", e);
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void loadContacts() {
        loadContacts(true);
    }

    public void loadContacts(final boolean fromCache) {

        final Source source = (fromCache) ? Source.CACHE : Source.DEFAULT;

        final DocumentReference docRef = db.collection("Patients").document(patient_uid);

        docRef.get(source).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("MEEEEEE", "DocumentSnapshot data: " + document.getData());

                        Map<String, Object> contacts = (Map<String, Object>) document.get("EmergencyContacts");
                        if (contacts != null) {
                        name1.setText(String.valueOf(contacts.get("name1")));
                        num1.setText( contacts.get("num1").toString());
                        name2.setText( contacts.get("name2").toString());
                        num2.setText( contacts.get("num2").toString());
                        name3.setText( contacts.get("name3").toString());
                        num3.setText( contacts.get("num3").toString());
                        }
                    } else {
                        if (fromCache) {
                            loadContacts(false);
                        }
                        Log.d("MEEEEEE", "No such document");

                        num1.setEnabled(true);
                        num1.setCursorVisible(true);
                        num2.setEnabled(true);
                        num2.setCursorVisible(true);
                        num3.setEnabled(true);
                        num3.setCursorVisible(true);
                        name1.setEnabled(true);
                        name1.setCursorVisible(true);
                        name2.setEnabled(true);
                        name2.setCursorVisible(true);
                        name3.setEnabled(true);
                        name3.setCursorVisible(true);

                        save.setVisibility(save.VISIBLE);
                        edit.setVisibility(edit.GONE);

                    }
                } else {
                    Log.d("MEEEEEE", "get failed with ", task.getException());
                }
            }
        });


    }
}

