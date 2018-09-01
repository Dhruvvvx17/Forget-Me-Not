package com.example.mlabsystem2.dialerfinal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Debug;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.provider.CallLog;
import android.provider.ContactsContract;

public class Logs extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerAdapter1 adapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<String> durations=new ArrayList<String>();
    ArrayList<String> numbers=new ArrayList<String>();
    Cursor cursor1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG}, 1);
        }
        cursor1 = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null,
                CallLog.Calls.DEFAULT_SORT_ORDER);
        startManagingCursor(cursor1);
        while(cursor1.moveToNext()){
            numbers.add(cursor1.getString(cursor1.getColumnIndex(CallLog.Calls.NUMBER)));
            String duration=(cursor1.getString(cursor1.getColumnIndex(CallLog.Calls.DURATION))+" s");
            durations.add(duration);
        }
        recyclerView=findViewById(R.id.recyclerView);
        layoutManager=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new RecyclerAdapter1(durations,numbers,this);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public  void onBackPressed(){
        Intent intent=new Intent(this,Dialerfinal.class);
        startActivity(intent);
    }
}
