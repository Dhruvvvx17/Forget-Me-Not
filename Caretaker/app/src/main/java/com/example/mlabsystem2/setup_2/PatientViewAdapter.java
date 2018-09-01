package com.example.mlabsystem2.setup_2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class PatientViewAdapter extends RecyclerView.Adapter<PatientViewAdapter.ViewHolder> {

    private ArrayList<String> patientnames = new ArrayList<>();
    private Context mContext;

    public PatientViewAdapter(ArrayList<String> patientnames, Context mContext) {
        PatientDb dbHelper = new PatientDb(mContext);
        this.patientnames = patientnames;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_listitem,parent,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(patientnames.get(position));

    }

    @Override
    public int getItemCount() {
        return patientnames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        EditText name;
        RelativeLayout patientlayout;


        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.patient_name);
            patientlayout = itemView.findViewById(R.id.patient_item);
        }


    }
}
