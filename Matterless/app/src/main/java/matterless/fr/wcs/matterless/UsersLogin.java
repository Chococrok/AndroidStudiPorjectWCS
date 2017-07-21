package matterless.fr.wcs.matterless;

import com.google.gson.annotations.SerializedName;

public class UsersLogin {

    @SerializedName("login_id")
    private String email;
    @SerializedName("password")
    private String password;

    public UsersLogin(String email, String password) {

        this.email = email;
        this.password = password;
    }
}
