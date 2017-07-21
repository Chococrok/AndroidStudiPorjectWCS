package com.wcs.blablabof;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.Query;

import java.util.ArrayList;

/**
 * Created by apprenti on 04/05/17.
 */

public class TripResultAdapter extends FirebaseListAdapter<ItineraryModel> {


    /**
     * @param mRef        The Firebase location to watch for data changes. Can also be a slice of a location, using some
     *                    combination of <code>limit()</code>, <code>startAt()</code>, and <code>endAt()</code>,
     * @param mModelClass Firebase will marshall the data at a location into an instance of a class that you provide
     * @param mLayout     This is the mLayout used to represent a single list item. You will be responsible for populating an
     *                    instance of the corresponding view with the data from an instance of mModelClass.
     * @param activity    The activity containing the ListView
     */
    public TripResultAdapter(Query mRef, Class mModelClass, int mLayout, Activity activity) {
        super(mRef, mModelClass, mLayout, activity);
    }

    @Override
    protected void populateView(View v, ItineraryModel model) {

        TextView textViewName = (TextView) v.findViewById(R.id.textViewName);
        TextView textViewPrice = (TextView) v.findViewById(R.id.textViewPrice);
        TextView textViewDate = (TextView) v.findViewById(R.id.textViewDate);

        textViewName.setText(model.getNickName());
        textViewPrice.setText(String.valueOf(model.getPrice()));
        textViewDate.setText(model.getDepartureDate().toString());

    }
}
