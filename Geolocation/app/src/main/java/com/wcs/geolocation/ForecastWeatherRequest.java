package com.wcs.geolocation;

import android.util.Log;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.json.gson.GsonFactory;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

import roboguice.util.temp.Ln;

/**
 * Created by apprenti on 11/05/17.
 */

public class ForecastWeatherRequest extends GoogleHttpClientSpiceRequest<ForecastWeatherModel> {

    public final String TAG = "ForecastWeatherRequest";

    private String baseUrl;

    public ForecastWeatherRequest(double lat, double lng, String apiKey) {
        super(ForecastWeatherModel.class);
        this.baseUrl = "http://api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lng + "&appid=" + apiKey;
    }

    @Override
    public ForecastWeatherModel loadDataFromNetwork() throws Exception {
        Log.d(TAG, "ForecastWeatherModel loaded");

        GenericUrl url = new GenericUrl(this.baseUrl);

        ForecastWeatherModel forecastWeather = getHttpRequestFactory()
                .buildGetRequest(url).setParser(new GsonFactory().createJsonObjectParser())
                .execute()
                .parseAs(getResultType());
        return forecastWeather;
    }
}
