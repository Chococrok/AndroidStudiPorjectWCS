package com.ab.appstudapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, ListFragment.OnFragmentInteractionListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    public static final int PLACE_PICKER_REQUEST = 1;
    public static final String TAG = "MainActivity.this";
    public static final int MAP_FRAGMENT = 0;
    public static final int LIST_FRAGMENT = 1;

    private int currentFragment = -1;

    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private ArrayList<PlaceModel> mPlaces = new ArrayList<>();
    private ProgressDialog mLoadingDialog;


    private LocationManager mlocationManager;
    private LocationListener mlocationListener;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    setMapFragment();
                    return true;

                case R.id.navigation_dashboard:
                    setListFragment();
                    return true;
            }
            return false;
        }

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.lookAround:
                getPlacesAround();
                return true;
            case R.id.searchPlaces:
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(MainActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                String id = place.getId();
                String name = place.getName().toString();
                LatLng position = place.getLatLng();

                mLoadingDialog.show();
                mPlaces.clear();
                if (currentFragment == MAP_FRAGMENT){
                    mMap.clear();
                }
                getPlacePhotos(id, name, position);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mLoadingDialog = new ProgressDialog(this);
        mLoadingDialog.setMessage(getString(R.string.loading_photos));

        mlocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mlocationListener = new LocationListener() {


            @Override
            public void onLocationChanged(Location location) {
                Log.i(TAG, "position updated");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {


            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {


            }

        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        setMapFragment();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            return;
        }
        mlocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TimeUnit.SECONDS.toMillis(30), 0, mlocationListener);
        mlocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TimeUnit.SECONDS.toMillis(30), 0, mlocationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mlocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TimeUnit.SECONDS.toMillis(30), 0, mlocationListener);
        mlocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TimeUnit.SECONDS.toMillis(30), 0, mlocationListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mlocationManager.removeUpdates(mlocationListener);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        //TODO lastKnowLocation should not be null
        /*LatLng lastKnownLocation = new LatLng((mlocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude()),
                mlocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(lastKnownLocation));*/
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        getPlacesAround();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.getErrorMessage().equals(ConnectionResult.NETWORK_ERROR)) {
            Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void getPlacesAround() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            return;
        }
        if(currentFragment == MAP_FRAGMENT){
            mMap.clear();
        }
        mLoadingDialog.show();
        PendingResult<PlaceLikelihoodBuffer> result =
                Places.PlaceDetectionApi.getCurrentPlace(mGoogleApiClient, new PlaceFilter(false, null));
        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(@NonNull PlaceLikelihoodBuffer placeLikelihoodBuffer) {
                for (PlaceLikelihood placeLikelihood : placeLikelihoodBuffer) {
                    String id = placeLikelihood.getPlace().getId();
                    String name = placeLikelihood.getPlace().getName().toString();
                    LatLng position = placeLikelihood.getPlace().getLatLng();
                    getPlacePhotos(id, name, position);
                }
                placeLikelihoodBuffer.release();
            }
        });
    }

    private void getPlacePhotos(String id, final String name, final LatLng position){
        Places.GeoDataApi.getPlacePhotos(mGoogleApiClient, id).setResultCallback(new ResultCallback<PlacePhotoMetadataResult>() {
            @Override
            public void onResult(@NonNull PlacePhotoMetadataResult placePhotoMetadataResult) {
                PlacePhotoMetadataBuffer photoMetadataBuffer = placePhotoMetadataResult.getPhotoMetadata();
                setPlacePhoto(photoMetadataBuffer, name, position);
            }
        });
    }

    private void setPlacePhoto(PlacePhotoMetadataBuffer photoMetadataBuffer, final String name, final LatLng position){
        if (photoMetadataBuffer != null && photoMetadataBuffer.getCount() > 0) {
            if (currentFragment == MAP_FRAGMENT) {
                photoMetadataBuffer.get(0).getScaledPhoto(mGoogleApiClient, 200, 200)
                        .setResultCallback(new ResultCallback<PlacePhotoResult>() {
                            @Override
                            public void onResult(@NonNull PlacePhotoResult placePhotoResult) {
                                Bitmap currentPhoto = getCroppedBitmap(placePhotoResult.getBitmap());
                                if (currentFragment == MAP_FRAGMENT) {
                                    mMap.addMarker(new MarkerOptions()
                                            .position(position)
                                            .icon(BitmapDescriptorFactory.fromBitmap(currentPhoto))
                                            .title(name));
                                    mLoadingDialog.dismiss();
                                    return;
                                }

                            }
                        });
            }
            else{
                photoMetadataBuffer.get(0).getScaledPhoto(mGoogleApiClient, 1000, 1000)
                        .setResultCallback(new ResultCallback<PlacePhotoResult>() {
                            @Override
                            public void onResult(@NonNull PlacePhotoResult placePhotoResult) {
                                mPlaces.add(new PlaceModel(placePhotoResult.getBitmap(), name));

                                //TODO need a listener or something that send data to the adapter directly
                                // Here a fragment replace every time there is a new photo. Not satisfying.
                                ListFragment listFragment = ListFragment.newInstance(mPlaces);
                                FragmentTransaction fragmentTransaction =
                                        getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.container, listFragment);
                                fragmentTransaction.commit();
                                mLoadingDialog.dismiss();
                            }
                        });
            }

        }
        photoMetadataBuffer.release();
    }

    private void setMapFragment(){
        if (currentFragment == MAP_FRAGMENT){
            return;
        }
        mMapFragment = SupportMapFragment.newInstance();
        mMapFragment.getMapAsync(MainActivity.this);
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, mMapFragment);
        fragmentTransaction.commit();

        currentFragment = MAP_FRAGMENT;
    }

    private void setListFragment(){
        if (currentFragment == LIST_FRAGMENT){
            return;
        }
        mPlaces.clear();
        getPlacesAround();
        currentFragment = LIST_FRAGMENT;
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(120,
                120, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = Color.BLACK;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, 120, 120);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(120 / 2, 120 / 2,
                100 / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return drawBlackCircle(output);
    }

    public Bitmap drawBlackCircle(Bitmap bitmap) {
        Canvas canvas = new Canvas(bitmap);

        final int color = Color.BLACK;
        final Paint paint = new Paint();

        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(120 / 2, 120 / 2, 120 / 2, paint);
        return bitmap;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
