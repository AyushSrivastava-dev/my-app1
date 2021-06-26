package com.example.practicegallery;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.documentfile.provider.DocumentFile;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.RecoverableSecurityException;
import android.app.WallpaperManager;
import android.content.ClipData;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextThemeWrapper;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.ViewPropertyTransition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.example.practicegallery.ImageSliderModel;
import com.github.chrisbanes.photoview.OnMatrixChangedListener;
import com.github.chrisbanes.photoview.OnScaleChangedListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.google.android.material.bottomappbar.BottomAppBar;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.practicegallery.R.color.black;
import static com.example.practicegallery.R.color.red_background;
import static java.security.AccessController.getContext;
import androidx.core.content.FileProvider;

public class ImageFullActivity extends AppCompatActivity {
    private String imagePath;
    private float cumulScaleFactor = 1;
    private Toolbar toolbar4;
    private TextView textView;
    private ImageView imageView;
    private Toolbar toolbar5;
    long periodMs;
    ImageView rotate;
    ImageView startview;
    ImageView editImage;
    private int positions;
    ViewPager2 viewPager;
    ViewPager viewPager1;
    ArrayList<ImageSliderModel> sliders;
    static final String SAVE_STATE = "state";
    Timer timer;
    boolean check = false;
    TextView buttonSlide1;
    TextView buttonSlide2;
    private RadioGroup radioGroupSlide;
    private RadioButton radioButtonSlide;
    private ImageView cancelImage;
    ArrayList<Uri> uriArrayList;
    Uri deleteUri;
    int activeClass;
    String textC;
    File file;
    TextView txtName;
    TextView txtPath;
    TextView txtDate;
    TextView txtSize;
    private RadioGroup radioGroup1;
    private TextView button1;
    private TextView button2;
    private int selectedIdTrans = 0;
    private RadioButton radioButtonTrans;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full);
        Intent intent = getIntent();
        imagePath = intent.getStringExtra("path");
        textC = intent.getStringExtra("textCon");
        String imageN = intent.getStringExtra("name");
        String imageName = intent.getStringExtra("name").replace(".jpg", "");
        viewPager = findViewById(R.id.viewpPagersImage);
        viewPager1 = findViewById(R.id.view_pager);
        cancelImage = findViewById(R.id.image_cancel);
        rotate = findViewById(R.id.rotate_view);
        String folderName = intent.getStringExtra("folder");
        sliders = new ArrayList<ImageSliderModel>();

        Uri allImagesuri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATE_TAKEN, MediaStore.Images.Media._ID,MediaStore.Images.Media.SIZE};

        String sortOrders = MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC";
        SharedPreferences sharedPreferences = getSharedPreferences("MYPREF", MODE_PRIVATE);
        if (sharedPreferences.contains(SAVE_STATE)) {
            sortOrders = sharedPreferences.getString(SAVE_STATE, null);

        }

        Cursor cursor = this.getContentResolver().query(allImagesuri, projection, null, null, sortOrders);
        try {
            cursor.moveToFirst();
            do {
                String displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                if (displayName.equals(folderName)) {
                    activeClass = 2;
                    ImageSliderModel imageSliderModel = new ImageSliderModel();
                    imageSliderModel.imageNamer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                    imageSliderModel.imagePather = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                    imageSliderModel.id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                    Double sizeImages = cursor.getDouble(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE))/1000000;
                    DecimalFormat df = new DecimalFormat();
                    df.setMaximumFractionDigits(2);
                    imageSliderModel.imageSize = df.format(sizeImages)+" MB";
                    Calendar myCal = Calendar.getInstance();
                    myCal.setTimeInMillis(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)));
                    Date dateText = new Date(myCal.get(Calendar.YEAR)-1900,
                            myCal.get(Calendar.MONTH),
                            myCal.get(Calendar.DAY_OF_MONTH),
                            myCal.get(Calendar.HOUR_OF_DAY),
                            myCal.get(Calendar.MINUTE));
                    imageSliderModel.imageDate = (android.text.format.DateFormat.format("dd/MM/yy", dateText)).toString();


                    sliders.add(imageSliderModel);
                } else if (folderName.isEmpty()) {
                    activeClass = 1;
                    ImageSliderModel imageSliderModel = new ImageSliderModel();
                    imageSliderModel.imageNamer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                    imageSliderModel.imagePather = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                    imageSliderModel.id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                    Double sizeImages = cursor.getDouble(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE))/1000000;
                    DecimalFormat df = new DecimalFormat();
                    df.setMaximumFractionDigits(2);
                    imageSliderModel.imageSize = df.format(sizeImages)+" MB";
                    Calendar myCal = Calendar.getInstance();
                    myCal.setTimeInMillis(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)));
                    Date dateText = new Date(myCal.get(Calendar.YEAR)-1900,
                            myCal.get(Calendar.MONTH),
                            myCal.get(Calendar.DAY_OF_MONTH),
                            myCal.get(Calendar.HOUR_OF_DAY),
                            myCal.get(Calendar.MINUTE));
                    imageSliderModel.imageDate = (android.text.format.DateFormat.format("dd/MM/yy", dateText)).toString();

                    sliders.add(imageSliderModel);

                }
            } while (cursor.moveToNext());
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < sliders.size(); i++) {
            if (imagePath.equals(sliders.get(i).imagePather)) {
                positions = i;
            }
        }


        textView = (TextView) findViewById(R.id.titleOfTool);

        textView.setText(imageName);


        toolbar4 = findViewById(R.id.tool_bar);
        toolbar5 = findViewById(R.id.tool_bar2);
        toolbar5.inflateMenu(R.menu.image_menu);
        toolbar5.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.slide_show) {
                    slideshow();
                    return true;
                }
                if (item.getItemId() == R.id.wallpaper) {
                    setImageWallpaper();
                    return true;

                }
                if(item.getItemId() == R.id.details){
                    detailsImage();
                    return true;
                }
                if(item.getItemId() == R.id.transition){
                    transitionGetter();
                    return true;
                }
                return false;
            }

        });
        startview = findViewById(R.id.star_view);


        imageView = findViewById(R.id.back_up);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        cancelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ImageFullActivity.this, "Slideshow stopped", Toast.LENGTH_SHORT).show();
                Log.d("check value", "Second " + check);
                cancelImage.setVisibility(View.GONE);
                timer.cancel();
                toolbar4.setVisibility(View.VISIBLE);
                toolbar5.setVisibility(View.VISIBLE);
            }
        });


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        viewPager.setPageTransformer(new ZoomOutPageTransformer());
        SharedPreferences sharedPrefTrans = getSharedPreferences("TransitionId",MODE_PRIVATE);
        String transString = sharedPrefTrans.getString("SelectedTrans",null);
        if(transString != null) {
            if(transString.equals("   Sink")){
                viewPager1.setPageTransformer(true,new DepthPageTransformer());
            }
            if(transString.equals("   Zoom")){
                viewPager1.setPageTransformer(false,new ZoomOutPageTransformer());
            }
            if(transString.equals("   Spin")){
                viewPager1.setPageTransformer(false,new SpinnerTransformation());
            }
            if(transString.equals("   Cube_in")){
                viewPager1.setPageTransformer(false,new CubeInRotationTransformation());
            }
            if(transString.equals("   Cube_out")){
                viewPager1.setPageTransformer(false,new CubeOutRotationTransformation());
            }
            if(transString.equals("   Hinge")){
                viewPager1.setPageTransformer(false,new HingeTransformation());
            }
            if(transString.equals("   Fade")){
                viewPager1.setPageTransformer(false,new FadeOutTransformation());
            }
        }

        viewPager1.setAdapter(new PictureAdapter(getApplicationContext(), sliders, ImageFullActivity.this, toolbar5, toolbar4,rotate,viewPager1,imagePath,textC,imageN,folderName));
        viewPager1.setCurrentItem(positions, true);
        viewPager1.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                positions = position;
                textView.setText(sliders.get(position).imageNamer);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        editImage = findViewById(R.id.edit_view1);
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dsPhotoEditorIntent = new Intent(ImageFullActivity.this, DsPhotoEditorActivity.class);
                Uri inputImageUri = Uri.fromFile(new File(sliders.get(positions).imagePather));
                dsPhotoEditorIntent.setData(inputImageUri);
                dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "Edited_Image");
                startActivityForResult(dsPhotoEditorIntent, 200);
            }
        });


    }

    private void transitionGetter() {
        ViewGroup viewGroupSlide = findViewById(android.R.id.content);
        View dialogViewSlide = LayoutInflater.from(ImageFullActivity.this).inflate(R.layout.transition_get, viewGroupSlide, false);
        AlertDialog.Builder builderSlide = new AlertDialog.Builder(ImageFullActivity.this);
        builderSlide.setView(dialogViewSlide);
        AlertDialog alertDialogSlide = builderSlide.create();


        alertDialogSlide.show();
        button1 = dialogViewSlide.findViewById(R.id.no_button_trans);
        button2 = dialogViewSlide.findViewById(R.id.ok_button_trans);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogSlide.dismiss();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioGroup1 = dialogViewSlide.findViewById(R.id.radioG_trans);
                selectedIdTrans = radioGroup1.getCheckedRadioButtonId();
                radioButtonTrans = dialogViewSlide.findViewById(selectedIdTrans);
                radioButtonTrans.setChecked(true);
                String transitionValue = radioButtonTrans.getText().toString();
                if(transitionValue.equals("   Sink")){
                    viewPager1.setPageTransformer(true,new DepthPageTransformer());
                }
                if(transitionValue.equals("   Zoom")){
                    viewPager1.setPageTransformer(false,new ZoomOutPageTransformer());
                }
                if(transitionValue.equals("   Spin")){
                    viewPager1.setPageTransformer(false,new SpinnerTransformation());
                }
                if(transitionValue.equals("   Cube_in")){
                    viewPager1.setPageTransformer(false,new CubeInRotationTransformation());
                }
                if(transitionValue.equals("   Cube_out")){
                    viewPager1.setPageTransformer(false,new CubeOutRotationTransformation());
                }
                if(transitionValue.equals("   Hinge")){
                    viewPager1.setPageTransformer(false,new HingeTransformation());
                }
                if(transitionValue.equals("   Fade")){
                    viewPager1.setPageTransformer(false,new FadeOutTransformation());
                }
                SharedPreferences sharedPreferencesTrans = getSharedPreferences("TransitionId",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferencesTrans.edit();
                editor.putString("SelectedTrans",transitionValue);
                editor.apply();


                alertDialogSlide.dismiss();

            }
        });


    }

    private void detailsImage() {
        ViewGroup viewGroupSlide = findViewById(android.R.id.content);
        View dialogViewSlide = LayoutInflater.from(ImageFullActivity.this).inflate(R.layout.details_image, viewGroupSlide, false);
        AlertDialog.Builder builderSlide = new AlertDialog.Builder(ImageFullActivity.this);
        builderSlide.setView(dialogViewSlide);
        AlertDialog alertDialogSlide = builderSlide.create();


        alertDialogSlide.show();
        txtName = dialogViewSlide.findViewById(R.id.image_n1);
        txtPath = dialogViewSlide.findViewById(R.id.image_p1);
        txtDate = dialogViewSlide.findViewById(R.id.image_d1);
        txtSize = dialogViewSlide.findViewById(R.id.image_s1);
        txtName.setText(sliders.get(positions).imageNamer);
        txtPath.setText(sliders.get(positions).imagePather);
        txtDate.setText(sliders.get(positions).imageDate);
        txtSize.setText(sliders.get(positions).imageSize);

    }

    private void setImageWallpaper() {
        Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
        File fileI = new File(imagePath);
        Uri imageUri = getImageContentUri(this, fileI);
        if (imageUri != null) {
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(imageUri, "image/*");
            intent.putExtra("mimeType", "image/*");
            this.startActivity(Intent.createChooser(intent, "Set as:"));
        }
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    private void slideshow() {

        ViewGroup viewGroupSlide = findViewById(android.R.id.content);

        View dialogViewSlide = LayoutInflater.from(ImageFullActivity.this).inflate(R.layout.slideshow_menu, viewGroupSlide, false);
        AlertDialog.Builder builderSlide = new AlertDialog.Builder(ImageFullActivity.this);
        builderSlide.setView(dialogViewSlide);
        AlertDialog alertDialogSlide = builderSlide.create();
        alertDialogSlide.show();
        buttonSlide1 = dialogViewSlide.findViewById(R.id.no_button_slide);
        buttonSlide2 = dialogViewSlide.findViewById(R.id.ok_button_slide);
        buttonSlide1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogSlide.dismiss();
            }
        });
        buttonSlide2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radioGroupSlide = dialogViewSlide.findViewById(R.id.radioGroup_slide);
                radioButtonSlide = dialogViewSlide.findViewById(radioGroupSlide.getCheckedRadioButtonId());
                String slideName1 = radioButtonSlide.getText().toString();
                slideName1 = slideName1.trim() + "000";
                periodMs = Long.parseLong(slideName1);
                alertDialogSlide.dismiss();
                Toast.makeText(ImageFullActivity.this, "Slideshow started", Toast.LENGTH_SHORT).show();
                cancelImage.setVisibility(View.VISIBLE);
                toolbar4.setVisibility(View.GONE);
                toolbar5.setVisibility(View.GONE);


                final long DELAY_MS = 100;//delay in milliseconds before task is to be executed
                final long PERIOD_MS = periodMs;// time in milliseconds between successive task execution
                final Handler handler = new Handler();
                final Runnable Update = new Runnable() {
                    public void run() {
                        if (positions == sliders.size()) {
                            positions = 0;
                        }
                        viewPager1.setCurrentItem(positions++, true);
                    }
                };
                timer = new Timer(); // This will create a new Thread
                timer.schedule(new TimerTask() { // task to be scheduled
                    @Override
                    public void run() {
                        handler.post(Update);
                        SharedPreferences sharingTouch = getSharedPreferences("keyis1", Context.MODE_PRIVATE);
                        check = sharingTouch.getBoolean("keyNot1", false);
                        if (check) {
                            Toast.makeText(ImageFullActivity.this, "Slideshow stopped", Toast.LENGTH_SHORT).show();
                            cancelImage.setVisibility(View.GONE);
                            timer.cancel();

                        }

                    }
                }, DELAY_MS, PERIOD_MS);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case 200:

                    Uri outputUri = data.getData();
                    Toast.makeText(this, "Image saved Successfully.Please check on Edited_Image Folder", Toast.LENGTH_LONG).show();

                    break;
                case 201:
                    if (resultCode == RESULT_OK) {
                        Log.d("deleteImage", "file deleted in ok");
                        if (file.delete()) {
                        }
                        getContentResolver().delete(deleteUri, null, null);

                        Toast.makeText(this, "maybe", Toast.LENGTH_SHORT).show();
                        intentStart();
                    }
                    break;
                case 301:
                    if (resultCode == RESULT_OK) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ImageFullActivity.this).setTitle("Warning")
                                .setMessage("Do you really want to delete this image").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (!(data == null)) {
                                            ClipData clipData = data.getClipData();
                                            if (clipData != null) {
                                                Uri imU = clipData.getItemAt(0).getUri();
                                                deleteImageWUri(imU);
                                            }
                                        }
                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        onBackPressed();
                                    }
                                });
                        builder.show();
                    }
            }

        }

    }

    private void deleteImageWUri(Uri imU) {
        Log.d("nodeletethis", "yes");
        DocumentFile documentFile = DocumentFile.fromSingleUri(this, imU);
        if (documentFile.canWrite() == true) {
            documentFile.delete();
            Toast.makeText(this, "Hi deleted", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendIt(View view) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        Uri uriToImage = Uri.parse(imagePath);
        sendIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
        sendIntent.setType("image/jpeg");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    public void deleteFiles(View view) {
        uriArrayList = new ArrayList<Uri>();
        positions = viewPager1.getCurrentItem();
        file = new File(sliders.get(positions).imagePather);
        Uri imageUri = Uri.fromFile(file);
        uriArrayList.add(imageUri);
        if (file.exists()) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.Q)
                @Override
                public void run() {
                    // Set up the projection (we only need the ID)
                    String[] projection = {MediaStore.Images.Media._ID};

                    // Match on the file path
                    String selection = MediaStore.Images.Media.DATA + " = ?";
                    String[] selectionArgs = new String[]{file.getAbsolutePath()};

                    Uri queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    ContentResolver contentResolver = getContentResolver();

                    Cursor c = contentResolver.query(queryUri, projection, selection, selectionArgs, null);
                    if (c != null) {

                        if (c.moveToFirst()) {
                            Log.d("mydeleteimage", "in movefirst");
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                PendingIntent pi = MediaStore.createDeleteRequest(ImageFullActivity.this.getContentResolver(), uriArrayList);

                                try {
                                    startIntentSenderForResult(pi.getIntentSender(), 201, null, 0, 0,
                                            0);
                                } catch (IntentSender.SendIntentException e) {
                                }
                            }

                                   else {

                                    try {
                                        String s1 = String.valueOf(sliders.get(positions).id);
                                        String arg1[] = new String[]{s1};
                                        long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                                        deleteUri = ContentUris.withAppendedId(queryUri, id);
                                        contentResolver.delete(deleteUri,null,null);
                                        try {
                                            file.getCanonicalFile().delete();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        if(file.delete()){
                                        }
                                        intentStart();

                                    } catch (RecoverableSecurityException recoverableSecurityException) {
                                           if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                                               try {
                                                   ImageFullActivity.this.startIntentSenderForResult(recoverableSecurityException.getUserAction().getActionIntent().getIntentSender(), 201, null, 0, 0, 0, null);
                                               } catch (IntentSender.SendIntentException e) {
                                                   e.printStackTrace();
                                               }

                                           }



                                    }
                                }
                        }
                        c.close();
                    }
                }
            }, 300);


        }
    }



    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1f);
                view.setTranslationX(0f);
                view.setScaleX(1f);
                view.setScaleY(1f);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }
        }
    }
    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }
        }
    }
    public class FadeOutTransformation implements ViewPager.PageTransformer{
        @Override
        public void transformPage(View page, float position) {

            page.setTranslationX(-position*page.getWidth());

            page.setAlpha(1-Math.abs(position));


        }
    }
    public class CubeInRotationTransformation implements ViewPager.PageTransformer{
        @Override
        public void transformPage(View page, float position) {

            page.setCameraDistance(20000);


            if (position < -1){     // [-Infinity,-1)
                // This page is way off-screen to the left.
                page.setAlpha(0);

            }
            else if (position <= 0){    // [-1,0]
                page.setAlpha(1);
                page.setPivotX(page.getWidth());
                page.setRotationY(90*Math.abs(position));

            }
            else if (position <= 1){    // (0,1]
                page.setAlpha(1);
                page.setPivotX(0);
                page.setRotationY(-90*Math.abs(position));

            }
            else{    // (1,+Infinity]
                // This page is way off-screen to the right.
                page.setAlpha(0);

            }


        }
    }
    public class CubeOutRotationTransformation implements ViewPager.PageTransformer {
        @Override
        public void transformPage(View page, float position) {

            if (position < -1){    // [-Infinity,-1)
                // This page is way off-screen to the left.
                page.setAlpha(0);

            }
            else if (position <= 0) {    // [-1,0]
                page.setAlpha(1);
                page.setPivotX(page.getWidth());
                page.setRotationY(-90 * Math.abs(position));

            }
            else if (position <= 1){    // (0,1]
                page.setAlpha(1);
                page.setPivotX(0);
                page.setRotationY(90 * Math.abs(position));

            }
            else {    // (1,+Infinity]
                // This page is way off-screen to the right.
                page.setAlpha(0);

            }
        }
    }
    public class HingeTransformation implements ViewPager.PageTransformer{
        @Override
        public void transformPage(View page, float position) {

            page.setTranslationX(-position*page.getWidth());
            page.setPivotX(0);
            page.setPivotY(0);


            if (position < -1){    // [-Infinity,-1)
                // This page is way off-screen to the left.
                page.setAlpha(0);

            }
            else if (position <= 0){    // [-1,0]
                page.setRotation(90*Math.abs(position));
                page.setAlpha(1-Math.abs(position));

            }
            else if (position <= 1){    // (0,1]
                page.setRotation(0);
                page.setAlpha(1);

            }
            else {    // (1,+Infinity]
                // This page is way off-screen to the right.
                page.setAlpha(0);

            }


        }
    }
    public class SpinnerTransformation implements ViewPager.PageTransformer {
        @Override
        public void transformPage(View page, float position) {

            page.setTranslationX(-position * page.getWidth());
            page.setCameraDistance(12000);

            if (position < 0.5 && position > -0.5) {
                page.setVisibility(View.VISIBLE);
            } else {
                page.setVisibility(View.INVISIBLE);
            }



            if (position < -1){     // [-Infinity,-1)
                // This page is way off-screen to the left.
                page.setAlpha(0);

            }
            else if (position <= 0) {    // [-1,0]
                page.setAlpha(1);
                page.setRotationY(900 *(1-Math.abs(position)+1));

            }
            else if (position <= 1) {    // (0,1]
                page.setAlpha(1);
                page.setRotationY(-900 *(1-Math.abs(position)+1));

            }
            else {    // (1,+Infinity]
                // This page is way off-screen to the right.
                page.setAlpha(0);

            }


        }
    }

    public void intentStart() {
        if (activeClass == 1) {
            Intent intent = new Intent(ImageFullActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            ImageFullActivity.this.finish();

        } else if (activeClass == 2) {
            Intent intent = new Intent(ImageFullActivity.this, Album_OpenActivity.class);
            intent.putExtra("currentSong", textC);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            ImageFullActivity.this.finish();
        }
    }
}



