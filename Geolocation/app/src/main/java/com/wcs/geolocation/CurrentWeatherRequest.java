package com.wcs.geolocation;

import android.util.Log;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.json.gson.GsonFactory;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

import java.net.URL;

import roboguice.util.temp.Ln;

/**
 * Created by apprenti on 11/05/17.
 */

public class CurrentWeatherRequest extends GoogleHttpClientSpiceRequest<CurrentWeatherModel> {

    public final String TAG = "ForecastWeatherRequest";

    private String baseUrl;

    public CurrentWeatherRequest(double lat, double lng, String apiKey) {

        super(CurrentWeatherModel.class);
        this.baseUrl = "http://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lng + "&appid=" + apiKey;
    }

    @Override
    public CurrentWeatherModel loadDataFromNetwork() throws Exception {
        Log.d(TAG, "ForecastWeatherModel loaded");

        GenericUrl url = new GenericUrl(this.baseUrl);

        CurrentWeatherModel currentWeatherModel = getHttpRequestFactory()
                .buildGetRequest(url).setParser(new GsonFactory().createJsonObjectParser())
                .execute()
                .parseAs(getResultType());
        return currentWeatherModel;
    }
}
