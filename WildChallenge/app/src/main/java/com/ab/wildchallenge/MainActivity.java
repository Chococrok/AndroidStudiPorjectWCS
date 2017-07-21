package com.ab.wildchallenge;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ClickerFragment.OnFragmentClickerInteractionListener,
        RankingFragment.OnFragmentInteractionListener{

    private static final String TAG = "MainActivity";
    static final int CLIKER_FRAGMENT = 0;
    static final int RANKING_FRAGMENT_ALL = 1;
    static final int RANKING_FRAGMENT_TEAM = 2;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            if (firebaseAuth.getCurrentUser() == null){
                startActivity(new Intent(MainActivity.this, LogActivity.class));

            } else {
                mUser = firebaseAuth.getCurrentUser();

                mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), mUser.getUid());
                mViewPager.setAdapter(mSectionsPagerAdapter);



                mTextViewDrawerNickName.setText(mUser.getDisplayName());
                mTextViewDrawerEmail.setText(mUser.getEmail());
                mStorage = FirebaseStorage.getInstance().getReference(LogActivity.STORAGE_REF_PROFILE_PICTURE);
                mStorage.child(mUser.getUid()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            /*Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_menu_camera);
                            mImageViewDrawer.setImageBitmap(bitmap);*/
                            Glide.with(MainActivity.this)
                                    .load(task.getResult())
                                    .centerCrop()
                                    .into(mImageViewDrawer);
                        }
                    }
                });

            }
        }
    };
    private StorageReference mStorage;
    private FirebaseUser mUser;

    private ImageView mImageViewDrawer;
    private TextView mTextViewDrawerNickName;
    private TextView mTextViewDrawerEmail;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFirebaseAuth = FirebaseAuth.getInstance();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navH = navigationView.getHeaderView(0);
        mImageViewDrawer = (ImageView) navH.findViewById(R.id.imageViewDrawer);
        mTextViewDrawerNickName = (TextView) navH.findViewById(R.id.textViewDrawerNickName);
        mTextViewDrawerEmail = (TextView) navH.findViewById(R.id.textViewDrawerEmail);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.viewPagerMain);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_log_out:
                mFirebaseAuth.signOut();
                return true;
            case R.id.action_view_profile:
                startActivity(new Intent(MainActivity.this, LogActivity.class));
                return true;
        }

        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mFirebaseAuth.removeAuthStateListener(mAuthListener);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            mViewPager.setCurrentItem(CLIKER_FRAGMENT);

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentClickerInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
