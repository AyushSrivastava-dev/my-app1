package com.example.practicegallery;

public class ImageSliderModel {
    String imageNamer = null;
    String imagePather = null;
    String imageSize = null;
    String imageDate = null;
    int id;

    public ImageSliderModel(String imageNamer, String imagePather,int id) {
        this.imageNamer = imageNamer;
        this.imagePather = imagePather;
        this.id = id;
    }

    public ImageSliderModel() {
    }
}
