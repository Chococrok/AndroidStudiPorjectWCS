package blablawild.fr.wcs.multiacvtivityquest;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import android.content.res.Resources;

import com.google.firebase.database.Query;

/**
 * Created by apprenti on 14/03/17.
 */

public class ItineraryAdapter extends FirebaseListAdapter<ItineraryModel> {

    private TextView textViewDeparture;
    private TextView textViewDestination;
    private TextView textViewDepartureTime;
    private TextView textViewPrice;
    private TextView textViewNames;

    /**
     * @param mRef        The Firebase location to watch for data changes. Can also be a slice of a location, using some
     *                    combination of <code>limit()</code>, <code>startAt()</code>, and <code>endAt()</code>
     * @param mLayout     This is the mLayout used to represent a single list item. You will be responsible for populating an
     *                    instance of the corresponding view with the data from an instance of mModelClass.
     * @param activity    The activity containing the ListView
     */
    public ItineraryAdapter(Query mRef, int mLayout, Activity activity) {
        super(mRef, ItineraryModel.class, mLayout, activity);
    }

    @Override
    protected void populateView(View v, ItineraryModel model) {


        textViewDeparture = (TextView) v.findViewById(R.id.textViewDeparture);
        textViewDestination = (TextView) v.findViewById(R.id.textViewDestination);
        textViewDepartureTime = (TextView) v.findViewById(R.id.textViewDepartureTime);
        textViewPrice = (TextView) v.findViewById(R.id.textViewPrice);
        textViewNames = (TextView) v.findViewById(R.id.textViewNames);

        textViewDeparture.setText(model.getDeparture());
        textViewDestination.setText(model.getDestination());
        textViewDepartureTime.setText(model.getDepartureDateString().toString());
        textViewPrice.setText(Integer.toString(model.getPrice()));
        textViewNames.setText(model.getUserName());
    }

}
