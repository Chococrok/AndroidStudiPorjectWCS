package blablawild.fr.wcs.multiacvtivityquest;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by apprenti on 06/03/17.
 */

public class SearchRequestModel implements Parcelable {

    private String mStart;
    private String mArrival;
    private String mDate;

    private SearchRequestModel(){}

    public SearchRequestModel(String start, String arrival, String date){

        this.mStart = start;
        this.mArrival = arrival;
        this.mDate = date;
    }

    protected SearchRequestModel(Parcel in) {
        mStart = in.readString();
        mArrival = in.readString();
        mDate = in.readString();
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

    public String getStart(){
        return mStart;
    }
    public  String getArrival(){
        return mArrival;
    }
    public  String getDate(){
        return mDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mStart);
        dest.writeString(mArrival);
        dest.writeString(mDate);
    }

}
