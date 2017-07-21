package matterless.fr.wcs.matterless;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


import pl.bclogic.pulsator4droid.library.PulsatorLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String FILE_NAME = "FILE_NAME";
    public static final String TAG = "MainActivity";

    private Button buttonConfigureEvents;
    private Button buttonMyProfile;
    private ImageView imageViewBigButtonMainActivity;
    private UserCredentials muserCredentials;
    private PulsatorLayout pulsator;

    private boolean botLaunched;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonConfigureEvents = (Button) findViewById(R.id.buttonConfigureEvents);
        buttonConfigureEvents.setOnClickListener(this);
        buttonMyProfile = (Button)findViewById(R.id.buttonMyProfile);
        buttonMyProfile.setOnClickListener(this);
        imageViewBigButtonMainActivity = (ImageView) findViewById(R.id.imageViewBigButtonMainActivity);
        imageViewBigButtonMainActivity.setOnClickListener(this);

        pulsator = (PulsatorLayout) findViewById(R.id.pulsator);
        pulsator.start();

        botLaunched = true;

        muserCredentials = new UserCredentials();
    }

    @Override
    protected void onResume() {
        super.onResume();

        muserCredentials = UserCredentials.fromFile(this, FILE_NAME);
        if(muserCredentials == null){
            Intent intentToSettingsActivity = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intentToSettingsActivity);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.buttonConfigureEvents:
                if(muserCredentials != null){
                    Intent intentToSettings = new Intent(MainActivity.this, MessageListActivity.class);
                    startActivity(intentToSettings);
                }
                else {
                    Toast.makeText(this, R.string.authentication_needed, Toast.LENGTH_SHORT).show();
                }
                break;


            case R.id.buttonMyProfile:
                Intent intentToSettings = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intentToSettings);
                break;


            case R.id.imageViewBigButtonMainActivity:
                if (botLaunched) {
                    Intent toService = new Intent(MainActivity.this, MyService.class);
                    toService.setAction(MyService.INTENT_STOP_BOT);
                    startService(toService);
                    imageViewBigButtonMainActivity.setImageDrawable(getResources().getDrawable(R.drawable.red));
                    pulsator.stop();
                    botLaunched = false;
                }
                else{
                    Intent toService = new Intent(MainActivity.this, MyService.class);
                    toService.setAction(MyService.INTENT_START_BOT);
                    startService(toService);
                    imageViewBigButtonMainActivity.setImageDrawable(getResources().getDrawable(R.drawable.green));
                    pulsator.start();
                    botLaunched = true;
                }
                break;
        }
    }
}
