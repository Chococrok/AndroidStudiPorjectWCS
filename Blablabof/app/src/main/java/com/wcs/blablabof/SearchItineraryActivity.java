package com.wcs.blablabof;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

public class SearchItineraryActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String SEARCHREQUESTMODEL = "SearchRequestModel";

    private FirebaseUser mUser;

    private Button mButtonToViewSearchItineraryActivity;
    private EditText mEditTextDeparture;
    private EditText mEditTextDestination;
    private EditText mEditTextDate;
    private Calendar mCalendar;
    private DatePickerDialog.OnDateSetListener mOnDateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_itinerary);

        mButtonToViewSearchItineraryActivity = (Button) findViewById(R.id.buttonToViewSearchItineraryActivity);
        mButtonToViewSearchItineraryActivity.setOnClickListener(this);
        mEditTextDeparture = (EditText) findViewById(R.id.editTextDeparture);
        mEditTextDestination = (EditText) findViewById(R.id.editTextDestination);
        mEditTextDate = (EditText) findViewById(R.id.editTextDate);
        mEditTextDate.setOnClickListener(this);

        mCalendar = Calendar.getInstance();
        mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, month);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate();
            }
        };
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonToViewSearchItineraryActivity:
                if (mEditTextDestination.length() != 0 && mEditTextDeparture.length() != 0) {

                    DateFormat dateFormat = new SimpleDateFormat("dd/mm/YY", Locale.FRANCE);
                    SearchRequestModel newSearchRequestModel = new SearchRequestModel();
                    try {
                        newSearchRequestModel = new SearchRequestModel(mEditTextDeparture.getText().toString(),
                                mEditTextDestination.getText().toString(),
                                dateFormat.parse(mEditTextDate.getText().toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Intent intentToViewSearchItineraryResultsListActivity = new Intent(SearchItineraryActivity.this, ViewSearchItineraryResultsListActivity.class);
                    intentToViewSearchItineraryResultsListActivity.putExtra(SEARCHREQUESTMODEL, newSearchRequestModel);
                    startActivity(intentToViewSearchItineraryResultsListActivity);
                }
                else {
                    Toast.makeText(this, R.string.toast_departure_destination_needed, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.editTextDate:
                new DatePickerDialog(SearchItineraryActivity.this,
                        mOnDateSetListener,
                        mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateDate(){
        String format = "dd/MM/YY";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.FRANCE);
        mEditTextDate.setText(sdf.format(mCalendar.getTime()));

    }
}
