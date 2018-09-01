package com.example.mlabsystem2.dialerfinal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class YourReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
            Intent intent1 = new Intent(context, PatientHome.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }
    }
}
