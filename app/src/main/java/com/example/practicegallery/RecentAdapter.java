package com.example.practicegallery;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.ScheduledExecutorService;


public class RecentAdapter extends  RecyclerView.Adapter<RecentAdapter.ViewHolder>{

    ArrayList<Image> imagesList;
    private ClickInterface clickInterface;
    private Context context;
    ViewHolder holders;
    ArrayList<ViewHolder> viewHolders = new ArrayList<ViewHolder>();
    public RecentAdapter(Context context, ArrayList<Image> imagesList, ClickInterface clickInterface) {
        this.imagesList = imagesList;
        this.context = context;
        this.clickInterface = clickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.recent_items, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        viewHolders.add(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holders = holder;
        holder.bind(imagesList.get(position));

    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView image;
        public ImageView checkImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.row_image_recent);
             checkImage = itemView.findViewById(R.id.check_sign);

        }
        void bind(final Image image0){
            checkImage.setVisibility(image0.isChecked() ?View.VISIBLE: View.GONE);
            RequestOptions cropOptions = new RequestOptions().centerCrop();

             Glide.with(context).load(image0.imagePath)
                .apply(cropOptions).transition(DrawableTransitionOptions.withCrossFade(800)).into(image);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
//                    image0.setChecked(!image0.isChecked());
//                    checkImage.setVisibility(image0.isChecked() ? View.VISIBLE: View.GONE);
                    clickInterface.onLongItemClick(holders.getAbsoluteAdapterPosition(),checkImage,image0);

                    return true;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickInterface.onItemClick(holders.getAbsoluteAdapterPosition(),checkImage,image0);
                }
            });
        }
    }

}

