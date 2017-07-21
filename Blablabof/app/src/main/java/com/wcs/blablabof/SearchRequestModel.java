package com.wcs.blablabof;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by apprenti on 04/05/17.
 */

public class SearchRequestModel implements Parcelable {

    private String departure;
    private String destination;
    private java.util.Date date;

    public SearchRequestModel(){}

    public SearchRequestModel(String departure, String destination, Date date) {
        this.departure = departure;
        this.destination = destination;
        this.date = date;
    }

    protected SearchRequestModel(Parcel in) {
        departure = in.readString();
        destination = in.readString();
        date = new Date(in.readLong());
    }

    public static final Creator<SearchRequestModel> CREATOR = new Creator<SearchRequestModel>() {
        @Override
        public SearchRequestModel createFromParcel(Parcel in) {
            return new SearchRequestModel(in);
        }

        @Override
        public SearchRequestModel[] newArray(int size) {
            return new SearchRequestModel[size];
        }
    };

    public String getDeparture() {
        return departure;
    }

    public String getDestination() {
        return destination;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(departure);
        dest.writeString(destination);
        dest.writeLong(date.getTime());
    }
}
