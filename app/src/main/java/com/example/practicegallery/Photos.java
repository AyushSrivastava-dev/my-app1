package com.example.practicegallery;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.trendyol.bubblescrollbarlib.BubbleScrollBar;
import com.trendyol.bubblescrollbarlib.BubbleTextProvider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Photos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Photos extends Fragment {
    private ArrayList<String> folderName;
    private RecyclerView imageRecycler;
    private ProgressBar progressBar;
    BubbleScrollBar bubbleScrollBarMain;
    private String textContent;
    private ArrayList<Image> allPictures;
    private String s1 = null;
    private String s2 = null;
    AlbumAdapter imageAdapter1;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<Albums> albumsArrayList;

    public Photos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
     * @return A new instance of fragment Photos.
     */
    // TODO: Rename and change types and number of parameters
    public static Photos newInstance(ArrayList<Albums> albumsArrayList) {
        Photos fragment = new Photos();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity activity = (MainActivity)getActivity();
        albumsArrayList = activity.albumlist;


        View rootView = inflater.inflate(R.layout.fragment_photos, container, false);
        folderName = new ArrayList<String>();
        imageRecycler = rootView.findViewById(R.id.album_Recycler);
        progressBar = rootView.findViewById(R.id.recycler_progress_main);
        bubbleScrollBarMain = rootView.findViewById(R.id.bubbleScrollBarMain);
        bubbleScrollBarMain.attachToRecyclerView(imageRecycler);
        imageRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        imageRecycler.setHasFixedSize(true);
        progressBar.setVisibility(View.VISIBLE);
        imageAdapter1 = new AlbumAdapter(getActivity(), albumsArrayList);
        bubbleScrollBarMain.setBubbleTextProvider(new BubbleTextProvider() {
            @Override
            public String provideBubbleText(int i) {
                return imageAdapter1.albumList.get(i).folder;
            }
        });

        imageRecycler.setAdapter(imageAdapter1);

        progressBar.setVisibility(View.GONE);

        return rootView;

    }
    }
