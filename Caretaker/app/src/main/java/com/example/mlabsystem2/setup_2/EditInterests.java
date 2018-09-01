package com.example.mlabsystem2.setup_2;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditInterests extends AppCompatActivity implements View.OnClickListener {
    EditText interest;
    ArrayList<String> interests = new ArrayList<String>();
    String singleInterest;
    FirebaseFirestore db;
    Button submit;
    String patient_uid;

    String TAG = "MEEEEEEEEEEE";

    private RcvInterests adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_interests);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        myToolbar.setBackgroundColor(getResources().getColor(R.color.darkBlue));
        myToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.White), PorterDuff.Mode.SRC_ATOP);

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        patient_uid = prefs.getString("patient_uid", "");

        interest = findViewById(R.id.interest);
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(this);

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);


        recyclerView = findViewById(R.id.recycler_view1);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RcvInterests(interests, this, patient_uid);
        recyclerView.setAdapter(adapter);

        loadInterests();

        Log.d("MEEEEEEEE", patient_uid);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void loadInterests() {
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
                    ArrayList<String> newInterests = (ArrayList<String>) snapshot.get("Interests");
                    if (newInterests != null) {
                        interests = newInterests;
                        adapter.setItems(interests);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                singleInterest = interest.getText().toString().trim();
                if (singleInterest.equals("")) {
                    Toast toast = Toast.makeText(this, "Please enter some text.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                    break;
                }
                interests.add(singleInterest);
                interest.setText(null);

                Map<String, Object> interestsMap = new HashMap<>();
                interestsMap.put("Interests", interests);

                db.collection("Patients")
                        .document(patient_uid)
                        .update(interestsMap);
                break;
        }
    }
}

