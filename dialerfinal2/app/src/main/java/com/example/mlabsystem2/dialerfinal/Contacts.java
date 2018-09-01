package com.example.mlabsystem2.dialerfinal;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;

public class Contacts extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerAdaptercont adapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<String> contacts=new ArrayList<String>();
    ArrayList<String> numbers=new ArrayList<String>();
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);




        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        startManagingCursor(cursor);
        while (cursor.moveToNext()){
            contacts.add(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
            numbers.add(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
        }
        recyclerView=findViewById(R.id.recyclerView);
        layoutManager=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new RecyclerAdaptercont(contacts,numbers,cursor,this);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public  void onBackPressed(){
        Intent intent=new Intent(this,Dialerfinal.class);
        startActivity(intent);
    }
}
