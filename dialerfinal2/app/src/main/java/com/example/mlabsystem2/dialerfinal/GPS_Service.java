package com.example.mlabsystem2.dialerfinal;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class GPS_Service extends Service {

    FirebaseFirestore db;
    String uid;
    private LocationListener listener;
    private LocationManager locationManager;
    private static final int NOTIFICATION_ID = 101;
    public Double lat=0.0, lng=0.0;
    int count=0;


//    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        uid = prefs.getString("uid", "");
        final Location location=new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(12.9345d);
        Log.d("Meee", "changed");
        location.setLongitude(77.5345d);
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);


        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                lat=location.getLatitude();
                lng=location.getLongitude();
                Map<String, Object> user = new HashMap<>();
                user.put("latitude", String.valueOf(lat));
                user.put("longitude", String.valueOf(lng));
                Map<String, Object> coordinates = new HashMap<String, Object>();
                coordinates.put("coordinates", user);
                db.collection("Patients")
                        .document(uid)
                        .update(coordinates);


                Intent i = new Intent("location_update");
                i.putExtra("coordinates",lat+" "+lng);
                sendBroadcast(i);

                count=count+1;
                Log.d("meee", "onLocationChanged: "+count);
                if(count>=2){
                    Toast.makeText(getApplicationContext(),"You are out of safe zone",Toast.LENGTH_SHORT).show();
                  sendAlertSMS(lat,lng);
                }

                else{
                    Toast.makeText(getApplicationContext(), "Loading location", Toast.LENGTH_SHORT).show();
                }



            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        //noinspection MissingPermission
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,60000,2,listener);

//        final Timer timer = new Timer ();
//        TimerTask hourlyTask = new TimerTask () {
//            @Override
//            public void run () {
                lat=location.getLatitude();
                lng=location.getLongitude();
                Map<String, Object> user = new HashMap<>();
                user.put("latitude", String.valueOf(lat));
                user.put("longitude", String.valueOf(lng));
                Map<String, Object> coordinates = new HashMap<String, Object>();
                coordinates.put("coordinates", user);
                db.collection("Patients")
                        .document(uid)
                        .update(coordinates);
//            }
//        };

// schedule the task to run starting now and then every hour...
      //  timer.schedule (hourlyTask, 0l, 1000*1*60);



     showForegroundNotification("GPS SERVICE IS RUNNING");

        //ALERT IS SHOWN IF PATIENT IS 4m AWAY FROM CURRENT LOCATION(HOME COORDINATES)


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locationManager != null){
            //noinspection MissingPermission
            locationManager.removeUpdates(listener);
        }
    }



    private void showForegroundNotification(String contenttext) {
        // Create intent that will bring our app to the front, as if it was tapped in the app
        // launcher
       // Intent showTaskIntent = new Intent(getApplicationContext(), Gpscoordinates.class);
        //showTaskIntent.setAction(Intent.ACTION_MAIN);
        //showTaskIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        //showTaskIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //PendingIntent contentIntent = PendingIntent.getActivity(
          //      getApplicationContext(),
            //    0,
          //      showTaskIntent,
              //  PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(getApplicationContext())
                .setContentTitle(getString(R.string.app_name))
                .setContentText(contenttext)
                .setPriority(Notification.PRIORITY_MAX)
               .setSmallIcon(R.drawable.gps_service)
                .setWhen(System.currentTimeMillis())
                //.setContentIntent(contentIntent)
                .build();
        startForeground(NOTIFICATION_ID, notification);
    }


    private void sendAlertSMS(double lat, double lng){

        String smsTo = "8762557133"; // some phone number here
        String smsMessage = "Latitude:"+lat+"Longitude"+lng;
      //  Toast.makeText(getApplicationContext(),Integer.toString(count),Toast.LENGTH_SHORT).show();
        SmsManager.getDefault().sendTextMessage(smsTo, null,smsMessage , null,null);
//        SmsManager smsMgr = SmsManager.getDefault();
//        smsMgr.sendTextMessage(smsTo, null, smsMessage, null, null);


    }
}
/////////////////
///*private void sendSMS(String phoneNumber, String message)
//{
//    PendingIntent pi = PendingIntent.getActivity(this, 0,
//            new Intent(this, SMS.class), 0);
//    SmsManager sms = SmsManager.getDefault();
//    sms.sendTextMessage(phoneNumber, null, message, pi, null);
//}*/



/*int icon = R.drawable.notification_icon;        // icon from resources
CharSequence tickerText = "Hello";              // ticker-text
long when = System.currentTimeMillis();         // notification time
Context context = getApplicationContext();      // application Context
CharSequence contentTitle = "My notification";  // expanded message title
CharSequence contentText = "Hello World!";      // expanded message text

Intent notificationIntent = new Intent(this, MyClass.class);
PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

// the next two lines initialize the Notification, using the configurations above
Notification notification = new Notification(icon, tickerText, when);
notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);*/