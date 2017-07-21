package patrimoine.wcs.fr.toulousemonuments;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.octo.android.robospice.GsonGoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.io.InputStream;
import java.util.Map;

import patrimoine.wcs.fr.toulousemonuments.models.MonumentModel;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    private GoogleMap mMap;
    private LocationManager mlocationManager = null;
    private LocationListener mlocationListener;
    private SpiceManager mSpiceManager;
    private int mPosition;
    private MonumentModel mMonument;
    private LatLng mLatLng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mSpiceManager = new SpiceManager(GsonGoogleHttpClientSpiceService.class);

        mlocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mlocationListener = new LocationListener() {


            @Override
            public void onLocationChanged(Location location) {
                mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 12.0f));
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
    protected void onPause() {
        super.onPause();
        mlocationManager.removeUpdates(mlocationListener);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if (ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mlocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mlocationListener);
        mlocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocationListener);
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);


            //    ActivityCompat#requestPermissions

            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mlocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mlocationListener);
        mlocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocationListener);

        mSpiceManager.start(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mSpiceManager.isStarted()){
            mSpiceManager.shouldStop();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);

        LatLng toulouse = new LatLng(43.600000, 1.433333);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(toulouse));
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        performRequest();

    }

    private void performRequest() {

        MonumentRequest monumentRequest = new MonumentRequest();
        mSpiceManager.execute(monumentRequest, MainActivity.CACHE, DurationInMillis.ONE_WEEK, new MapsActivity.MonumentRequestListener());
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        for (int i = 0; i < 20; i++){
            String monumentID = mMonument.getRecords().get(i).getFields().getNomCdt();
            if(marker.getTitle().equals(monumentID)){
                Intent intentToDescriptionActivity = new Intent(MapsActivity.this, DescriptionActivity.class);
                intentToDescriptionActivity.putExtra(MainActivity.POSITION, i);
                startActivity(intentToDescriptionActivity);
            }
        }
        return false;
    }

    private class MonumentRequestListener implements RequestListener<MonumentModel> {


        @Override
        public void onRequestFailure(SpiceException spiceException) {

        }

        @Override
        public void onRequestSuccess(MonumentModel monument) {
            mMonument = monument;
            for (int i = 0; i < 20; i++){
                String nameId = mMonument.getRecords().get(i).getFields().getNomCdt();
                LatLng latLng = new LatLng(
                        monument.getRecords().get(i).getFields().getGeoPoint2d().get(0),
                        monument.getRecords().get(i).getFields().getGeoPoint2d().get(1)
                );

                Marker currentMarker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(nameId));
                if (nameId.equals("LA BASILIQUE SAINT-SERNIN")){
                    currentMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                }
                else currentMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
            }

            Marker currentMarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(43.611378086546445d, 1.420810441929067d))
                    .title("Le canal du midi"));
            currentMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));


        }
    }
}


