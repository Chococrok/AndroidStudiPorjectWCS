package matterless.fr.wcs.matterless;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.Query;


public class CustomListAdapter extends FirebaseListAdapter<Message> {

    public CustomListAdapter (Query ref, Activity activity, int layout) {
        super (ref, Message.class, layout, activity );
    }

    /* Custom List Adapter classique avec un text view que je rempli avec le titre du message */
    @Override
    protected void populateView(View v, Message model) {

        TextView textViewListItemName = (TextView) v.findViewById(R.id.textViewMessageName);

        textViewListItemName.setText(model.getmName());

    }
}
