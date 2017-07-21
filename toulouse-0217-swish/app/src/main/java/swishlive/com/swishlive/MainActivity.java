package swishlive.com.swishlive;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private TextView textViewUserName;
    private ProfileTracker profileTracker;
    private Profile userProfile;
    private ImageView imageViewProfilePicture;
    private ImageButton imageButtonStartBroadcast;
    private final static String TAG=".MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        loginButton = (LoginButton) findViewById(R.id.loginButton);

        imageButtonStartBroadcast = (ImageButton) findViewById(R.id.imageButtonStartBroadcast);
        imageButtonStartBroadcast.setOnClickListener(this);
        textViewUserName = (TextView) findViewById(R.id.textViewUserName);
        imageViewProfilePicture = (ImageView) findViewById(R.id.imageViewProfilePicture);
        userProfile = Profile.getCurrentProfile();

        if (userProfile != null) {

            textViewUserName.setText(userProfile.getFirstName() + " " + userProfile.getLastName());
            String userPictureUri = userProfile.getProfilePictureUri(50, 50).toString();
            Glide.with(MainActivity.this)
                    .load(userPictureUri)
                    .centerCrop()
                    .into(imageViewProfilePicture);
        }

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

                if (currentProfile != null) {
                    userProfile = currentProfile;
                    textViewUserName.setText(userProfile.getFirstName() + " " + userProfile.getLastName());
                    String userPictureUri = userProfile.getProfilePictureUri(50, 50).toString();
                    Glide.with(MainActivity.this)
                            .load(userPictureUri)
                            .centerCrop()
                            .into(imageViewProfilePicture);
                } else {

                    clearUserView();
                }


            }
        };

        profileTracker.startTracking();

        callbackManager = CallbackManager.Factory.create();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop(){
        super.onStop();

        profileTracker.stopTracking();

    }

    protected void clearUserView() {

        textViewUserName.setText(null);
        Glide.with(MainActivity.this)
                .load(R.drawable.chameleon_swish)
                .into(imageViewProfilePicture);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.imageButtonStartBroadcast :
                if(userProfile==null){
                    Toast.makeText(MainActivity.this, R.string.notConnected,Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(new Intent(MainActivity.this, MatchConfigurationActivity.class));
        }
    }
}
