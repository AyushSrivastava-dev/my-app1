package com.example.practicegallery;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.RecoverableSecurityException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.UriPermission;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.trendyol.bubblescrollbarlib.BubbleScrollBar;
import com.trendyol.bubblescrollbarlib.BubbleTextProvider;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Recent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Recent extends Fragment implements ClickInterface {
    private ArrayList<String> folderName;
    //    private ArrayList<String> firstImPath;
//    Parcelable state;
//    ListView listView;
    private Uri deleteUri;
    private RecyclerView imageRecycler;
    private ProgressBar progressBar;
    BubbleScrollBar bubbleScrollBarMain;
    private String textContent;
    private ArrayList<Image> allPictures;
    private String s1 = null;
    private String s2 = null;
    int postionS;
    boolean flag2 = true;
    boolean flag3 = true;
    RecentAdapter imageAdapter2;
    ArrayList<Image> albumsArrayList;
    ArrayList<Uri> files;
    List<UriPermission> files2;
    ArrayList<Image> imagesArrayL1;
    FloatingActionButton floatingActionButton1;
    FloatingActionButton floatingActionButton2;
    FloatingActionButton floatingActionButton3;
    int j = 0;
    ArrayList<Uri> uriArrayList;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Recent() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Recent.
     */
    // TODO: Rename and change types and number of parameters
    public static Recent newInstance(String param1, String param2) {
        Recent fragment = new Recent();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MainActivity activity = (MainActivity) getActivity();
        albumsArrayList = activity.imagesList;
//        albumsArrayList = activity.albumlist;
        if (savedInstanceState != null) {
            postionS = savedInstanceState.getInt("ScrollPosition");
        }


        View rootView = inflater.inflate(R.layout.fragment_recent, container, false);
        folderName = new ArrayList<String>();
        imageRecycler = rootView.findViewById(R.id.photo_Recycler);
        progressBar = rootView.findViewById(R.id.recycler_progress_photo);
        bubbleScrollBarMain = rootView.findViewById(R.id.bubbleScrollBa_photo);
        floatingActionButton1 = rootView.findViewById(R.id.delete_f);
        floatingActionButton2 = rootView.findViewById(R.id.share_f);
        floatingActionButton3 = rootView.findViewById(R.id.canel_f);
        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToNormal();
            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                senderShare();
            }
        });
        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.Q)
                    @Override
                    public void run() {
                        deleteInBulk();

                    }
                }, 500);
            }

        });
        bubbleScrollBarMain.attachToRecyclerView(imageRecycler);
        imageRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        imageRecycler.scrollToPosition(postionS);
        imageRecycler.setHasFixedSize(true);

        progressBar.setVisibility(View.VISIBLE);
        imageAdapter2 = new RecentAdapter(getActivity(), albumsArrayList, this);
        bubbleScrollBarMain.setBubbleTextProvider(new BubbleTextProvider() {
            @Override
            public String provideBubbleText(int i) {
//                return imageAdapter1.albumList.get(i).folder;

                Log.d("Showdate",imageAdapter2.imagesList.get(i).imageDate);

                return imageAdapter2.imagesList.get(i).imageDate;
            }
        });

        imageRecycler.setAdapter(imageAdapter2);

        progressBar.setVisibility(View.GONE);

        return rootView;

    }

    @Override
    public void onItemClick(int position, View itemView, Image image) {
        postionS = position;
        if (flag2) {
            Intent intent1 = new Intent(getActivity(), ImageFullActivity.class);
            intent1.putExtra("folder", "");
            intent1.putExtra("path", image.imagePath);
            intent1.putExtra("name", image.imageName);
            intent1.putExtra("textCon", "");

            getActivity().startActivity(intent1);
        } else {
            Log.d("nameisA", " " + imagesArrayL1.get(0).folder.toString());
            image.setChecked(!image.isChecked());
            itemView.setVisibility(image.isChecked() ? View.VISIBLE : View.GONE);
            imagesArrayL1.add(image);
            if (files.contains(Uri.parse(image.imagePath.toString()))) {
                files.remove(Uri.parse(image.imagePath.toString()));
            }
            else{
                files.add(Uri.parse((image.imagePath.toString())));
            }


//            if(itemView.getVisibility() == View.GONE){
//                itemView.setVisibility(View.VISIBLE);
//            }
//            else{
//                itemView.setVisibility(View.GONE);
//            }
////
////                itemView.setVisibility(View.GONE);

        }
    }

    @Override
    public void onLongItemClick(int position, View itemView, Image image) {
        flag2 = false;
        Toast.makeText(getActivity(), "Please Swipe Up", Toast.LENGTH_SHORT).show();
        files = new ArrayList<Uri>();
        files.add(Uri.parse(image.imagePath.toString()));
        imagesArrayL1 = new ArrayList<Image>();
        imagesArrayL1.add(image);
        floatingActionButton1.setVisibility(View.VISIBLE);
        floatingActionButton2.setVisibility(View.VISIBLE);
        floatingActionButton3.setVisibility(View.VISIBLE);
        Log.d("nameisA", " " + position);
//        if(itemView.getVisibility() == View.GONE){
//            itemView.setVisibility(View.VISIBLE);
//        }
//        else{
//            itemView.setVisibility(View.GONE);
//        }
//                itemView.setVisibility(View.GONE);
        image.setChecked(!image.isChecked());
        itemView.setVisibility(image.isChecked() ? View.VISIBLE : View.GONE);


    }

    public void backToNormal() {
        flag2 = true;
        j=0;
        files.clear();
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();

    }

    public void senderShare() {
        if (!files.isEmpty()) {
            Intent shareIntents = new Intent();
            shareIntents.setAction(Intent.ACTION_SEND_MULTIPLE);
            shareIntents.putExtra(Intent.EXTRA_SUBJECT, "SOME FILES");
            shareIntents.setType("image/jpeg");
            shareIntents.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
            Intent shareIntent = Intent.createChooser(shareIntents, null);

            startActivity(shareIntent);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        outState.putInt("ScrollPosition", postionS);
        super.onSaveInstanceState(outState);
    }

    public void deleteInBulk() {
        String[] projection = {MediaStore.Images.Media._ID};
        if(j<files.size()) {
            File file1 = new File(files.get(j++).getPath());

            // Match on the file path

            String selection = MediaStore.Images.Media.DATA + " = ?";
            String[] selectionArgs = new String[]{file1.getAbsolutePath()};

            // Query for the ID of the media matching the file path
            Uri queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver contentResolver = getActivity().getContentResolver();
            Log.d("mydeleteimage", "cursor in ryece");

            Cursor c = contentResolver.query(queryUri, projection, selection, selectionArgs, null);
            if (c != null) {

                    if (c.moveToFirst()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            try {

                                long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                                deleteUri = ContentUris.withAppendedId(queryUri, id);
                                contentResolver.delete(deleteUri, null, null);

                            } catch (RecoverableSecurityException recoverableSecurityException) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    try {
                                        getActivity().startIntentSenderForResult(recoverableSecurityException.getUserAction().getActionIntent().getIntentSender(), 2011, null, 0, 0, 0, null);
                                    } catch (IntentSender.SendIntentException e) {
                                        e.printStackTrace();
                                    }

                                }


                            }
                        }
                        else{
                            long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                            deleteUri = ContentUris.withAppendedId(queryUri, id);
                            contentResolver.delete(deleteUri, null, null);
                            deleteInBulk();
                        }
                    }c.close();
            }


        }
        else{
            backToNormal();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 2011:
                    if (resultCode == RESULT_OK) {
                        Log.d("hAFh","how is this deleted");
                        getActivity().getContentResolver().delete(deleteUri, null, null);
                        deleteInBulk();

                    }
                    break;
            }
        }
    }
}


