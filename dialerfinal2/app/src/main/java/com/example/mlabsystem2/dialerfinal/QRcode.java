package com.example.mlabsystem2.dialerfinal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRcode extends AppCompatActivity {

    ImageView QRcode;
    String TAG = "MEEEEE" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        QRcode = findViewById(R.id.QRcode);

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String uid = prefs.getString("uid", "");

        Log.d(TAG,"Uid: "+uid);

        String text = uid ;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            QRcode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public void changeActivity(View view) {
        Intent intent = new Intent(this,PatientHome.class);
        startActivity(intent);

    }
}
