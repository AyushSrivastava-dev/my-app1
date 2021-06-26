package com.example.practicegallery;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.util.ArrayList;

public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.ViewHolder>{
    ArrayList<ImageSliderModel> imageSliderModelArrayList;
    private Context context;
    ViewPager2 viewPager2;
    int pos;
//    boolean flag = true;

    public ImageSliderAdapter(ArrayList<ImageSliderModel> imageSliderModelArrayList,Context context, ViewPager2 viewPager2,int pos) {
        this.imageSliderModelArrayList = imageSliderModelArrayList;
        this.viewPager2 = viewPager2;
        this.context = context;
        this.pos = pos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context1 = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context1);
        View image_view = layoutInflater.inflate(R.layout.viewpager,parent,false);
        ViewHolder viewHolder = new ViewHolder(image_view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//            viewPager2.setCurrentItem(pos,true);
//            viewPager2.setAlpha(1);
            holder.setPhotoView(imageSliderModelArrayList.get(position).imagePather);



    }

    @Override
    public int getItemCount() {
        return imageSliderModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        SubsamplingScaleImageView photoView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            photoView = itemView.findViewById(R.id.photo_view);
        }


        public void setPhotoView(String imagePath){
                photoView.setMaxScale(10f);
                photoView.setImage(ImageSource.uri(imagePath));


        }
    }
}