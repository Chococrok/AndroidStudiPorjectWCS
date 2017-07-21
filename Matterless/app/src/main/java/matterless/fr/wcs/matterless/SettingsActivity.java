package matterless.fr.wcs.matterless;

import android.*;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {


    public final String API_BASE_URL = "https://chat.wildcodeschool.fr/api/v3/";
    public final String TAG = "SettingsActivity";
    public static final int MY_PERMISSIONS_REQUEST_TO_LOCATION = 1;


    private TextView textViewUserName;
    private UsersLogin usersLogin;
    private UserProfile userProfile;
    private UserCredentials muserCredentials;
    private TextView textViewTitle;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private CheckBox checkBoxTermsOfUse;
    private Button buttonlog;
    private Button buttonTutorial;
    private String authToken;

    private ImageView imageViewProfilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(SettingsActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_TO_LOCATION);
        }

        imageViewProfilePicture = (ImageView) findViewById(R.id.imageViewProfilePicture);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        checkBoxTermsOfUse = (CheckBox) findViewById(R.id.checkBoxTermsOfUse);
        checkBoxTermsOfUse.setMovementMethod(LinkMovementMethod.getInstance());
        buttonlog = (Button) findViewById(R.id.buttonLog);
        buttonTutorial= (Button) findViewById(R.id.buttonTutorial);
        buttonTutorial.setOnClickListener(this);
        buttonlog.setOnClickListener(this);
        textViewUserName = (TextView) findViewById(R.id.textViewUserName);
        textViewTitle = (TextView) findViewById(R.id.textViewConnectTitle);

        muserCredentials = new UserCredentials();
        muserCredentials = UserCredentials.fromFile(this, MainActivity.FILE_NAME);


        if(muserCredentials != null){

            setProfileVisible();

            MattermostService getUserImageUrl = ServiceGenerator.RETROFIT.create(MattermostService.class);
            Call<ResponseBody> callImageUrl = getUserImageUrl.getProfilePicture( "Bearer " + muserCredentials.getToken(), muserCredentials.getImageUrl());
            callImageUrl.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseImageUrl) {
                    if (responseImageUrl.isSuccessful()) {

                        boolean writtenToDisk = writeResponseBodyToDisk(responseImageUrl.body());
                        if(writtenToDisk) {
                            File imageFile = new File(String.valueOf(getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/profilePicture.png")));
                            Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                            imageViewProfilePicture.setImageBitmap(myBitmap);
                        }


                    } else {
                        SettingsActivity.this.deleteFile(MainActivity.FILE_NAME);
                        usersLogin = new UsersLogin(muserCredentials.getEmail(), muserCredentials.getPassword());

                        MattermostService mattermostService =
                                ServiceGenerator.RETROFIT.create(MattermostService.class);
                        Call<Void> callLogin = mattermostService.basicLogin(usersLogin);
                        callLogin.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, final Response<Void> response) {

                                if (response.isSuccessful()) {
                                    // usersLogin object available
                                    authToken = response.headers().get("Token");
                                    MattermostService getUserProfile =
                                            ServiceGenerator.RETROFIT.create(MattermostService.class);
                                    Call<UserProfile> callUserProfile = getUserProfile.getUser("Bearer " + authToken);
                                    callUserProfile.enqueue(new Callback<UserProfile>() {
                                        @Override
                                        public void onResponse(Call<UserProfile> call, Response<UserProfile> responseUser) {

                                            if (responseUser.isSuccessful()) {
                                                // usersLogin object available
                                                setProfileVisible();

                                                userProfile = responseUser.body();
                                                String imageUrl = API_BASE_URL + "users/" +userProfile.getId() + "/image?time=" + userProfile.getLastPictureUpdate();

                                                UserCredentials muserCredentials = new UserCredentials(userProfile.getId(), userProfile.getEmail(),
                                                        editTextPassword.getText().toString(),
                                                        authToken,
                                                        imageUrl,
                                                        userProfile.getUsername());

                                                UserCredentials.toFile(SettingsActivity.this, MainActivity.FILE_NAME, muserCredentials);

                                                MattermostService getUserImageUrl = ServiceGenerator.RETROFIT.create(MattermostService.class);
                                                Call<ResponseBody> callImageUrl = getUserImageUrl.getProfilePicture( "Bearer " + authToken, imageUrl);
                                                callImageUrl.enqueue(new Callback<ResponseBody>() {
                                                    @Override
                                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseImageUrl) {
                                                        if (responseImageUrl.isSuccessful()) {

                                                            boolean writtenToDisk = writeResponseBodyToDisk(responseImageUrl.body());
                                                            if(writtenToDisk) {
                                                                File imageFile = new File(String.valueOf(getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/profilePicture.png")));
                                                                Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                                                                imageViewProfilePicture.setImageBitmap(myBitmap);
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                        Log.e(TAG, t.getMessage());
                                                    }
                                                });

                                            } else {
                                                // error response, no access to resource?
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<UserProfile> call, Throwable t) {
                                            Log.e(TAG, t.getMessage());
                                        }
                                    });




                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                // something went completely south (like no internet connection)
                                Log.e(TAG, t.getMessage());
                            }
                        });

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e(TAG, t.getMessage());
                }
            });
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonLog:
                if(editTextEmail != null && editTextPassword != null && muserCredentials == null && checkBoxTermsOfUse.isChecked()){

                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage(getString(R.string.progress_dialog));
                    progressDialog.show();
                    usersLogin = new UsersLogin(editTextEmail.getText().toString(), editTextPassword.getText().toString());
                    MattermostService mattermostService =
                            ServiceGenerator.RETROFIT.create(MattermostService.class);
                    Call<Void> call = mattermostService.basicLogin(usersLogin);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, final Response<Void> response) {

                            if (response.isSuccessful()) {
                                // usersLogin object available
                                authToken = response.headers().get("Token");
                                MattermostService getUserProfile =
                                        ServiceGenerator.RETROFIT.create(MattermostService.class);
                                Call<UserProfile> callUserProfile = getUserProfile.getUser("Bearer " + authToken);
                                callUserProfile.enqueue(new Callback<UserProfile>() {
                                    @Override
                                    public void onResponse(Call<UserProfile> call, Response<UserProfile> responseUser) {

                                        if (responseUser.isSuccessful()) {
                                            // usersLogin object available

                                            userProfile = responseUser.body();
                                            String imageUrl = API_BASE_URL + "users/" +userProfile.getId() + "/image?time=" + userProfile.getLastPictureUpdate();

                                             muserCredentials = new UserCredentials(userProfile.getId(), userProfile.getEmail(),
                                                    editTextPassword.getText().toString(),
                                                    authToken,
                                                    imageUrl,
                                                    userProfile.getUsername());

                                            UserCredentials.toFile(SettingsActivity.this, MainActivity.FILE_NAME, muserCredentials);


                                            Intent toService = new Intent(SettingsActivity.this, MyService.class);
                                            toService.setAction(MyService.INTENT_START_BOT);
                                            startService(toService);

                                            setProfileVisible();

                                            MattermostService getUserImageUrl = ServiceGenerator.RETROFIT.create(MattermostService.class);
                                            Call<ResponseBody> callImageUrl = getUserImageUrl.getProfilePicture( "Bearer " + authToken, imageUrl);
                                            callImageUrl.enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> responseImageUrl) {
                                                    if (responseImageUrl.isSuccessful()) {

                                                        boolean writtenToDisk = writeResponseBodyToDisk(responseImageUrl.body());
                                                        if(writtenToDisk) {
                                                            File imageFile = new File(String.valueOf(getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/profilePicture.png")));
                                                            Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                                                            imageViewProfilePicture.setImageBitmap(myBitmap);
                                                            Toast.makeText(SettingsActivity.this, R.string.logging_success, Toast.LENGTH_SHORT).show();
                                                            progressDialog.dismiss();
                                                        }


                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                    Log.e(TAG, t.getMessage());
                                                }
                                            });

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<UserProfile> call, Throwable t) {
                                        Log.e(TAG, t.getMessage());
                                    }
                                });




                            } else{
                                Toast.makeText(SettingsActivity.this, R.string.toastConnexionFailed, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            // something went completely south (like no internet connection)
                            Log.d(TAG, t.getMessage());
                        }
                    });
                }

                else if (authToken != null || muserCredentials != null){

                    textViewTitle.setVisibility(View.VISIBLE);
                    editTextEmail.setVisibility(View.VISIBLE);
                    editTextEmail.setText(null);
                    editTextPassword.setVisibility(View.VISIBLE);
                    editTextPassword.setText(null);
                    checkBoxTermsOfUse.setVisibility(View.VISIBLE);
                    checkBoxTermsOfUse.setChecked(false);
                    textViewUserName.setVisibility(View.INVISIBLE);
                    imageViewProfilePicture.setVisibility(View.INVISIBLE);
                    buttonlog.setText(R.string.buttonLogInText);
                    muserCredentials = null;
                    SettingsActivity.this.deleteFile(MainActivity.FILE_NAME);
                }

                else{
                    Toast.makeText(this, R.string.toast_message_empty, Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.buttonTutorial :

                Intent intent = new Intent(SettingsActivity.this, TutorialActivity.class);
                startActivity(intent);
                finish();
                break;
        }

    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(SettingsActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "profilePicture.png");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    private void setProfileVisible(){

        textViewTitle.setVisibility(View.GONE);
        editTextEmail.setVisibility(View.GONE);
        editTextPassword.setVisibility(View.GONE);
        checkBoxTermsOfUse.setVisibility(View.GONE);
        textViewUserName.setVisibility(View.VISIBLE);
        imageViewProfilePicture.setVisibility(View.VISIBLE);
        buttonlog.setText(R.string.buttonLogOutText);

        textViewUserName.setText(muserCredentials.getUserName());
    }
}
