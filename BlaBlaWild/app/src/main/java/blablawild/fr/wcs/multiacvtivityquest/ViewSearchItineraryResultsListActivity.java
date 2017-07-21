package blablawild.fr.wcs.multiacvtivityquest;

import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ViewSearchItineraryResultsListActivity extends AppCompatActivity {

    ItineraryAdapter itineraryAdapter;
    ListView listViewSearchResults;

    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_search_itinerary_results_list);

        ref = FirebaseDatabase.getInstance().getReference("Itineraries");

        /*Intent intentFromSearchItineraryActivity = getIntent();
        SearchRequestModel searchRequestModel = intentFromSearchItineraryActivity.getParcelableExtra(SearchItineraryActivity.EXTRA_REQUEST);

        setTitle(searchRequestModel.getStart() + " >> " + searchRequestModel.getArrival());
        Toast.makeText(this, searchRequestModel.getDate(), Toast.LENGTH_SHORT).show();*/

        this.listViewSearchResults = (ListView) findViewById(R.id.ListViewSearchResults);

    }


    @Override
    public void onStart() {
        super.onStart();
        // Setup our view and list adapter. Ensure it scrolls to the bottom as data changes
        //final ListView listView = getListView();
        // Tell our list adapter that we only want 50 messages at a time
        this.itineraryAdapter = new ItineraryAdapter(ref, R.layout.trip_item, this);
        this.listViewSearchResults.setAdapter(this.itineraryAdapter);
        itineraryAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listViewSearchResults.setSelection(itineraryAdapter.getCount() - 1);
            }
        });/*

        // Finally, a little indication of connection status
        mConnectedListener = mFirebaseRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean) dataSnapshot.getValue();
                if (connected) {
                    Toast.makeText(MainActivity.this, "Connected to Firebase", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Disconnected from Firebase", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // No-op
            }
        });*/
    }
}
