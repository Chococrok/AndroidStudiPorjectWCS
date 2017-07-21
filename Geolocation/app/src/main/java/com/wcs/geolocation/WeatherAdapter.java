package com.wcs.geolocation;

import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by apprenti on 11/05/17.
 */

public class WeatherAdapter extends BaseAdapter {

    public static final double TOKMPERHOUR = 3.6;
    public static final double KELVIN = 273.15;
    private Context context; //context
    private ArrayList<List> items; //data source of the list adapter

    //public constructor
    public WeatherAdapter(Context context, ArrayList<List> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return items.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.weather_item, parent, false);
        }

        // get current item to be displayed
        List currentListItem = (List) getItem(position);

        // get the TextView for item name and item description
        TextView textViewForecastHour = (TextView)
                convertView.findViewById(R.id.textViewForecastHour);
        TextView textViewHumidity = (TextView)
                convertView.findViewById(R.id.textViewHumidity);
        TextView textViewTemp = (TextView)
                convertView.findViewById(R.id.textViewTemp);
        TextView textViewWeatherDescription = (TextView)
                convertView.findViewById(R.id.textViewWeatherDescription);
        TextView textViewWindSpeed = (TextView)
                convertView.findViewById(R.id.textViewWindSpeed);

        //sets the text for item name and item description from the current item object

        textViewForecastHour.setText(getDataFormated(currentListItem.getDt()));
        textViewHumidity.setText(context.getString(R.string.humidity) + " : " + currentListItem.getMain().getHumidity().toString());
        textViewTemp.setText(context.getString(R.string.temp) + " : " + String.valueOf((int) (currentListItem.getMain().getTemp() - KELVIN)));
        textViewWeatherDescription.setText(context.getString(R.string.weather) + " : " + currentListItem.getWeather().get(0).getDescription());
        textViewWindSpeed.setText(context.getString(R.string.wind_speed) + " : " + String.valueOf((int) (currentListItem.getWind().getSpeed() * TOKMPERHOUR)));

        // returns the view for the current row
        return convertView;
    }

    public String getDataFormated(long milliSec){
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd/MM/yy HH:mm");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSec * 1000);

        return dateFormat.format(calendar.getTime());
    }
}
