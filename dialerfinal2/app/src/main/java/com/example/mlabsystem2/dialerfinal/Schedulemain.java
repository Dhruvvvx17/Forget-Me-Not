package com.example.mlabsystem2.dialerfinal;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Schedulemain extends AppCompatActivity {

    private static final String TAG = "MEEEE";

    private ArrayList<String> taskNames = new ArrayList<>();
    private ArrayList<String> taskDates = new ArrayList<>();
    private ArrayList<String> taskTimes = new ArrayList<>();
    private ArrayList<String> taskFID = new ArrayList<>();

    private static HashMap<String, PendingIntent> intents = new HashMap<>();

    public FirebaseFirestore db;
    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private static int reqcode = 0;
    DateFormat formatDateTime = DateFormat.getDateTimeInstance();
    Calendar dateTime = Calendar.getInstance();

    private int notificationId = 1;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedulemain);
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        uid = prefs.getString("uid", "");

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter(taskFID, this);
        recyclerView.setAdapter(adapter);


        loadTaskList();
    }

    private void loadTaskList() {
        loadTaskList(true);

    }

    public void loadTaskList(Boolean fromCache) {

        Log.d("MEEE", "Before loading");

        taskNames = new ArrayList<>();
        taskDates = new ArrayList<>();
        taskTimes = new ArrayList<>();
        taskFID = new ArrayList<>();

        // Refer to firebase docs for patient_id condition
        db.collection("Tasks")
                .whereEqualTo("patient_uid", uid)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "listen:error", e);
                            return;
                        }

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            QueryDocumentSnapshot document = dc.getDocument();
                            switch (dc.getType()) {
                                case ADDED:
                                    Log.d("MEEEE", "NEW " + dc.getDocument().getData());
                                    taskFID.add(document.getId());
                                    taskNames.add(document.getString("Task"));
                                    taskDates.add(document.getString("Date"));
                                    taskTimes.add(document.getString("Time"));

                                    if (document.getBoolean("isSet") != null && document.getBoolean("isSet")) {
                                        Log.d(TAG, "onEvent: Alarm already set.");
                                    } else {
                                        HashMap<String, Object> obj = new HashMap<>();
                                        obj.put("isSet", true);
                                        db.collection("Tasks").document(document.getId()).update(obj);

                                        Intent intent = new Intent(Schedulemain.this, AlarmReceiver.class);
                                        intent.putExtra("Task", document.getString("Task"));
                                        intent.putExtra("Description", document.getString("Description"));
                                        int p = (int) System.currentTimeMillis();
                                        intent.putExtra("notificationId", p);

                                        PendingIntent alarmintent = PendingIntent.getBroadcast(Schedulemain.this, reqcode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                                        intents.put(document.getId(), alarmintent);

                                        File file = new File(getDir("data", MODE_PRIVATE), "map");
//                                        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));
//                                        outputStream.writeObject(intents);
//                                        outputStream.flush();
//                                        outputStream.close();

                                        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
                                        assert alarm != null;
                                        if (document.getLong("repeatFrequency") == -1) {
                                            alarm.set(AlarmManager.RTC_WAKEUP, document.getLong("TimeMillis"), alarmintent);
                                        } else {
                                            alarm.setRepeating(AlarmManager.RTC_WAKEUP, document.getLong("TimeMillis"), document.getLong("repeatFrequency"), alarmintent);
                                        }
                                        Log.d(TAG, "onEvent: Alarm set.");
                                    }

                                    //adapter.setItems(taskNames,taskDates,taskTimes,taskFID);

                                    break;
                                case MODIFIED:
                                    Log.d("MEEE", "MODIFIED : " + dc.getDocument().getData());
                                    break;
                                case REMOVED: {
                                    Log.d("MEEE", "REMOVED : " + dc.getDocument().getData());
                                    int x = taskFID.indexOf(document.getId());
                                    AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
                                    if (intents.get(document.getId()) != null) {
                                        alarm.cancel(intents.get(document.getId()));
                                        Log.d("MEEE", "onEvent: Alarm cancelled");
                                    }
                                    Log.d(TAG, "onEvent: " + intents); //DON'T REMOVE THIS LINE

                                    deleteTask(x);
                                    Log.d("MEEE", "REMOVED FID : " + taskFID);
                                }

                            }
                            adapter.setItems(taskNames, taskDates, taskTimes, taskFID);
                            adapter.notifyDataSetChanged();
                        }

                    }
                });


    }


    private void deleteTask(final int firebase_id) {

        Log.d("MEEEE", "firebase id" + firebase_id + "...");
        db.collection("Tasks").document(taskFID.get(firebase_id))
                .delete();


        Log.d("MEEEEEEEEEEEEE", "DocumentSnapshot successfully deleted!");
        taskNames.remove(firebase_id);
        taskFID.remove(firebase_id);
        taskTimes.remove(firebase_id);
        taskDates.remove(firebase_id);
        adapter.setItems(taskNames, taskDates, taskTimes, taskFID);


    }

}


