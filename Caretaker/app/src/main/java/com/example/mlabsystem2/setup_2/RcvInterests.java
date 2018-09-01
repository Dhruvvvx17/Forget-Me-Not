package com.example.mlabsystem2.setup_2;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RcvInterests extends RecyclerView.Adapter<RcvInterests.ViewHolder> {
    @NonNull
    ArrayList<String> interests;
    FirebaseFirestore db;
    String patient_uid;
    Context mContext;

    public RcvInterests(ArrayList<String> interests, Context mContext, String patient_uid) {
        this.interests = interests;
        this.mContext = mContext;
        this.patient_uid = patient_uid;

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
    }

    @Override
    public RcvInterests.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_interest, parent, false);
        RcvInterests.ViewHolder vh = new RcvInterests.ViewHolder(view, mContext);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.interest1.setText(interests.get(position));

        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interests.remove(holder.getAdapterPosition());

                Map<String, Object> interestsMap = new HashMap<>();
                interestsMap.put("Interests", interests);

                db.collection("Patients")
                        .document(patient_uid)
                        .update(interestsMap);

            }
        });

    }


    @Override
    public int getItemCount() {
        return interests.size();
    }

    public void setItems(ArrayList<String> interests) {
        this.interests = interests;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        Context ctx;
        TextView interest1;
        ImageButton Delete;

        public ViewHolder(View itemView, Context ctx1) {
            super(itemView);
            this.ctx = ctx1;

            interest1 = (TextView) itemView.findViewById(R.id.interest_name);
            Delete = (ImageButton) itemView.findViewById(R.id.delete);

        }

    }
}


