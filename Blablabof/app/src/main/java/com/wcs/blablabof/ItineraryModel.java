package com.wcs.blablabof;

import java.util.Date;

/**
 * Created by apprenti on 04/05/17.
 */

public class ItineraryModel {

    private String userID;
    private String nickName;
    private Date departureDate;
    private int price;
    private String departure;
    private String destination;

    public ItineraryModel(){}

    public ItineraryModel(Date departureDate, int price, String departure, String destination, String userID, String nickName) {
        this.userID = userID;
        this.nickName = nickName;

        this.departureDate = departureDate;
        this.price = price;
        this.departure = departure;
        this.destination = destination;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
