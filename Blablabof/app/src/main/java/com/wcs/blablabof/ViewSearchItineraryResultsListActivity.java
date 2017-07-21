package com.wcs.blablabof;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ViewSearchItineraryResultsListActivity extends AppCompatActivity{

    private FirebaseUser mUser;

    private ListView mListViewResults;
    private DatabaseReference mDatabaseReference;
    private ValueEventListener mConnectedListener;
    private TripResultAdapter mResultsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_search_itinerary_results_list);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(SubmitItineraryActivity.ITINERARIES_CHILD);
        mResultsAdapter = new TripResultAdapter(mDatabaseReference, ItineraryModel.class, R.layout.trip_item, this);

        mListViewResults = (ListView) findViewById(R.id.ListViewTrip);
        mListViewResults.setAdapter(mResultsAdapter);

        /*ArrayList<TripResultModel> results = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy-hh:mm");

        try {
            results.add(new TripResultModel("Bruce", sdf.parse("21/02/2017-15:30"), 15));
            results.add(new TripResultModel("Clark", sdf.parse("21/02/2017-16:00"), 20));
            results.add(new TripResultModel("Bary", sdf.parse("21/02/2017-16:30"), 16));
            results.add(new TripResultModel("Lex", sdf.parse("21/02/2017-17:00"), 40));
        } catch (ParseException e) {
        }
        mResultsAdapter = new TripResultAdapter(this, results);

        mListViewResults.setAdapter(mResultsAdapter);*/

        Intent intentFromSearchItineraryActivity = getIntent();
        SearchRequestModel searchRequestModel = intentFromSearchItineraryActivity.getParcelableExtra(SearchItineraryActivity.SEARCHREQUESTMODEL);
        setTitle(searchRequestModel.getDeparture()
                + "  ---->  "
                + searchRequestModel.getDestination());

        Toast.makeText(this, searchRequestModel.getDate().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (mUser != null) {
            menu.findItem(R.id.sign_in).setTitle(mUser.getEmail());
        }
        return true;
    }
}
