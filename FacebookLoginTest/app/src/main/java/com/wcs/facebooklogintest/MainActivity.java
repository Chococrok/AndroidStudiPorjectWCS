package com.wcs.facebooklogintest;

import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.LoginStatusCallback;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private TextView textViewId;
    private ImageView imageViewPictureProfile;
    private Button buttonStream;

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewId = (TextView) findViewById(R.id.textViewId);
        imageViewPictureProfile = (ImageView) findViewById(R.id.imageViewPictureProfile);
        buttonStream = (Button) findViewById(R.id.buttonStream);
        buttonStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, StreamingActivity.class));
            }
        });
        loginButton = (LoginButton) findViewById(R.id.loginButtonFacebook);

        callbackManager = CallbackManager.Factory.create();
        profile = Profile.getCurrentProfile();
        if (profile != null){
            textViewId.setText(profile.getName());
            Glide.with(this).load(profile.getProfilePictureUri(100, 100)).into(imageViewPictureProfile);


        }

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                if (currentProfile == null){
                    textViewId.setText("pas connecté");
                    imageViewPictureProfile.setImageBitmap(null);
                    return;
                }
                textViewId.setText(currentProfile.getName());
                Glide.with(MainActivity.this).load(currentProfile.getProfilePictureUri(50, 50)).into(imageViewPictureProfile);
            }
        };
        loginButton.setPublishPermissions(Arrays.asList("publish_actions"));;
        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();

        profileTracker.startTracking();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();

        profileTracker.stopTracking();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
