package com.example.mlabsystem2.dialerfinal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Source;

public class taskDescription extends AppCompatActivity {

    TextView taskName, taskDate, taskTime, taskDes;
    String  fid;

    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_description);

        db = FirebaseFirestore.getInstance();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        taskName = findViewById(R.id.task);
        taskTime = findViewById(R.id.time);
        taskDate = findViewById(R.id.date);
        taskDes = findViewById(R.id.description);
        fid=getIntent().getStringExtra("id");

        Source source = Source.CACHE;

        DocumentReference docRef = db.collection("Tasks").document(fid);
        docRef.get(source).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot document = task.getResult();

                        Log.d("MEEE", "DocumentSnapshot data: " + document.getData());

                        taskName.setText("TASK: " + document.getString("Task"));
                        taskDate.setText("DATE: "+document.getString("Date"));
                        taskTime.setText("TIME: "+document.getString("Time"));
                        taskDes.setText("DESCRIPTION: "+document.getString("Description"));

            }
        });


    }
}
