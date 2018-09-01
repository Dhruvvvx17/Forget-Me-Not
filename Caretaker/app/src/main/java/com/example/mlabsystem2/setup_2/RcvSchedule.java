package com.example.mlabsystem2.setup_2;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class RcvSchedule extends RecyclerView.Adapter<RcvSchedule.ViewHolder> {

    public ArrayList<String> taskFID = new ArrayList<>();
    private Context mContext;
    private RcvSchedule adapter = this;

    private FirebaseFirestore db;

    public RcvSchedule(ArrayList<String> taskFID, Context mContext) {

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        setItems(taskFID);

        this.mContext = mContext;
    }

    private void deleteTask(String firebase_id) {

        Log.d("MEEEE", "firebase id" + firebase_id + "...");
        db.collection("Tasks").document(firebase_id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("MEEEEEEEEEEEEE", "DocumentSnapshot successfully deleted!");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("MEEEEEEEEEEEEE", "Error deleting document", e);
                    }
                });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_task, parent, false);
        ViewHolder vh = new ViewHolder(view, mContext);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.taskId = taskFID.get(holder.getAdapterPosition());

        Source source = Source.CACHE;
        DocumentReference docRef = db.collection("Tasks").document(holder.taskId);
        docRef.get(source).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Log.d("MEEE", "Display: " + document.getData());
                        holder.taskname.setText(document.getString("Task"));
                        holder.date.setText(document.getString("Date"));
                        holder.time.setText(document.getString("Time"));
//                        holder.description.setText(document.getString("Description"));

                    } else {
                        Log.d("MEEError", "No such document");
                    }
                } else {
                    Log.d("MEEEError", "get failed with ", task.getException());
                }
            }
        });


        //Task Deletion
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteTask(taskFID.get(position));
                taskFID.remove(position);

                setItems(taskFID);
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void setItems(ArrayList<String> taskFID) {
        this.taskFID = taskFID;
    }

    @Override
    public int getItemCount() {
        return taskFID.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView image;
        TextView taskname;
        TextView description;
        TextView date;
        TextView time;
        ImageButton button;
        String taskId;
        LinearLayout parentlayout;
        Context ctx;

        public ViewHolder(View itemView, Context ctx1) {
            super(itemView);
            this.ctx = ctx1;

            image = itemView.findViewById(R.id.image);
            taskname = itemView.findViewById(R.id.task_name);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            button = itemView.findViewById(R.id.delete);
            parentlayout = itemView.findViewById(R.id.parent_layout);

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
