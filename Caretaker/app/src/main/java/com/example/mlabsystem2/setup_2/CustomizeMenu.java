package com.example.mlabsystem2.setup_2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class CustomizeMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize);
    }

    public void editEmergencyContacts(View view) {
        Intent intent = new Intent(getApplicationContext(), EditContacts.class);
        startActivity(intent);
    }

    public void newsFeedInterests(View view) {
        Intent newfeedi = new Intent(this, EditInterests.class);
        startActivity(newfeedi);
    }
}
