package com.ab.appstudapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by apprenti on 11/05/17.
 */

public class PlacePhotoAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<PlaceModel> mListPlaces;

    //public constructor
    public PlacePhotoAdapter(Context context, ArrayList<PlaceModel> places) {
        mContext = context;
        mListPlaces = places;
    }

    @Override
    public int getCount() {
        return mListPlaces.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return mListPlaces.get(0); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).
                    inflate(R.layout.place_item, parent, false);
        }

        TextView textViewName = convertView.findViewById(R.id.textViewName);
        ImageView imageViewPhoto = convertView.findViewById(R.id.imageViewPhoto);

        textViewName.setText(mListPlaces.get(position).getmNames());
        imageViewPhoto.setImageBitmap(mListPlaces.get(position).getmPhotos());

        return convertView;
    }

}
