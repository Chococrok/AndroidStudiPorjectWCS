package src.com.hoho.android.usbserial.examples;

import android.location.Location;

/**
 * Created by apprenti on 24/03/17.
 */

public class LatLong {

    private double latitude;
    private double longitude;

    private LatLong(){};

    public LatLong(double latitude, double longitude){

        this.latitude = latitude;
        this.longitude = longitude;

    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
