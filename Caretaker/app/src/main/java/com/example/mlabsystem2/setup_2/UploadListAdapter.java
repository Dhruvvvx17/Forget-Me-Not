package com.example.mlabsystem2.setup_2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class UploadListAdapter extends RecyclerView.Adapter<UploadListAdapter.ViewHolder> {

    public ArrayList<String> Filelist = new ArrayList<>();
    private Context mContext;

    public UploadListAdapter(ArrayList<String> Filelist, Context mContext) {
        this.mContext = mContext;
        this.Filelist = Filelist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final String path = Filelist.get(position);
      //  File f = new File(path);
        Bitmap b = BitmapFactory.decodeFile(path);
        holder.image.setImageBitmap(b);
        holder.labelcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filelist.remove(position);
                notifyDataSetChanged();
                notifyItemRangeChanged(position, Filelist.size());  //REDUNDANT I THINK
            }
        });
        holder.labelupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String labelname = holder.label.getText().toString();
                MainActivity_aws1 main = new MainActivity_aws1();
                main.FaceUpload(mContext,labelname,path);
                Filelist.remove(position);
                notifyDataSetChanged();
                notifyItemRangeChanged(position, Filelist.size());

            }
        });

    }

    @Override
    public int getItemCount() {
        return Filelist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        EditText label;
        Button labelupload;
        Button labelcancel;

        public ViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.img_file);
            label = itemView.findViewById(R.id.label);
            labelupload = itemView.findViewById(R.id.labelupload);
            labelcancel = itemView.findViewById(R.id.labelcancel);
        }
    }

 //Not needed
      public Bitmap decodeFile(File f) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE = 70;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }
}
