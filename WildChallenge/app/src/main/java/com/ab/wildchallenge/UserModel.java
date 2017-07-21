package com.ab.wildchallenge;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by apprenti on 13/07/17.
 */

public class UserModel {

    public static final int MALE = 0;
    public static final int FEMALE = 1;

    public static final String RED = "red";
    public static final String BLUE = "blue";
    public static final String YELLOW = "yellow";
    public static final String UNKNOWN = "unknown";
    public static final String ANONYMOUS = "Anonymous";

    public static final String CLICK_REF = "clickCount";

    private int clickCount;
    private String nickName;
    private String email;
    private String team;

    private UserModel() {
    }

    public UserModel(int clickCount, String nickName, String email, String team) {
        this.clickCount = clickCount;
        this.nickName = nickName;
        this.email = email;
        this.team = team;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }
}
