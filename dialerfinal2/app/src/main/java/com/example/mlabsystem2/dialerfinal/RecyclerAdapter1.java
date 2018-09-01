package com.example.mlabsystem2.dialerfinal;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerAdapter1 extends RecyclerView.Adapter<RecyclerAdapter1.ImageViewHolder> {
    ArrayList<String> durations = new ArrayList<String>();
    ArrayList<String> numbers = new ArrayList<String>();
    Context ctx;

    public RecyclerAdapter1(ArrayList<String> durations, ArrayList<String> numbers, Context ctx) {
        this.durations = durations;
        this.numbers = numbers;
        this.ctx = ctx;

    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photolayout, parent, false);
        ImageViewHolder imageViewHolder = new ImageViewHolder(view, ctx,durations, numbers);
        return imageViewHolder;
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        String duration = durations.get(position);
        String number = numbers.get(position);
        holder.contact1.setText(duration);
        holder.number1.setText(number);
    }

    @Override
    public int getItemCount() {
        return numbers.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView contact1;
        TextView number1;
        ArrayList<String> duration;
        ArrayList<String> number;
        Context ctx;

        public ImageViewHolder(View itemView, Context ctx, ArrayList<String> durations, ArrayList<String> numbers) {
            super(itemView);
            this.duration = durations;
            this.number = numbers;
            this.ctx = ctx;

            itemView.setOnClickListener(this);
            contact1 = itemView.findViewById(R.id.contact);
            number1 = itemView.findViewById(R.id.number);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            String num_id = this.number.get(position);
            Intent call=new Intent(this.ctx,Dialerfinal.class);
            call.putExtra("number",num_id);
            this.ctx.startActivity(call);


        }
    }
}
