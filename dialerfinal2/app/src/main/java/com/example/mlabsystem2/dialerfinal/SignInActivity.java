package com.example.mlabsystem2.dialerfinal;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "MEEEE";
    FirebaseFirestore db;
    boolean isFound = false;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
//        }

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
    }

    @Override
    protected void onStart() {
        super.onStart();

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String uid = prefs.getString("uid", "");

        Log.d("MEEEEE", uid);

        if (uid.equals("")) {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);
        } else {
            callPatientHome();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                Toast.makeText(this, "Signed In", Toast.LENGTH_SHORT).show();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                uid = user.getUid();
                SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("uid", uid);
                editor.apply();

                findPatient(uid);

            } else {
                Toast.makeText(this, "Couldn't sign you in. Please check your internet connectivity.", Toast.LENGTH_SHORT).show();
                // Sign in failed. If response is null the interestsMap canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                Intent intent = new Intent(this,Main4Activity.class);
                startActivity(intent);
            }
        }
    }

    private boolean findPatient(String p_uid) {

        DocumentReference docRef = db.collection("Patients").document(p_uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                        isFound = true;
                    } else {
                        Log.d(TAG, "No such document");

                        Map<String, Object> patient = new HashMap<>();
                        patient.put("uid",uid);

                        db.collection("Patients")
                                .document(uid)
                                .set(patient)
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
                        callQRcode();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        return isFound;
    }
    private void callQRcode(){
        Intent in = new Intent(this, QRcode.class);
        startActivity(in);
    }


    private void callPatientHome() {
        Intent in = new Intent(this, Main4Activity.class);
        startActivity(in);
    }

}
