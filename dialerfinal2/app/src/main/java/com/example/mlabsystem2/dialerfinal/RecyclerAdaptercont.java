package com.example.mlabsystem2.dialerfinal;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerAdaptercont extends RecyclerView.Adapter<RecyclerAdaptercont.ImageViewHolder> {
    ArrayList<String> contacts=new ArrayList<String>();
    ArrayList<String> numbers=new ArrayList<String>();
    Context ctx;
    Cursor cursor;
    Cursor cursor1;

    public RecyclerAdaptercont(ArrayList<String>contacts, ArrayList<String>numbers, Cursor cursor, Context ctx) {
        this.contacts = contacts;
        this.numbers= numbers;
        this.cursor=cursor;
        this.ctx = ctx;


    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photolayout, parent, false);
        ImageViewHolder imageViewHolder = new ImageViewHolder(view, ctx,contacts,numbers);
        return imageViewHolder;
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        String contact = contacts.get(position);
        String number= numbers.get(position);
        holder.contact1.setText(contact);
        holder.number1.setText(number);
    }

    @Override
    public int getItemCount() {
        return numbers.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView contact1;
        TextView number1;
        ArrayList<String>contact;
        ArrayList<String>number;
        Context ctx;

        public ImageViewHolder(View itemView, Context ctx, ArrayList<String>contacts,ArrayList<String>numbers) {
            super(itemView);
            this.contact = contacts;
            this.number=numbers;
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
