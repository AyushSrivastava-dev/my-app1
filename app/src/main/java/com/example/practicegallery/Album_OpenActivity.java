package com.example.practicegallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.trendyol.bubblescrollbarlib.BubbleScrollBar;
import com.trendyol.bubblescrollbarlib.BubbleTextProvider;

import java.util.ArrayList;

import static androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY;

public class Album_OpenActivity extends AppCompatActivity {
    private RecyclerView imageRecycler;
    ImageAdapter imageAdapter;
    private BubbleScrollBar bubbleScrollBar;
    private String textContent;
    private String choice = null;
    private String textContent2 = null;
    private ArrayList<Image> allPictures;
    private ImageView imageView;
    private Toolbar toolbar2;
    private TextView button1;
    private TextView button2;
    private RadioButton radioButton;
    private RadioButton radioButton2;
    private RadioGroup radioGroup;
    private RadioGroup radioGroup2;
    private String sortOrders = null;
    int selectedIdAlbum= -1;
    int selectedIdAlbum2 = -1;
    static final String SAVE_STATE_ALBUM = "state";
    static final String SELECTEDALBUM = "selectedId1";
    static final String SELECTEDALBUM2 = "selectedId2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            sortOrders = savedInstanceState.getString(SAVE_STATE_ALBUM);
            selectedIdAlbum = savedInstanceState.getInt(SELECTEDALBUM);
            selectedIdAlbum2 = savedInstanceState.getInt(SELECTEDALBUM2);
        }
        else{
        SharedPreferences sharedP = getSharedPreferences("MY_PREFRENCE",MODE_PRIVATE);
        choice = sharedP.getString(SAVE_STATE_ALBUM,null);
        if(choice != null){
           sortOrders = sharedP.getString(SAVE_STATE_ALBUM,null);
           selectedIdAlbum = sharedP.getInt(SELECTEDALBUM, -1);
           selectedIdAlbum2 = sharedP.getInt(SELECTEDALBUM2,-1);
        }}

        setContentView(R.layout.activity_album__open);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        textContent = intent.getStringExtra("currentSong");

        allPictures = getAllImages();

        imageRecycler = findViewById(R.id.image_Recycler);
        bubbleScrollBar = findViewById(R.id.bubbleScrollBars);
        bubbleScrollBar.attachToRecyclerView(imageRecycler);
        imageRecycler.setLayoutManager(new GridLayoutManager(this,3));
        imageRecycler.setHasFixedSize(true);


        toolbar2 = findViewById(R.id.tool_bar);

        toolbar2.inflateMenu(R.menu.menu_album);
        toolbar2.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(item.getItemId()==R.id.sort_by)
                {
                   showDialouge2();

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
         imageAdapter = new ImageAdapter(this, allPictures,textContent);
        imageRecycler.setAdapter(imageAdapter);


    }

    public void showDialouge2() {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView1 = LayoutInflater.from(Album_OpenActivity.this).inflate(R.layout.menu_sort_by, viewGroup, false);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(Album_OpenActivity.this);
        builder1.setView(dialogView1);
        AlertDialog alertDialog = builder1.create();
        alertDialog.show();

        if(selectedIdAlbum == -1) {
            radioButton = dialogView1.findViewById(R.id.radioDate_a);
            radioButton.setChecked(true);
        }
        else if(selectedIdAlbum != -1){
            radioButton = dialogView1.findViewById(selectedIdAlbum);
            radioButton.setChecked(true);
        }
        if(selectedIdAlbum2 == -1) {
            radioButton2 = dialogView1.findViewById(R.id.radioDescending_a);
            radioButton2.setChecked(true);
        }
        else if( selectedIdAlbum2 != -1){
            radioButton2 = dialogView1.findViewById(selectedIdAlbum2);
            radioButton2.setChecked(true);
        }

        button1 = dialogView1.findViewById(R.id.no_button_a);
        button2 = dialogView1.findViewById(R.id.ok_button_a);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioGroup = dialogView1.findViewById(R.id.radioGroup_a);
                radioGroup2 = dialogView1.findViewById(R.id.radioGroup2_a);
                selectedIdAlbum = radioGroup.getCheckedRadioButtonId();
                selectedIdAlbum2 = radioGroup2.getCheckedRadioButtonId();
                radioButton = dialogView1.findViewById(selectedIdAlbum);
                radioButton.setChecked(true);
                radioButton2 = dialogView1.findViewById(selectedIdAlbum2);
                radioButton2.setChecked(true);

                refresh(radioButton.getText().toString(),radioButton2.getText().toString());
                alertDialog.dismiss();

            }
        });
    }

    private void refresh(String toString, String toString1) {
        ArrayList<Image> images1 = new ArrayList<Image>();
        Uri allImagesuri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.Images.ImageColumns.DATA ,MediaStore.Images.Media.DISPLAY_NAME,MediaStore.Images.Media.BUCKET_DISPLAY_NAME,MediaStore.Images.Media.DATE_TAKEN};
        sortOrders = MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC";;
        if(toString1.equals("   Ascending")){

           if(toString.equals("  Name")){
               sortOrders = MediaStore.Images.ImageColumns.DISPLAY_NAME + " ASC";

           }
           else if(toString.equals("   Date")){
               sortOrders = MediaStore.Images.ImageColumns.DATE_TAKEN + " ASC";
           }
           else if(toString.equals("   Size")){
               sortOrders = MediaStore.Images.ImageColumns.SIZE + " ASC";
           }
       }
       else if(toString1.equals("   Descending")){
           if(toString.equals("  Name")){
               sortOrders = MediaStore.Images.ImageColumns.DISPLAY_NAME + " DESC";
           }
           else if(toString.equals("   Date")){
               sortOrders = MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC";


           }
           else if(toString.equals("   Size")){
               sortOrders = MediaStore.Images.ImageColumns.SIZE + " DESC";

           }
       }



        Cursor cursor = this.getContentResolver().query(allImagesuri, projection, null, null, sortOrders);
        try{
            cursor.moveToFirst();
            do{
                String displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                if(displayName.equals(textContent)) {
                    Image image = new Image();
                    image.imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                    image.imageName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                    image.folder = displayName;

                    images1.add(image);
                }
            }while(cursor.moveToNext()) ;
            cursor.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        imageAdapter = new ImageAdapter(this, images1,textContent);
        imageRecycler.setAdapter(imageAdapter);
        imageAdapter.notifyDataSetChanged();

    }

    private ArrayList<Image> getAllImages() {
        ArrayList<Image> images = new ArrayList<Image>();
        Uri allImagesuri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.Images.ImageColumns.DATA ,MediaStore.Images.Media.DISPLAY_NAME,MediaStore.Images.Media.BUCKET_DISPLAY_NAME,MediaStore.Images.Media.DATE_TAKEN};
        if(sortOrders == null){
            sortOrders = MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC";
        }

//        Log.d("Mo",MediaStore.Images.ImageColumns.DATE_TAKEN);
        Cursor cursor = this.getContentResolver().query(allImagesuri, projection, null, null, sortOrders);
        try{
            cursor.moveToFirst();
            do{
                String displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                if(displayName.equals(textContent)) {
                    Image image = new Image();
                    image.imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                    image.imageName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                    image.folder = displayName;

                    images.add(image);
                }
            }while(cursor.moveToNext()) ;
            cursor.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        return images;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(SAVE_STATE_ALBUM,sortOrders);
        outState.putInt(SELECTEDALBUM,selectedIdAlbum);
        outState.putInt(SELECTEDALBUM2,selectedIdAlbum2);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        SharedPreferences sharedP = getSharedPreferences("MY_PREFRENCE",MODE_PRIVATE);
        SharedPreferences.Editor editorA = sharedP.edit();
        editorA.putString(SAVE_STATE_ALBUM,sortOrders);
        editorA.putInt(SELECTEDALBUM,selectedIdAlbum);
        editorA.putInt(SELECTEDALBUM2,selectedIdAlbum2);
        editorA.putString("TexConten",textContent);
        editorA.apply();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        SharedPreferences sharedPreferences = getSharedPreferences("MYPREF",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SAVE_STATE_ALBUM,sortOrders);
        editor.commit();
        super.onPause();
    }

    @Override
    protected void onResume() {
        allPictures = getAllImages();
        imageAdapter = new ImageAdapter(this, allPictures,textContent);
        imageRecycler.setAdapter(imageAdapter);
        imageAdapter.notifyDataSetChanged();
        super.onResume();
    }

    public void backToPrevious(View view) {
        onBackPressed();
    }



}