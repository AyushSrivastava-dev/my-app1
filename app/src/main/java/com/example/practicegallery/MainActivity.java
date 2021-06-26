package com.example.practicegallery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> folderName;
    private ArrayList<Integer> folderCount;
    private AppBarLayout appBarLayout;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    ImageView topImage;
    FragmentAdapter fragmentAdapter;
    private Toolbar toolbar1;
    private String sortOrders = null;
    boolean f;
    private int count;
    ArrayList<Albums> albumlist;
    ArrayList<Image> imagesList;
    private TextView countText;
    Bundle savedI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO);
        savedI = savedInstanceState;
        folderName = new ArrayList<String>();
        folderCount = new ArrayList<Integer>();
        f = true;

        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(this), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);

            if (ContextCompat.checkSelfPermission(Objects.requireNonNull(this), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 102);
            }
        }

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);


        appBarLayout = findViewById(R.id.appbar);
        countText = findViewById(R.id.number_i);
        topImage = findViewById(R.id.image_top);
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(this), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            albumlist = imageAlbums();


            if (imagesList.isEmpty()) {
                Toast.makeText(this, "No media to show", Toast.LENGTH_SHORT).show();
            } else {
                Glide.with(MainActivity.this).load(imagesList.get(0).imagePath).centerCrop().apply(RequestOptions.bitmapTransform(new BlurTransformation(2, 3)))
                        .transition(DrawableTransitionOptions.withCrossFade(800)).into(topImage);
                String showText = "Photos - " + imagesList.size();
                countText.setText(showText);
            }

            tabLayout = findViewById(R.id.tab_layout);
            viewPager2 = findViewById(R.id.view_pager);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentAdapter = new FragmentAdapter(fragmentManager, getLifecycle());


            viewPager2.setAdapter(fragmentAdapter);
            tabLayout.addTab(tabLayout.newTab().setText("Recent"));
            tabLayout.addTab(tabLayout.newTab().setText("Photos"));
            tabLayout.addTab(tabLayout.newTab().setText("Videos"));
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager2.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    tabLayout.selectTab(tabLayout.getTabAt(position));
                }
            });

            toolbar1 = (Toolbar) findViewById(R.id.tool_bar);

            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
                        topImage.setVisibility(View.GONE);
                        tabLayout.setBackgroundColor(getResources().getColor(R.color.black));
                        getWindow().setStatusBarColor(Color.BLACK);


                    } else {
                        topImage.setVisibility(View.VISIBLE);
                        tabLayout.setBackgroundColor(getResources().getColor(R.color.transparent));
                        getWindow().setStatusBarColor(Color.TRANSPARENT);


                    }
                }
            });
        }

    }





    private ArrayList<Albums> imageAlbums() {
        int c = 0;
        imagesList = new ArrayList<Image>();
        ArrayList<Albums> albumsArrayList = new ArrayList<Albums>();
        Uri allImagesuri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATE_TAKEN};
        if (sortOrders == null) {
            sortOrders = MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC";
        }
        Log.d("wallhm", sortOrders);

        Cursor cursor1 = this.getContentResolver().query(allImagesuri,
                projection,
                null,
                null,
                sortOrders);

        try {
            Log.d("folderna", "something here try1");
            cursor1.moveToFirst();
            do {
                Log.d("foldernamingtag", "something here do");
                String a = cursor1.getString(cursor1.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                String b = cursor1.getString(cursor1.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                File f = new File(b);
                if (f.exists()) {
                    Image image1 = new Image();
                    image1.imagePath = b;
                    image1.imageName = cursor1.getString(cursor1.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                    image1.folder = a;
                    Calendar myCal = Calendar.getInstance();
                    myCal.setTimeInMillis(cursor1.getLong(cursor1.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)));
                    Date dateText = new Date(myCal.get(Calendar.YEAR) - 1900,
                            myCal.get(Calendar.MONTH),
                            myCal.get(Calendar.DAY_OF_MONTH),
                            myCal.get(Calendar.HOUR_OF_DAY),
                            myCal.get(Calendar.MINUTE));


                    image1.imageDate = (android.text.format.DateFormat.format("dd/MM/yy", dateText)).toString();

                    imagesList.add(image1);
                }
                if (!folderName.contains(a)) {
                    Albums albums1 = new Albums();
                    albums1.folder = a;
                    albums1.firstPic = b;
                    albumsArrayList.add(albums1);
                    folderName.add(a);
                }


            } while (cursor1.moveToNext());
            cursor1.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


        for (int i = 0; i < folderName.size(); i++) {
            count = 0;
            for (int j = 0; j < imagesList.size(); j++) {
                if (folderName.get(i).equals(imagesList.get(j).folder)) {
                    count = count + 1;

                }
                if (count > 0) {
                    folderCount.add(count);
                    albumsArrayList.get(i).pic_count = count;
                }

            }
        }

        return albumsArrayList;

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "yes granted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                this.finish();

            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}