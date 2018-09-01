package com.example.mlabsystem2.setup_2;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ScheduleMain extends AppCompatActivity {

    private static final String TAG = "MEEEEEEEEEE";

    private RcvSchedule adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    DateFormat formatDateTime = DateFormat.getDateTimeInstance();
    Calendar dateTime = Calendar.getInstance();

    private static int notificationId = 0;
    private static int reqcode = 0;

    private ArrayList<String> taskFID = new ArrayList<>();

    public String formatedDate, formatedTime;

    FloatingActionButton fab;
    public static String firebase_id;

    FirebaseFirestore db;
    String patient_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list);

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

        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RcvSchedule(taskFID, this);
        recyclerView.setAdapter(adapter);

        loadTaskList();

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ScheduleMain.this);
                View mView = getLayoutInflater().inflate(R.layout.customdialog, null);
                final EditText addtask = (EditText) mView.findViewById(R.id.Addtask);
                final EditText addDes = mView.findViewById(R.id.AddDescription);
                Button setdate = (Button) mView.findViewById(R.id.setdate);
                Button settime = (Button) mView.findViewById(R.id.settime);
                final TextView display = (TextView) mView.findViewById(R.id.display);
                final Spinner myspinner = (Spinner) mView.findViewById(R.id.spinner);

                ArrayAdapter<String> myadapter = new ArrayAdapter<String>(ScheduleMain.this,
                        android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.spinnernames));
                myadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                myspinner.setAdapter(myadapter);


                setdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateDate();
                    }
                });

                settime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateTime();
                    }
                });

        /*        set.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        display.setText(formatDateTime.format(dateTime.getTime()));
                        if(addtask.getText().toString().isEmpty()) {
                            Toast.makeText(ScheduleMain.this, "Insert Something", Toast.LENGTH_SHORT).show();
                        }else {


                        }

                    }
                });*/

                display.setText(formatDateTime.format(dateTime.getTime()));

                //Add a Task
                mBuilder.setView(mView);
                mBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        if (addtask.getText().toString().isEmpty()) {
                            Toast.makeText(ScheduleMain.this, "Task Name cannot be empty.", Toast.LENGTH_SHORT).show();
                        } else {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            formatedDate = sdf.format(dateTime.getTime());
                            SimpleDateFormat sdft = new SimpleDateFormat("HH:mm a");
                            formatedTime = sdft.format(dateTime.getTime());
                            String task = String.valueOf(addtask.getText());
                            String descrption = String.valueOf(addDes.getText());

                            long TimeMillis = dateTime.getTimeInMillis();
                            long repeatFrequency = -1;

                            // Storing Task information on Firebase
                            Map<String, Object> user = new HashMap<>();
                            user.put("patient_uid", patient_uid);
                            user.put("Task", task);
                            user.put("Date", formatedDate);
                            user.put("Time", formatedTime);
                            user.put("TimeMillis", TimeMillis);
                            user.put("Description", descrption);
                            user.put("isSet", false);
                            // TODO: Change this to uuid or firebase generated id
//                            firebase_id = String.valueOf(System.currentTimeMillis());
//                            interestsMap.put("ID", firebase_id);


                            switch (myspinner.getSelectedItem().toString()) {
                                case "Every minute":
                                    repeatFrequency = 60 * 1000;
                                    break;
                                case "Every two minutes":
                                    repeatFrequency = 2 * 60 * 1000;
                                    break;
                                case "Every Day":
                                    repeatFrequency = 24 * 60 * 60 * 1000;
                                    break;
                            }

                            user.put("repeatFrequency", repeatFrequency);

                            // Add a new document with a generated ID
                            db.collection("Tasks")
                                    .document()
                                    .set(user)
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

//                            loadTaskList();
                            //End of Updating Info on FireBase
//
//                            Log.d(TAG, "onClick: Setting alarm");
//                            Intent intent = new Intent(ScheduleMain.this, AlarmReceiver.class);
//                            intent.putExtra("task", addtask.getText().toString());
//                            intent.putExtra("notificationId", notificationId);
//
//                            PendingIntent alarmintent = PendingIntent.getBroadcast(ScheduleMain.this, reqcode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//                            AlarmManager alarm = (AlarmManager)ScheduleMain.this.getSystemService(Context.ALARM_SERVICE);
//                            alarm.cancel(alarmintent)

                            reqcode++;
                            notificationId++;


                        }
                    }
                });

                mBuilder.setNegativeButton("Cancel", null);

                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    private void loadTaskList() {
        loadTaskList(true);

    }

    public void loadTaskList(final boolean fromCache) {

        Log.d("MEEE", "Before loading");

        taskFID = new ArrayList<>();

        // Refer to firebase docs for patient_id condition
        db.collection("Tasks")
                .whereEqualTo("patient_uid", patient_uid)
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
                                    break;
                                case MODIFIED:
                                    Log.d("MEEE", "MODIFIED : " + dc.getDocument().getData());
                                    break;
                                case REMOVED:
                                    Log.d("MEEE", "REMOVED : " + dc.getDocument().getData());
                                    taskFID.remove(document.getId());
                                    Log.d("MEEE", "REMOVED FID : " + taskFID);

                                    break;
                            }
                            adapter.setItems(taskFID);
                            adapter.notifyDataSetChanged();
                        }

                    }
                });

//
//        // TODO: Add "where patient_id = patientID" to the query
//        db.collection("Tasks")
//                .get(source)
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
////                                Log.d("MEEEE", document.getId() + " => " + document.getData());
//                                taskNames.add(document.getString("Task"));
//                                taskDates.add(document.getString("Date"));
//                                taskTimes.add(document.getString("Time"));
//                                taskFID.add(document.getId());
//                            }
//
////                            Log.d("MEEEE", "Loaded data");
//
//                            if (taskNames.size() == 0 && fromCache) {
//                                loadTaskList(false);
//
//                            }
//                            adapter.setItems(taskNames, taskDates, taskTimes, taskFID);
//                            adapter.notifyDataSetChanged();
//
//                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
    }

    private void updateDate() {
        new DatePickerDialog(this, d, dateTime.get(Calendar.YEAR), dateTime.get(Calendar.MONTH), dateTime.get(Calendar.DAY_OF_MONTH))
                .show();

    }

    private void updateTime() {
        new TimePickerDialog(this, t, dateTime.get(Calendar.HOUR_OF_DAY), dateTime.get(Calendar.MINUTE),
                false).show();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            dateTime.set(Calendar.YEAR, year);
            dateTime.set(Calendar.MONTH, month);
            dateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        }
    };

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateTime.set(Calendar.MINUTE, minute);
            dateTime.set(Calendar.SECOND, 0);
        }
    };

}
