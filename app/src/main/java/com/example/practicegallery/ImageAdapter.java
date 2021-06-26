package com.example.practicegallery;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class  ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    ArrayList<Image> imagesList;
    String tContent;
    private Context context;
    private Vibrator myVib;
    private LinearLayout linearLayout1;
    private static final int TYPE_FULL = 0;
    private static final int TYPE_HALF = 1;
    private static final int TYPE_QUARTER = 2;
    public ImageAdapter(Context context, ArrayList<Image> imagesList ,String cont) {
        this.imagesList = imagesList;
        this.context = context;
        this.tContent = cont;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.row_custom_recycler_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Image currentImage = imagesList.get(position);
        RequestOptions cropOptions = new RequestOptions().centerCrop();
        Glide.with(context).load(currentImage.imagePath)
                .apply(cropOptions).transition(DrawableTransitionOptions.withCrossFade(800)).into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(context,ImageFullActivity.class);
                intent1.putExtra("folder",currentImage.folder);
                intent1.putExtra("path",currentImage.imagePath);
                intent1.putExtra("name",currentImage.imageName);
                intent1.putExtra("textCon",tContent);
                context.startActivity(intent1);

            }
        });


    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.row_image);
        }
    }

}
