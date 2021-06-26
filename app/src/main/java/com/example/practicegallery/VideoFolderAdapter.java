//package com.example.practicegallery;
//
//public class VideoFolderAdapter {
//}
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

public class VideoFolderAdapter extends RecyclerView.Adapter<VideoFolderAdapter.ViewHolder> {
    private Context context;
    ArrayList<VModelclass> videoList;
    public VideoFolderAdapter(Context context, ArrayList<VModelclass> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View albumView = layoutInflater.inflate(R.layout.vid_row_maker,parent,false);
        ViewHolder viewHolder = new ViewHolder(albumView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VModelclass currentImage = videoList.get(position);
        String counterName = currentImage.videoName;
        holder.textView.setText(counterName);

        RequestOptions cropOptions = new RequestOptions().centerCrop();
        Glide.with(context).load(Uri.fromFile(new File(currentImage.videoPath)))       //load(currentImage.firstPicPath)
                .apply(cropOptions).into(holder.imaging);
        holder.imaging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, Intent.ACT);
//                String currentSong = currentImage.folderName.toString();
//                intent.putExtra("currentSong", currentSong);
//
//                Log.d("Ayush1",currentImage.folderName);
                Intent intentss = new Intent(Intent.ACTION_VIEW);
                intentss.setDataAndType(Uri.parse(currentImage.videoPath), "video/*");
                context.startActivity(intentss);

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
            imaging = itemView.findViewById(R.id.imageView1);
            textView = itemView.findViewById(R.id.textView1);
        }
    }




}

