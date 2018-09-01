package com.example.mlabsystem2.dialerfinal;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.ArrayList;
import java.util.Map;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
CardView cardfeeds,cardmaps,cardschedule,cardgames;
FirebaseFirestore db;
String uid;
ArrayList<String> addresses;
    public static final String TAG ="Meee" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        uid = prefs.getString("uid", "");

        cardgames=(CardView)findViewById(R.id.games);
        cardfeeds=(CardView)findViewById(R.id.cardfeeds);
        cardmaps=(CardView)findViewById(R.id.cardmaps);
        cardschedule=(CardView)findViewById(R.id.schedule);
        cardfeeds.setOnClickListener(this);
        cardmaps.setOnClickListener(this);
        cardschedule.setOnClickListener(this);
        cardgames.setOnClickListener(this);

            final DocumentReference docRef = db.collection("Patients").document(uid);
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
                        addresses = new ArrayList<>();
                        Map<String, Object> details = (Map<String, Object>) snapshot.get("Details");
                        Log.d("MEEEE", "onComplete: " + details);
                        if (details != null) {
                            addresses.add(details.get("addrLine1").toString());
                            addresses.add(details.get("addrLine2").toString());
                            addresses.add(details.get("addrLine3").toString());

                        }

                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                }
            });

    }



   public void LaunchMap(View view,ArrayList<String>addresses) {

        Uri gmmIntentUri = Uri.parse("google.navigation:q="+addresses.get(0)+" "+addresses.get(1)+" "+addresses.get(2));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cardfeeds:{
                Intent feed=new Intent(this,FeedActivity.class);
                startActivity(feed);
            }break;
            case R.id.cardmaps:{
                LaunchMap(v,addresses);
            }break;
            case R.id.schedule:{
                Intent schedule=new Intent(this,Schedulemain.class);
                startActivity(schedule);
            }break;
            case  R.id.games:{
                Intent games=new Intent(this,Games.class);
                startActivity(games);
            }
        }

    }
}