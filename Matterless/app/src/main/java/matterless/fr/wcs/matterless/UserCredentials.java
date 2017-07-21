package matterless.fr.wcs.matterless;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by apprenti on 30/03/17.
 */

public class UserCredentials {

    private String email;
    private String password;
    private String token;
    private String imageUrl;
    private String userName;
    private String userID;
    private static FileOutputStream fileOutputStream;

    public UserCredentials(){}

    public UserCredentials(String userID, String email, String password, String token, String imageUrl, String userName){

        this.userID = userID;
        this.email = email;
        this.password = password;
        this.token = token;
        this.imageUrl = imageUrl;
        this.userName = userName;
    }

    public UserCredentials (String[] stringArray){

        this.userID = stringArray[0];
        this.email = stringArray[1];
        this.password = stringArray[2];
        this.token = stringArray[3];
        this.imageUrl = stringArray[4];
        this.userName = stringArray[5];


    }

    public String oneString(){
        return this.userID+ "|"+this.email+ "|"+this.password+"|"+this.getToken()+"|"+this.imageUrl+"|"+this.userName;
    }


    public static UserCredentials fromFile(Context context, String fileName) {


        UserCredentials userCredentials = null;
        FileInputStream fileInputStream;

        try {

            fileInputStream = context.openFileInput(fileName);

            int c;
            StringBuilder sb = new StringBuilder();
            while ((c = fileInputStream.read()) != -1) {
                sb.append(Character.toString((char) c));
            }
            String[] arr = sb.toString().split("\\|");
            userCredentials = new UserCredentials(arr);
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return userCredentials;
    }


    public static void toFile(Context context, String fileName, UserCredentials userCredentials) {

        try {
            fileOutputStream = context.openFileOutput(fileName, context.MODE_PRIVATE);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fileOutputStream.write(userCredentials.oneString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserID() {
        return userID;
    }
}
