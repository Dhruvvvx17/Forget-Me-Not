package com.example.mlabsystem2.dialerfinal;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Dialerfinal extends AppCompatActivity implements View.OnClickListener {
    FloatingActionButton bt;
    Button contacts;
    Cursor cursor;
    String number;
    Button logs;
    Button emerge;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialerfinal);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG}, 1);
        }

        bt = findViewById(R.id.bt);
        emerge = findViewById(R.id.emerge);
        contacts = findViewById(R.id.contacts);
        editText = findViewById(R.id.et);
        bt.setOnClickListener(this);
        contacts.setOnClickListener(this);
        emerge.setOnClickListener(this);
        logs = findViewById(R.id.logs);
        if (getIntent().getStringExtra("number") != null) {
            editText.setText(getIntent().getStringExtra("number"));
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        }
        number = editText.getText().toString();

        logs.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {

        cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null,
                CallLog.Calls.DEFAULT_SORT_ORDER);
        startManagingCursor(cursor);

        number = editText.getText().toString();


        switch (view.getId()) {
            case R.id.bt: {


                cursor.moveToFirst();
                String number1;
                int i = 0;
                long logtime;
                long time;
                String date;
                for (i = 0; i <= 4; i++) {
                    time = System.currentTimeMillis();
                    number1 = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                    date = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
                    logtime = time - (Long.parseLong(date) + Long.parseLong(cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION))));
                    String type = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));
                    if ((number.equals(number1) || ("+91" + number).equals(number1) || ("+91" + number1).equals(number)) && logtime < 300000 && type.equals("2")) {
                        break;
                    } else {
                        cursor.moveToNext();
                    }
                }


                if (i < 5) {
                    Intent intent = new Intent(this, Pop.class);
                    intent.putExtra("number", number);
                    startActivity(intent);

                } else {
                    Intent call = new Intent(Intent.ACTION_CALL);
                    call.setData(Uri.parse("tel:" + number));
                    startActivity(call);
                        editText.setText(null);
                    }

                }
                break;


                case R.id.contacts: {
                    Intent cont = new Intent(this, Contacts.class);
                    startActivity(cont);
                }
                break;
                case R.id.logs: {
                    Intent log = new Intent(this, Logs.class);
                    startActivity(log);
                }
                break;
                case R.id.emerge:{
                  Intent emergency=new Intent (this,Emergency.class);
                  startActivity(emergency);
            }

            }
        }

        @Override
        public void onBackPressed () {
            super.onBackPressed();
            Intent back = new Intent(this, PatientHome.class);
            startActivity(back);
        }



}

