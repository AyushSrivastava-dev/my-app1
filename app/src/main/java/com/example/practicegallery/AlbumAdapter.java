package com.example.practicegallery;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    private Context context;
    ArrayList<Albums> albumList;
    boolean control1 = true;
    public AlbumAdapter(Context context, ArrayList<Albums> albumList) {
        this.context = context;
        this.albumList = albumList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View albumView = layoutInflater.inflate(R.layout.row_album_recycler_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(albumView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Albums currentImage = albumList.get(position);
        String counterName = currentImage.folder +"("+ currentImage.pic_count+")";
        holder.textView.setText(counterName);
        RequestOptions cropOptions = new RequestOptions().centerCrop();
        Glide.with(context).load(currentImage.firstPic)
                .apply(cropOptions).thumbnail(0.1f).into(holder.imaging);
        holder.imaging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(control1) {
                    Intent intent = new Intent(context, Album_OpenActivity.class);
                    String currentSong = currentImage.folder.toString();
                    intent.putExtra("currentSong", currentSong);

                    Log.d("Ayush1", currentImage.folder);
                    context.startActivity(intent);
                }
                else{

                }
            }
        });
        holder.imaging.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {



                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imaging;
        public TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imaging = itemView.findViewById(R.id.imageView1);
            textView = itemView.findViewById(R.id.textView1);
        }
    }




}
