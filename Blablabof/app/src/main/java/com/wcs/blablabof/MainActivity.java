package com.wcs.blablabof;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button searchNewItinerary;
    private Button createNewItinerary;
    private Menu mMenu;

    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        searchNewItinerary = (Button) findViewById(R.id.buttonSearchItinerary);
        createNewItinerary = (Button) findViewById(R.id.buttonCreateItinerary);
        searchNewItinerary.setOnClickListener(this);
        createNewItinerary.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        invalidateOptionsMenu();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mMenu = menu;

        MenuItem menuItemConnexion = mMenu.findItem(R.id.sign_in);
        if (mUser != null) {
            menuItemConnexion.setTitle(mUser.getEmail());
        }
        else{
            menuItemConnexion.setTitle(getString(R.string.sign_in));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sign_in) {

            startActivity(new Intent(MainActivity.this, AccountActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonSearchItinerary:
                if(mUser != null) {
                    Intent intentToSearchItineraryActivity = new Intent(MainActivity.this, SearchItineraryActivity.class);
                    startActivity(intentToSearchItineraryActivity);
                }
                else {
                    Toast.makeText(this, R.string.toast_sign_in_needed, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.buttonCreateItinerary:
                if(mUser != null) {
                    Intent intentToSubmitItineraryActivity = new Intent(MainActivity.this, SubmitItineraryActivity.class);
                    startActivity(intentToSubmitItineraryActivity);
                }
                else {
                    Toast.makeText(this, R.string.toast_sign_in_needed, Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
}
