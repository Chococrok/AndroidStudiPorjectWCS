package matterless.fr.wcs.matterless;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Random;

/**
 * Created by apprenti on 12/04/17.
 */

public class Day implements Parcelable {

    private boolean enabled;
    private int id;
    private String name;

    private Day(){}

    public Day(String str){
        this.name = str;
        this.id = new Random().nextInt();
    }


    protected Day(Parcel in) {
        enabled = in.readByte() != 0;
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<Day> CREATOR = new Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel in) {
            return new Day(in);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (enabled ? 1 : 0));
        dest.writeInt(id);
        dest.writeString(name);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}