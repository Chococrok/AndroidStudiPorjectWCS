package com.ab.appstudapplication;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by apprenti on 17/06/17.
 */

public class PlaceModel implements Parcelable {

    private Bitmap mPhotos;
    private String mNames;

    public PlaceModel(Bitmap mPhotos, String mNames) {
        this.mPhotos = mPhotos;
        this.mNames = mNames;
    }

    protected PlaceModel(Parcel in) {
        mPhotos = in.readParcelable(Bitmap.class.getClassLoader());
        mNames = in.readString();
    }

    public static final Creator<PlaceModel> CREATOR = new Creator<PlaceModel>() {
        @Override
        public PlaceModel createFromParcel(Parcel in) {
            return new PlaceModel(in);
        }

        @Override
        public PlaceModel[] newArray(int size) {
            return new PlaceModel[size];
        }
    };

    public Bitmap getmPhotos() {
        return mPhotos;
    }

    public void setmPhotos(Bitmap mPhotos) {
        this.mPhotos = mPhotos;
    }

    public String getmNames() {
        return mNames;
    }

    public void setmNames(String mNames) {
        this.mNames = mNames;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(mPhotos, i);
        parcel.writeString(mNames);
    }
}
