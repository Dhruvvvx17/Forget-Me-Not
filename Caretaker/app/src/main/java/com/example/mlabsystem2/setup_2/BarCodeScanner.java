package com.example.mlabsystem2.setup_2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.List;

import info.androidhive.barcode.BarcodeReader;

public class BarCodeScanner extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {

    String TAG = "MEEEE";
    FirebaseFirestore db;
    private BarcodeReader barcodeReader;
    int SCAN_BARCODE = 1021;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code_scanner);

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        Log.d(TAG, "onCreate: BarCodeScanner");
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_fragment);
    }


    @Override
    public void onScanned(Barcode barcode) {
        Log.d(TAG, "onScanned: " + barcode.displayValue);
        // play beep sound
        barcodeReader.playBeep();
//        Toast.makeText(this, "Please wait while we find the patient.", Toast.LENGTH_SHORT).show();
        PatientFound(barcode.displayValue);
    }

    private void PatientFound(final String p_uid) {
        DocumentReference docRef = db.collection("Patients").document(p_uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String name = document.getString("Name");
                        if (name == null) {
                            name = "Name not specified";
                        }

                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("patient_name", name);
                        editor.putString("patient_uid", p_uid);
                        editor.apply();

                        Intent intent = new Intent();
                        intent.putExtra("isModified", true);
                        setResult(SCAN_BARCODE, intent);
                        finish();
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("isModified", false);
        setResult(SCAN_BARCODE, intent);
        finish();
    }

    @Override
    public void onScannedMultiple(List<Barcode> list) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String s) {

    }

    @Override
    public void onCameraPermissionDenied() {
        Toast.makeText(getApplicationContext(), "Camera permission denied!", Toast.LENGTH_LONG).show();
        onBackPressed();
    }
}