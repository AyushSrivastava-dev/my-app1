package com.example.practicegallery;

public class Image {
    String imagePath = null;
    String imageName = null;
    String folder = null;
    String firstImagePath = null;
    String imageDate = null;
    boolean chek = false;

    public Image(String imagePath, String imageName,String folder) {
        this.imagePath = imagePath;
        this.imageName = imageName;
        this.folder = folder;
    }

    public Image() {
    }
    public Image(String folder,String firstImagePath){
        this.folder = folder;
        this.firstImagePath = firstImagePath;
    }
    public boolean isChecked(){
        return chek;
    }
    public void setChecked(boolean checked){
        this.chek = checked;
    }
}
