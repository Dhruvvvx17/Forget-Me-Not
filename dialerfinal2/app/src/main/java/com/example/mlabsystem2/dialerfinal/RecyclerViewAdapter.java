package com.example.mlabsystem2.dialerfinal;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    public ArrayList<String> taskNames = new ArrayList<>(), taskDates = new ArrayList<>(), taskTimes = new ArrayList<>(), taskFID = new ArrayList<>();
    private Context mContext;

    private FirebaseFirestore db;

    public RecyclerViewAdapter( ArrayList<String> taskFID, Context mContext) {

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        setItems(taskNames,taskDates,taskTimes,taskFID);

        this.mContext = mContext;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem,parent,false);
        ViewHolder vh = new ViewHolder(view,mContext);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.taskname.setText(taskNames.get(position));
        holder.date.setText(taskDates.get(position));
        holder.time.setText(taskTimes.get(position));
        holder.taskId = taskFID.get(position);

    }
    public void setItems(ArrayList<String> taskNames, ArrayList<String> taskDates, ArrayList<String> taskTimes, ArrayList<String> taskFID) {
        this.taskNames = taskNames;
        this.taskDates = taskDates;
        this.taskTimes = taskTimes;
        this.taskFID = taskFID;
    }


    @Override
    public int getItemCount() {
        return taskNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView image;
        TextView taskname;
        TextView date;
        TextView time;
        String taskId;
        Context ctx;

        RelativeLayout parentlayout;

        public ViewHolder(View itemView,Context ctx1) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            taskname = itemView.findViewById(R.id.task_name);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            this.ctx=ctx1;

            parentlayout = itemView.findViewById(R.id.parent_layout);
            int pos = getAdapterPosition();
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            Intent taskI = new Intent(this.ctx, taskDescription.class);
            taskI.putExtra("id", taskId);
            this.ctx.startActivity(taskI);
        }
    }
}
