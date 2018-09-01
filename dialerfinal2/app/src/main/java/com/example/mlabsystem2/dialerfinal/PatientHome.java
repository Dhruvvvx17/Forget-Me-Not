package com.example.mlabsystem2.dialerfinal;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.ArrayList;
import java.util.Map;

public class PatientHome extends AppCompatActivity implements View.OnClickListener {
    CardView cd, callcard;
    FirebaseFirestore db;
    TextView patientName;
    String uid;
    ArrayList<String>addresses;
    Handler handler;
    Runnable r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i =new Intent(getApplicationContext(),GPS_Service.class);
        startService(i);
        handler = new Handler();
        r = new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Toast.makeText(PatientHome.this, "user is inactive from last 1 minute",Toast.LENGTH_SHORT).show();
               // connectedNotify();
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.ic_blur_on_black_24dp)
                                .setContentTitle("Device Connected")
                                .setContentText("Click to monitor");

                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
               // mBuilder.setSound(alarmSound);
                //mBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
                mBuilder.setVibrate(new long[] { 1000, 1000});
                mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

            }
        };
        startHandler();

        patientName =findViewById(R.id.patient_name);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        }
       // Intent i =new Intent(getApplicationContext(),GPS_Service.class);
        //startService(i);

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        uid = prefs.getString("uid", "");
        final DocumentReference docRef = db.collection("Patients").document(uid);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {


            public static final String TAG ="Mee" ;

            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                    addresses = new ArrayList<>();
                    Map<String, Object> details = (Map<String, Object>) snapshot.get("Details");
                    Log.d("MEEEE", "onComplete: " + details);
                    if (details != null) {
                        addresses.add(details.get("Name").toString());
                        patientName.setText(addresses.get(0));
                    }

                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });





        cd = (CardView) findViewById(R.id.card3);
        callcard = (CardView) findViewById(R.id.callcard);
        cd.setOnClickListener(this);
        callcard.setOnClickListener(this);


    }
    public void connectedNotify() {
        Integer mId = 0;
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_blur_on_black_24dp)
                        .setContentTitle("Device Connected")
                        .setContentText("Click to monitor");

        Intent resultIntent = new Intent(this, PatientHome.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(PatientHome.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(getApplicationContext(),
                        0,
                        resultIntent,
                        PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setOngoing(true);
        Notification note = mBuilder.build();
        //note.defaults |= Notification.DEFAULT_VIBRATE;
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(mId, note);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);
        mBuilder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.card3: {
                Intent intent = new Intent(this, Main2Activity.class);
                startActivity(intent);
            }break;
            case R.id.callcard: {
                Intent intent1 = new Intent(this, Dialerfinal.class);
                startActivity(intent1);
            }

        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void generateQRcode(View view) {
        Intent intent = new Intent(this,QRcode.class);
        startActivity(intent);
    }
    public void change(View v){
        Intent i1=new Intent(this,MainActivity_aws.class);
        startActivity(i1);
    }
    @Override
    public void onUserInteraction() {
        // TODO Auto-generated method stub
        super.onUserInteraction();
        stopHandler();//stop first and then start
        startHandler();
    }
    public void stopHandler() {
        handler.removeCallbacks(r);
    }
    public void startHandler() {
        handler.postDelayed(r, 1*60*1000); //for 5 minutes
    }
}
