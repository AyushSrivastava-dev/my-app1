package com.example.practicegallery;

import android.os.Parcel;
import android.os.Parcelable;

public class Albums implements Parcelable {
    String folder;
    String firstPic;
    int pic_count;
    public Albums(String folder, String firstPic,int pic_count){
        this.folder = folder;
        this.firstPic = firstPic;
        this.pic_count = pic_count;
    }
    public Albums(){

    }

    protected Albums(Parcel in) {
        folder = in.readString();
        firstPic = in.readString();
        pic_count = in.readInt();
    }

    public static final Creator<Albums> CREATOR = new Creator<Albums>() {
        @Override
        public Albums createFromParcel(Parcel in) {
            return new Albums(in);
        }

        @Override
        public Albums[] newArray(int size) {
            return new Albums[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(folder);
        parcel.writeString(firstPic);
        parcel.writeInt(pic_count);
    }
}
