package com.example.practicegallery;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.trendyol.bubblescrollbarlib.BubbleScrollBar;
import com.trendyol.bubblescrollbarlib.BubbleTextProvider;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Videos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Videos extends Fragment {
    private ArrayList<String> folderName;
    //    private ArrayList<String> firstImPath;
//    Parcelable state;
//    ListView listView;
    private RecyclerView imageRecycler;
    private ProgressBar progressBar;
    BubbleScrollBar bubbleScrollBarMain;
    private String textContent;
    private ArrayList<Image> allPictures;
    private
    int count;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Videos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Videos.
     */
    // TODO: Rename and change types and number of parameters
    public static Videos newInstance(String param1, String param2) {
        Videos fragment = new Videos();
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
        View mainView = inflater.inflate(R.layout.fragment_videos, container, false);

        folderName = new ArrayList<String>();
        imageRecycler = mainView.findViewById(R.id.album_Recycler);
        progressBar = mainView.findViewById(R.id.recycler_progress_main);
        bubbleScrollBarMain = mainView.findViewById(R.id.bubbleScrollBarMain);
        bubbleScrollBarMain.attachToRecyclerView(imageRecycler);
        imageRecycler.setLayoutManager(new GridLayoutManager(getActivity(),3));
        imageRecycler.setHasFixedSize(true);
        if(ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},101);
        }

        ArrayList<Image> images = new ArrayList<Image>();
        ArrayList<VideosModelClass> videosArrayList = new ArrayList<VideosModelClass>();
        Uri allImagesuri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.Video.Media.DATA ,MediaStore.Video.Media.DISPLAY_NAME,MediaStore.Video.Media.BUCKET_DISPLAY_NAME,MediaStore.Video.Media.DATE_TAKEN};
//        String orderBy = MediaStore.Images.ImageColumns.DATE_TAKEN;
   //     String sortOrder = MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC";
        String sortOrder = MediaStore.Video.VideoColumns.DATE_TAKEN + " DESC";

        Cursor cursor1 = getActivity().getContentResolver().query(allImagesuri,
                projection,
                null,
                null,
                sortOrder);
        try{
            cursor1.moveToFirst();
            do{
                String a = cursor1.getString(cursor1.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME));
                String b = cursor1.getString(cursor1.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                Image image12 = new Image();
                image12.imagePath = b;
                image12.imageName = cursor1.getString(cursor1.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                image12.folder = a;
                images.add(image12);



                if(!folderName.contains(a)){
                    VideosModelClass videos1 = new VideosModelClass();
                    videos1.folderName = a;
                    videos1.firstPicPath = b;
                    videosArrayList.add(videos1);
//                    firstImPath.add(b);
                    folderName.add(a);
                }

            }while(cursor1.moveToNext()) ;
            cursor1.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        for(int i = 0; i<folderName.size(); i++){
            count = 0;
            for(int j = 0; j<images.size(); j++){
                if(folderName.get(i).equals(images.get(j).folder)){
                    count = count + 1;

                }
                if(count>0){
                    videosArrayList.get(i).pic_counts = count;
                }

            }
        }
        progressBar.setVisibility(View.VISIBLE);
        VideosAdapter imageAdapter1 = new VideosAdapter(getActivity(),videosArrayList);
        bubbleScrollBarMain.setBubbleTextProvider(new BubbleTextProvider() {
            @Override
            public String provideBubbleText(int i) {
                return imageAdapter1.videoList.get(i).folderName;
            }
        });

        imageRecycler.setAdapter(imageAdapter1);

        progressBar.setVisibility(View.GONE);

        return  mainView;
    }
}