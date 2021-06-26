//package com.example.practicegallery;
//
//public class VideosAdapter {
//}
package com.example.practicegallery;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import java.io.File;
import java.util.ArrayList;
import java.util.zip.Inflater;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> {
    private Context context;
    String vidoFolderName;
    ArrayList<VideosModelClass> videoList;
    public VideosAdapter(Context context, ArrayList<VideosModelClass> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View albumView = layoutInflater.inflate(R.layout.video_row_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(albumView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VideosModelClass currentImage = videoList.get(position);
//        vidoFolderName = currentImage.folderName;
        String counterName = currentImage.folderName +"("+ currentImage.pic_counts+")";
        holder.textView.setText(counterName);
        RequestOptions cropOptions = new RequestOptions().centerCrop();
        Glide.with(context).load(Uri.fromFile(new File(currentImage.firstPicPath)))       //load(currentImage.firstPicPath)
                .apply(cropOptions).into(holder.imaging);
        holder.imaging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, Intent.ACT);
//                String currentSong = currentImage.folderName.toString();
//                intent.putExtra("currentSong", currentSong);
//
//                Log.d("Ayush1",currentImage.folderName);
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.parse(currentImage.firstPicPath), "video/*");
//                context.startActivity(intent);
                Intent intent11 = new Intent(context, VideoFolders.class);
                String currentSong = currentImage.folderName.toString();
                intent11.putExtra("currentSongs", currentSong);
                context.startActivity(intent11);

            }
        });


    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imaging;
        public TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imaging = itemView.findViewById(R.id.imageViewVideo);
            textView = itemView.findViewById(R.id.textView1);
        }
    }




}
