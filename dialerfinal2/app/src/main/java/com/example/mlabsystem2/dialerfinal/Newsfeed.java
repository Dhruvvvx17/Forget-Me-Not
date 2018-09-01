package com.example.mlabsystem2.dialerfinal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Newsfeed extends AppCompatActivity {
    FirebaseFirestore db;
    ArrayList<String> interests=new ArrayList<>();
    String uid;
    String TAG="Meeee";
    String API_KEY = "3ef9a0a52d4b4d61975877cc0fbb990c"; // ### YOUE NEWS API HERE ###
    String[] NEWS_SOURCE = {"bbc-news", "abc-news", "axios", "bbc-sport", "blasting-news-br", "bloomberg", "cbc-news", "cnbc", "cnn","daily-mail",
    "business-insider","espn","financial-times","google-news","fox-news","hacker-news","info-money","mirror","mtv-news","national-geographic","news24",
    "new-scientist","politico","time","the-hindu"};
    RecyclerView listNews;
    ProgressBar loader;
    private RecyclerAdapterfeed adapter;
    private RecyclerView.LayoutManager layoutManager;

    ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
    static final String KEY_AUTHOR = "author";
    static final String KEY_TITLE = "title";
    static final String KEY_DESCRIPTION = "description";
    static final String KEY_URL = "url";
    static final String KEY_URLTOIMAGE = "urlToImage";
    static final String KEY_PUBLISHEDAT = "publishedAt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        uid = prefs.getString("uid", "");

        listNews = (RecyclerView) findViewById(R.id.listNews);
        loader = (ProgressBar) findViewById(R.id.loader);
        loadInterests();










    }
    public void loadNews(){
        for(int i=0;i<NEWS_SOURCE.length;i++) {
            String m = String.valueOf(i);
            DownloadNews newsTask = new DownloadNews();

            if (Function.isNetworkAvailable(getApplicationContext())) {

                newsTask.execute(m);
            } else {
                if(i==1) {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                    loader.setVisibility(View.GONE);
                }
            }
        }
    }
    public void loadInterests(){
        final DocumentReference docRef = db.collection("Patients").document(uid);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                    interests = new ArrayList<>();
                    interests = (ArrayList<String>)snapshot.get("Interests");
                    Log.d("MEEEE", "onComplete: " + interests);
                    if(interests.size()!=0) {
                        share(interests);
                        loadNews();
                    }
                } else {
                    Log.d(TAG, "Current data: null");
                    Toast.makeText(getApplicationContext(),"No interests or no news",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void share(ArrayList<String>interests){
        //Set<String> set = .getStringSet("key", null);
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

//Set the values
        Set<String> set = new HashSet<String>();
        set.addAll(interests);
        editor.putStringSet("interests", set);
        editor.commit();
    }


    class DownloadNews extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... args) {
            String urlParameters = "";
            String xml = "";
            int l = Integer.parseInt(args[0]);


            xml = Function.excuteGet("https://newsapi.org/v2/top-headlines?sources=" + NEWS_SOURCE[l] + "&apiKey=" + API_KEY, urlParameters);
            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {

            Set<String> interest1 = new HashSet<String>();
            ArrayList<String> interests=new ArrayList();
            SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
            interest1=prefs.getStringSet("interests",null);
            interests.addAll(interest1);

            if (xml.length() > 10) { // Just checking if not empty

                try {
                    JSONObject jsonResponse = new JSONObject(xml);
                    JSONArray jsonArray = jsonResponse.optJSONArray("articles");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(KEY_AUTHOR, jsonObject.optString(KEY_AUTHOR).toString());
                        map.put(KEY_TITLE, jsonObject.optString(KEY_TITLE).toString());
                        map.put(KEY_DESCRIPTION, jsonObject.optString(KEY_DESCRIPTION).toString());
                        map.put(KEY_URL, jsonObject.optString(KEY_URL).toString());
                        map.put(KEY_URLTOIMAGE, jsonObject.optString(KEY_URLTOIMAGE).toString());
                        map.put(KEY_PUBLISHEDAT, jsonObject.optString(KEY_PUBLISHEDAT).toString());
                        for(int l=0;l<interests.size();l++) {
                            if (((jsonObject.optString(KEY_TITLE).toString().toLowerCase()).indexOf(interests.get(l)) != -1) || ((jsonObject.optString(KEY_DESCRIPTION).toString().toLowerCase()).indexOf(interests.get(l)) != -1)) {
                                dataList.add(map);
                            }
                        }
                        if(dataList.size()!=0){
                            loader.setVisibility(View.GONE);
                        }
                    }
                    if (dataList.size()==0){
                        Toast.makeText(getApplicationContext(),"No news found of interests",Toast.LENGTH_LONG);
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Unexpected error", Toast.LENGTH_SHORT).show();
                }

                layoutManager=new LinearLayoutManager(Newsfeed.this, LinearLayoutManager.VERTICAL,false);
                listNews.setHasFixedSize(true);
                listNews.setLayoutManager(layoutManager);
                adapter=new RecyclerAdapterfeed(dataList,Newsfeed.this);
                listNews.setAdapter(adapter);
              //  RecyclerAdapterfeed adapter = new RecyclerAdapterfeed(dataList,PatientHome.this);
                //listNews.setAdapter(adapter);

//                listNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    public void onItemClick(AdapterView<?> parent, View view,
//                                            int position, long id) {
//                        Intent i = new Intent(PatientHome.this, DetailsActivity.class);
//                        i.putExtra("url", dataList.get(+position).get(KEY_URL));
//                        startActivity(i);
//                    }
//                });


            } else {
                Toast.makeText(getApplicationContext(), "No news found", Toast.LENGTH_SHORT).show();
                loader.setVisibility(View.GONE);
            }


        }
    }
}





