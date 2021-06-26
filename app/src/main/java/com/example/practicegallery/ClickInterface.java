package com.example.practicegallery;

import android.view.View;

public interface ClickInterface {
    void onItemClick(int position, View itemView,Image image);
    void onLongItemClick(int position, View itemView,Image image);
}
