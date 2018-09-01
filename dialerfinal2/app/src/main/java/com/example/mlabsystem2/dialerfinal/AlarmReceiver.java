package com.example.mlabsystem2.dialerfinal;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "12323";

    @Override
    public void onReceive(Context context, Intent intent) {
//        Intent mainIntent = new Intent(context, Schedulemain.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);
//
//        String message = intent.getStringExtra("task");
//        int notificationId = intent.getIntExtra("notificationId", 0);
//
//        NotificationManager myNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        Notification.Builder builder = new Notification.Builder(context);
//        builder.setSmallIcon(android.R.drawable.ic_dialog_info).setContentTitle("Reminder!!!").setContentText(message)
//                .setWhen(System.currentTimeMillis()).setAutoCancel(true).setContentIntent(contentIntent).setSound(soundUri)
//                .setOngoing(true);
//
//        myNotificationManager.notify(notificationId, builder.build());
//        Vibrator v = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "NotifChannel";
            String description = "Deliver nottifs";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
            Log.d("WHAAA", "onReceive: Channel Created");

        }

        Intent scheduleIntent = new Intent(context, Schedulemain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, scheduleIntent, 0);


        String Description = intent.getStringExtra("Description"), Task = intent.getStringExtra("Task");
        Log.d("WHAAA", "onReceive: HERE");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_action_add)
                .setContentTitle(Task)
                .setContentText(Description)
                .setContentIntent(pIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // notificationId is a unique int for each notification that you must define
        int notificationId = intent.getIntExtra("notificationId", 0);
        notificationManager.notify(notificationId, mBuilder.build());
        Log.d("WHAAA", "onReceive: Notif" + notificationId);


    }
}
