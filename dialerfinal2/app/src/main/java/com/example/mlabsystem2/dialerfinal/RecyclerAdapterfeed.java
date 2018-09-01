package com.example.mlabsystem2.dialerfinal;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;


import static com.example.mlabsystem2.dialerfinal.Newsfeed.KEY_URL;

public class RecyclerAdapterfeed extends RecyclerView.Adapter<RecyclerAdapterfeed.ImageViewHolder> {
    private ArrayList<HashMap<String, String>> data;
    Context ctx;
    public RecyclerAdapterfeed(ArrayList<HashMap<String, String>> data,Context ctx){
        this.ctx=ctx;
        this.data=data;
    }
    @Override
    public RecyclerAdapterfeed.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        ImageViewHolder imageViewHolder = new ImageViewHolder(view, ctx,data);
        return imageViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapterfeed.ImageViewHolder holder, int position) {
        holder.description.setId(position);
        holder.author1.setId(position);
        holder.title1.setId(position);
        holder.photo.setId(position);
        holder.time.setId(position);
        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);
        try{
            holder.author1.setText(song.get(Newsfeed.KEY_AUTHOR));
            holder.title1.setText(song.get(Newsfeed.KEY_TITLE));
            holder.time.setText(song.get(Newsfeed.KEY_PUBLISHEDAT));
            holder.description.setText(song.get(Newsfeed.KEY_DESCRIPTION));

            if(song.get(Newsfeed.KEY_URLTOIMAGE).toString().length() < 5)
            {
                holder.photo.setVisibility(View.GONE);
            }else{
                Picasso.with(ctx)
                        .load(song.get(Newsfeed.KEY_URLTOIMAGE).toString())
                        .resize(300, 200)
                        .into(holder.photo);
            }
        }catch(Exception e) {}
    }

    @Override
    public int getItemCount() {

        return data.size();

    }
    public static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title1;
        TextView description;
        TextView time;
        TextView author1;
        ImageView photo;
        ArrayList<HashMap<String, String>> data;
        Context ctx;

        public ImageViewHolder(View itemView, Context ctx, ArrayList<HashMap<String, String>> data1){
            super(itemView);
            this.data=data1;

            this.ctx = ctx;

            itemView.setOnClickListener(this);
            title1 = (TextView)itemView.findViewById(R.id.title);
            description =(TextView) itemView.findViewById(R.id.sdetails);
            time=(TextView) itemView.findViewById(R.id.time);
            author1=(TextView) itemView.findViewById(R.id.author);
            photo=(ImageView)itemView.findViewById(R.id.galleryImage);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

          //  HashMap<String, String> num_id = this.data.get(position);
            Intent i = new Intent(this.ctx, DetailsActivity.class);
                        i.putExtra("url", data.get(+position).get(KEY_URL));
                        this.ctx.startActivity(i);;

        }
    }
}
