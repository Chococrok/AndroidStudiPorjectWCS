package com.ab.wildchallenge;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.Query;

/**
 * Created by apprenti on 13/07/17.
 */

public class TeamRankingAdapter extends FirebaseListAdapter<TeamModel>{
    /**
     * @param mRef        The Firebase location to watch for data changes. Can also be a slice of a location, using some
     *                    combination of <code>limit()</code>, <code>startAt()</code>, and <code>endAt()</code>,
     * @param mModelClass Firebase will marshall the data at a location into an instance of a class that you provide
     * @param mLayout     This is the mLayout used to represent a single list item. You will be responsible for populating an
     *                    instance of the corresponding view with the data from an instance of mModelClass.
     * @param activity    The activity containing the ListView
     */
    public TeamRankingAdapter(Query mRef, Class<TeamModel> mModelClass, int mLayout, Activity activity) {
        super(mRef, mModelClass, mLayout, activity);
    }

    @Override
    protected void populateView(View v, TeamModel model) {
        TextView textViewListNickName = v.findViewById(R.id.textViewListNickName);
        textViewListNickName.setText(model.getName());
        TextView textViewListScore = v.findViewById(R.id.textViewListScore);
        textViewListScore.setText(String.valueOf(model.getMeanScore()));
    }
}