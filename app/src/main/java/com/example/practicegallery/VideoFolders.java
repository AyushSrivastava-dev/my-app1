package com.example.practicegallery;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.trendyol.bubblescrollbarlib.BubbleScrollBar;
import com.trendyol.bubblescrollbarlib.BubbleTextProvider;

import java.util.ArrayList;
import java.util.Objects;

public class VideoFolders extends AppCompatActivity {
    private ArrayList<String> folderName;
    //    private ArrayList<String> firstImPath;
//    Parcelable state;
//    ListView listView;
    private RecyclerView imageRecycler;
    private ProgressBar progressBar;
    BubbleScrollBar bubbleScrollBarMain;
    private String textContent;
    private ArrayList<Image> allPictures;
    private ArrayList<VModelclass> videosArrayLists;
    private Toolbar toolbar3;
    private ImageView imageView;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_folders);
        Intent intenter = getIntent();
        Bundle bundle = intenter.getExtras();
        textContent = intenter.getStringExtra("currentSongs");
        folderName = new ArrayList<String>();
        imageRecycler = findViewById(R.id.image_Recycler1);
        progressBar = findViewById(R.id.recycler_progress1);
        bubbleScrollBarMain = findViewById(R.id.bubbleScrollBar1);
        bubbleScrollBarMain.attachToRecyclerView(imageRecycler);
        imageRecycler.setLayoutManager(new GridLayoutManager(this,3));
        imageRecycler.setHasFixedSize(true);
        if(ContextCompat.checkSelfPermission(Objects.requireNonNull(this), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},101);
        }

        ArrayList<Image> images = new ArrayList<Image>();
        videosArrayLists = new ArrayList<VModelclass>();
        Uri allImagesuri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.Video.Media.DATA ,MediaStore.Video.Media.DISPLAY_NAME,MediaStore.Video.Media.BUCKET_DISPLAY_NAME,MediaStore.Video.Media.DATE_TAKEN};
//        String orderBy = MediaStore.Images.ImageColumns.DATE_TAKEN;
        //     String sortOrder = MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC";
        String sortOrder = MediaStore.Video.VideoColumns.DATE_TAKEN + " DESC";

        Cursor cursor1 = this.getContentResolver().query(allImagesuri,
                projection,
                null,
                null,
                sortOrder);
        try{
            cursor1.moveToFirst();
            do{

                String aaa = cursor1.getString(cursor1.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME));
           //     if(a.equals(textContent)) {
//                    String b = cursor1.getString(cursor1.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
//                    String c = cursor1.getString(cursor1.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
//                Image image12 = new Image();
//                image12.imagePath = b;
//                image12.imageName = c;
//                image12.folder = a;
//                images.add(image12);
                if(textContent.equalsIgnoreCase(aaa)) {


                    VModelclass videos1 = new VModelclass();
                    videos1.folders = aaa;
                    videos1.videoPath = cursor1.getString(cursor1.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                    videos1.videoName = cursor1.getString(cursor1.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                    videosArrayLists.add(videos1);
//                    firstImPath.add(b);
                }


            }while(cursor1.moveToNext());
            cursor1.close();

        }catch (Exception e){
            e.printStackTrace();
        }
//        for(int i = 0; i<folderName.size(); i++){
//            count = 0;
//            for(int j = 0; j<images.size(); j++){
//                if(folderName.get(i).equals(images.get(j).folder)){
//                    count = count + 1;
//
//                }
//                if(count>0){
//                    videosArrayList.get(i).pic_counts = count;
//                }
//
//            }
//        }
        progressBar.setVisibility(View.VISIBLE);
        VideoFolderAdapter imageAdapters = new VideoFolderAdapter(this,videosArrayLists);
        bubbleScrollBarMain.setBubbleTextProvider(new BubbleTextProvider() {
            @Override
            public String provideBubbleText(int i) {
                return imageAdapters.videoList.get(i).videoName;
            }
        });

        imageRecycler.setAdapter(imageAdapters);

        progressBar.setVisibility(View.GONE);
        toolbar3 = findViewById(R.id.tool_bar);
//        toolbar1 = (Toolbar)findViewById(R.id.tool_bar);
        toolbar3.inflateMenu(R.menu.menu_item);
        toolbar3.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(item.getItemId()==R.id.sort_by)
                {
                    // do something
                }
                else if(item.getItemId()== R.id.new_album)
                {
                    // do something
                }
                else if(item.getItemId() == R.id.settings){
                    // do something
                }

                return false;
            }
        });
        imageView = findViewById(R.id.back_up);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



//        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        actionBar.setHomeButtonEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        final Drawable upArrow = getResources().getDrawable(R.drawable.back);
//        upArrow.setColorFilter(Color.parseColor("#FF000000"), PorterDuff.Mode.SRC_ATOP);
//        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }

//    private ArrayList<VModelclass> getVideosArrayLists()

}