package matterless.fr.wcs.matterless;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Message implements Parcelable {

    private String mName;
    private int eventID;
    private int mTimeMinute;
    private int mTimeHour;
    private String mChannelId;
    private String mChannelName;
    private String mMessageContent;
    private ArrayList<Day> mDays;
    private Double Lat;
    private Double Lng;


    public Message(){}

    public Message(String[] str){
        mDays = new ArrayList<>();
        for (int i = 0; i < str.length; i++){
            mDays.add(new Day(str[i]));

        }
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public int getmTimeMinute() {
        return mTimeMinute;
    }

    public void setmTimeMinute(int mTimeMinute) {
        this.mTimeMinute = mTimeMinute;
    }

    public int getmTimeHour() {
        return mTimeHour;
    }

    public void setmTimeHour(int mTimeHour) {
        this.mTimeHour = mTimeHour;
    }

    public String getmChannelId() {
        return mChannelId;
    }

    public void setmChannelId(String mChannelId) {
        this.mChannelId = mChannelId;
    }

    public String getmChannelName() {
        return mChannelName;
    }

    public void setmChannelName(String mChannelName) {
        this.mChannelName = mChannelName;
    }

    public String getmMessageContent() {
        return mMessageContent;
    }

    public void setmMessageContent(String mMessageContent) {
        this.mMessageContent = mMessageContent;
    }

    public ArrayList<Day> getmDays() {
        return mDays;
    }

    public void setmDays(ArrayList<Day> mDays) {
        this.mDays = mDays;
    }

    public Double getLat() {
        return Lat;
    }

    public void setLat(Double lat) {
        Lat = lat;
    }

    public Double getLng() {
        return Lng;
    }

    public void setLng(Double lng) {
        Lng = lng;
    }

    public static Creator<Message> getCREATOR() {
        return CREATOR;
    }

    protected Message(Parcel in) {
        mName = in.readString();
        eventID = in.readInt();
        mTimeMinute = in.readInt();
        mTimeHour = in.readInt();
        mChannelId = in.readString();
        mChannelName = in.readString();
        mMessageContent = in.readString();
        mDays = in.createTypedArrayList(Day.CREATOR);
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public String getDaysEnabled(){

        if (mDays == null){

            return "null";

        }else {

            ArrayList<String> strArray = new ArrayList<>();

            for (int i = 0; i < mDays.size(); i++) {
                switch ((byte) (mDays.get(i).isEnabled() ? 1 : 0)) {
                    case 1:
                        strArray.add(mDays.get(i).getName());
                        break;
                }
            }

            String daysDisplay = "";
            if (strArray.size() > 1) {

                for (int i = 0; i < 2; i++) {

                    if (i == strArray.size() - 1) {

                        daysDisplay = daysDisplay + strArray.get(i) + ".";
                    } else {

                        daysDisplay = daysDisplay + strArray.get(i) + ", ";
                    }
                }
                daysDisplay = daysDisplay + "...";
            } else if (strArray.size() > 0) {
                daysDisplay = strArray.get(0);
            }
            return daysDisplay;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeInt(eventID);
        dest.writeInt(mTimeMinute);
        dest.writeInt(mTimeHour);
        dest.writeString(mChannelId);
        dest.writeString(mChannelName);
        dest.writeString(mMessageContent);
        dest.writeTypedList(mDays);
    }
}
