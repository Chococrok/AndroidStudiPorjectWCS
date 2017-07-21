package patrimoine.wcs.fr.toulousemonuments;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.octo.android.robospice.GsonGoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;

import patrimoine.wcs.fr.toulousemonuments.models.MonumentModel;
import patrimoine.wcs.fr.toulousemonuments.models.Record;

public class MainActivity extends AppCompatActivity {

    public static final String POSITION = "positon";
    public static final String CACHE = "cache";
    private FirebaseDatabase firebaseDatabase;
    private SpiceManager mSpiceManager;
    private ListView mListViewRecord;
    private ImageButton mImageButtonToMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageButtonToMap = (ImageButton) findViewById(R.id.imageButtonToMap);
        mImageButtonToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
            }
        });

        mListViewRecord = (ListView) findViewById(R.id.listViewRecord);
        mListViewRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentDescriptionActivity = new Intent(MainActivity.this, DescriptionActivity.class);
                intentDescriptionActivity.putExtra(POSITION, position);
                startActivity(intentDescriptionActivity);
            }
        });

        mSpiceManager = new SpiceManager(GsonGoogleHttpClientSpiceService.class);


    }


    @Override
    protected void onStart() {
        super.onStart();
        mSpiceManager.start(this);
        performRequest();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSpiceManager.shouldStop();
    }

    private void performRequest() {

        MonumentRequest monumentRequest = new MonumentRequest();
        mSpiceManager.execute(monumentRequest, MainActivity.CACHE, DurationInMillis.ONE_WEEK, new MainActivity.MonumentRequestListener());
    }

    private class MonumentRequestListener implements RequestListener<MonumentModel> {

        @Override
        public void onRequestFailure(SpiceException e) {
            //update your UI
            //Log.d(TAG, e.getMessage());
        }

        @Override
        public void onRequestSuccess(MonumentModel monumentModel) {
            ArrayList<Record> recordArrayList = new ArrayList<>(monumentModel.getRecords());
            MonumentAdapter monumentAdapter = new MonumentAdapter(MainActivity.this, recordArrayList);
            mListViewRecord.setAdapter(monumentAdapter);

            /*ArrayList<Record> results = new ArrayList<>(monumentModel.getRecords());
            mWeatherAdapter = new WeatherAdapter(MainActivity.this, results);
            mListViewWeather.setAdapter(mWeatherAdapter);
            Log.d(TAG, forecastWeatherModel.getCity().getName());*/

        }
    }
}
