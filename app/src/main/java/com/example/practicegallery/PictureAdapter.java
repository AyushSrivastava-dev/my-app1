package com.example.practicegallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.util.ArrayList;

public class PictureAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<ImageSliderModel> list;
    private Activity activity;
    private boolean flag = true;
    private Toolbar toolbar1;
    private Toolbar toolbar2;
    private ImageView rotate;
    private SubsamplingScaleImageView imageView;
    private boolean flagImage;
    private ViewPager viewPager;
    SharedPreferences shareImagePref;
    SharedPreferences.Editor editorPictures;
    String path;
    String text;
    String folderN;
    String imageN;
    int orient;
    public PictureAdapter(Context context,ArrayList<ImageSliderModel> list,Activity activity,Toolbar t1, Toolbar t2,ImageView rotate,ViewPager viewPager,String path,String text,String imageN,String folderN) {
        this.context = context;
        this.list = list;
        this.activity = activity;
        this.toolbar1 = t1;
        this.toolbar2 = t2;
        this.rotate = rotate;
        this.viewPager = viewPager;
        this.path = path;
        this.text = text;
        this.imageN = imageN;
        this.folderN = folderN;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((ConstraintLayout)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        ImageView anotherImage;
        String filePathName = list.get(position).imagePather.substring(list.get(position).imagePather.lastIndexOf(".") + 1);
        LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.viewpager ,container,false);
        anotherImage = viewLayout.findViewById(R.id.anotherImage);
        imageView = viewLayout.findViewById(R.id.photo_view);

        ((ViewPager)container).addView(viewLayout);
        SharedPreferences sharingTouch = activity.getSharedPreferences("keyis1",Context.MODE_PRIVATE);
        SharedPreferences.Editor editorPicture = sharingTouch.edit();
        editorPicture.putBoolean("keyNot1",false);
        editorPicture.apply();
            Log.d("gifOrNot", filePathName);
         shareImagePref = activity.getSharedPreferences("ImageOr",Context.MODE_PRIVATE);
        flagImage = shareImagePref.getBoolean("ImageOrientation",false);
            anotherImage.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            imageView.setMaxScale(10f);
            imageView.setImage(ImageSource.uri(list.get(position).imagePather));
        if(flagImage){
            imageView.setOrientation((shareImagePref.getInt("positionI",90))%360);
            orient = shareImagePref.getInt("positionI",90);
            SharedPreferences.Editor editorPictures = shareImagePref.edit();
            editorPictures.putBoolean("ImageOrientation", false);
            editorPictures.apply();

        }

        imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sharingTouch = activity.getSharedPreferences("keyis1", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editorPicture = sharingTouch.edit();
                    editorPicture.putBoolean("keyNot1", true);
                    editorPicture.apply();
                    if (flag) {
//                    View decorView = activity.getWindow().getDecorView();
//                    // Hide the status bar.
//                    int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//                    decorView.setSystemUiVisibility(uiOptions);


                        toolbar1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                toolbar1.setVisibility(View.GONE);
                            }
                        }, 250);
                        toolbar2.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                toolbar2.setVisibility(View.GONE);
                            }
                        }, 250);

                        flag = false;


                    } else  {
                        toolbar1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                toolbar1.setVisibility(View.VISIBLE);
                            }
                        }, 250);

                        toolbar2.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                toolbar2.setVisibility(View.VISIBLE);
                            }
                        }, 250);
                        flag = true;


                    }
                }
            });

         if(filePathName.equals("giphy") || filePathName.equals("gif")){
            Log.d("gifOrNotY", filePathName);
            anotherImage.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            RequestOptions cropOptions = new RequestOptions().centerCrop();
            Glide.with(context).asGif().load(list.get(position).imagePather).apply(cropOptions).into(anotherImage);
            anotherImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sharingTouch = activity.getSharedPreferences("keyis1", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editorPicture = sharingTouch.edit();
                    editorPicture.putBoolean("keyNot1", true);
                    editorPicture.apply();
                    if (flag) {
//                    View decorView = activity.getWindow().getDecorView();
//                    // Hide the status bar.
//                    int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//                    decorView.setSystemUiVisibility(uiOptions);


                        toolbar1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                toolbar1.setVisibility(View.GONE);
                            }
                        }, 250);
                        toolbar2.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                toolbar2.setVisibility(View.GONE);
                            }
                        }, 250);

                        flag = false;


                    } else {
                        toolbar1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                toolbar1.setVisibility(View.VISIBLE);
                            }
                        }, 250);

                        toolbar2.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                toolbar2.setVisibility(View.VISIBLE);
                            }
                        }, 250);
                        flag = true;


                    }
                }
            });
        }
         rotate.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 shareImagePref = activity.getSharedPreferences("ImageOr",Context.MODE_PRIVATE);
                 editorPictures = shareImagePref.edit();
                 editorPictures.putBoolean("ImageOrientation", true);
                 editorPictures.putInt("positionI",orient+90);
                 Log.d("orientation"," "+orient);
                 editorPictures.apply();
                 Intent intent = new Intent(activity,ImageFullActivity.class);

                 intent.putExtra("folder",folderN);
                 intent.putExtra("path",list.get(position-1).imagePather);
                 intent.putExtra("name",list.get(position-1).imageNamer);
                 intent.putExtra("textCon",text);
                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                 activity.startActivity(intent);
                 activity.finish();


             }
         });
        return viewLayout;


    }



    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager)container).removeView((ConstraintLayout)object);
    }

}
