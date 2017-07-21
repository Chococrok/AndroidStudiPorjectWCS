package blablawild.fr.wcs.multiacvtivityquest;

import android.content.Context;

import java.util.Date;

import android.content.res.Resources;

/**
 * Created by apprenti on 13/03/17.
 */

public class ItineraryModel {

    private String userID;
    private String userName;
    private Date departureDate;
    private int price;
    private String departure;
    private String destination;
    private String departureDateString;

    private ItineraryModel(){}

    public ItineraryModel(String userID, String departureDateString, int price, String departure, String destination, String userName) {
        this.userID = userID;
        this.userName = userName;
        this.departureDateString = departureDateString;
        this.price = price;
        this.departure = departure;
        this.destination = destination;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return this.userName;
    }

    public Date getDepartureDate() {
        return departureDate;
    }
    public String getDepartureDateString(){
        return this.departureDateString;
    }

    public int getPrice() {
        return price;
    }

    public String getDeparture() {
        return departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDepartureDateString(String departureDateString) {
        this.departureDateString = departureDateString;
    }

}
