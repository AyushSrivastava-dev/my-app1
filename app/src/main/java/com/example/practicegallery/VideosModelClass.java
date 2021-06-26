//package com.example.practicegallery;
//
//public class VideosModelClass {
//
//}
package com.example.practicegallery;

public class VideosModelClass {
    String videoName;
    String folderName;
    String firstPicPath;
    int pic_counts;
    public VideosModelClass(String folder,String firstPic,int pic_count,String videoName){
        this.folderName = folder;
       this.firstPicPath = firstPic;
        this.pic_counts = pic_count;
        this.videoName = videoName;
    }
    public VideosModelClass(){

    }
}

