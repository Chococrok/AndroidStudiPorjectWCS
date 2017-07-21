package blablawild.fr.wcs.multiacvtivityquest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "MainActivity";

    private Button buttonToViewSearchItineraryResults;
    private Button buttonPropose;
    private Button buttonProfil;
    
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        buttonToViewSearchItineraryResults = (Button) findViewById(R.id.buttonToViewSearchItineraryResults);
        buttonToViewSearchItineraryResults.setOnClickListener(this);
        buttonPropose = (Button) findViewById(R.id.buttonPropose);
        buttonPropose.setOnClickListener(this);
        buttonProfil = (Button) findViewById(R.id.buttonProfil);
        buttonProfil.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mFirebaseUser = firebaseAuth.getCurrentUser();
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonPropose:
                if (mFirebaseUser != null) {
                    Intent intentToSubmitItineraryActivity = new Intent(this, SubmitItineraryActivity.class);
                    startActivity(intentToSubmitItineraryActivity);
                }
                else {
                    Toast.makeText(this, R.string.toast_autnetication_needed, Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.buttonToViewSearchItineraryResults:
                Intent intentToViewSearchItineraryResultsListActivity = new Intent(this, ViewSearchItineraryResultsListActivity.class);
                startActivity(intentToViewSearchItineraryResultsListActivity);
                break;

            case  R.id.buttonProfil:
                Intent intentToAccountActivity = new Intent(this, AccountActivity.class);
                startActivity(intentToAccountActivity);
                break;

        }
    }
}
