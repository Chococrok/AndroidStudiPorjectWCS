package com.wcs.geolocation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.GsonGoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    public static final int MY_REQUEST_FOR_LOCATION = 1;
    public static final String TAG = "MainActivity";

    private LocationManager mLocationManager;
    private LocationListener mLocationListener;

    private double mLat;
    private double mLng;

    private String mApiKey;

    protected SpiceManager spiceManager = new SpiceManager(GsonGoogleHttpClientSpiceService.class);

    private ListView mListViewWeather;
    private TextView mTextViewCurrentCity;
    private TextView mTextViewCurrentWeather;
    private TextView mTextViewCurrentTemp;
    private WeatherAdapter mWeatherAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mApiKey = getResources().getString(R.string.api_key);

        mListViewWeather = (ListView) findViewById(R.id.listViewWeather);

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mLat = location.getLatitude();
                mLng = location.getLongitude();
                Toast.makeText(MainActivity.this, "Lat: " + location.getLatitude() + ", Long: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
                performRequest();
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_REQUEST_FOR_LOCATION);
        }
        else {
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TimeUnit.HOURS.toMillis(1), 0, mLocationListener);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, TimeUnit.HOURS.toMillis(1), 0, mLocationListener);
        }
        spiceManager.start(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mLocationManager.removeUpdates(mLocationListener);
        spiceManager.shouldStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);

    }

    private void performRequest() {
        MainActivity.this.setProgressBarIndeterminateVisibility(true);

        CurrentWeatherRequest currentWeatherRequest = new CurrentWeatherRequest(mLat, mLng, mApiKey);
        spiceManager.execute(currentWeatherRequest, new CurrentWeatherRequestListener());

        ForecastWeatherRequest forecastWeatherRequest = new ForecastWeatherRequest(mLat, mLng, mApiKey);
        spiceManager.execute(forecastWeatherRequest, new ForecastWeatherRequestListener());
    }

    private class CurrentWeatherRequestListener implements RequestListener<CurrentWeatherModel> {

        @Override
        public void onRequestFailure(SpiceException e) {
            //update your UI
            Log.d(TAG, e.getMessage());
        }

        @Override
        public void onRequestSuccess(CurrentWeatherModel currentWeatherModel) {
            mTextViewCurrentCity = (TextView) findViewById(R.id.textViewCurrentCity);
            mTextViewCurrentCity.setText(currentWeatherModel.getName());

            mTextViewCurrentWeather = (TextView) findViewById(R.id.textViewCurrentWeather);
            mTextViewCurrentWeather.setText(getString(R.string.current_weather) + " : " + currentWeatherModel.getWeather().get(0).getDescription());

            mTextViewCurrentTemp = (TextView) findViewById(R.id.textViewCurrentTemp);
            mTextViewCurrentTemp.setText(getString(R.string.current_temp) + " : " +String.valueOf((int)(currentWeatherModel.getMain().getTemp() - WeatherAdapter.KELVIN)));

            Log.d(TAG, currentWeatherModel.getWind().getSpeed().toString());

        }
    }

    private class ForecastWeatherRequestListener implements RequestListener<ForecastWeatherModel> {

        @Override
        public void onRequestFailure(SpiceException e) {
            //update your UI
            Log.d(TAG, e.getMessage());
        }

        @Override
        public void onRequestSuccess(ForecastWeatherModel forecastWeatherModel) {

            ArrayList<List> results = new ArrayList<>(forecastWeatherModel.getList());
            mWeatherAdapter = new WeatherAdapter(MainActivity.this, results);
            mListViewWeather.setAdapter(mWeatherAdapter);
            Log.d(TAG, forecastWeatherModel.getCity().getName());

        }
    }
}
