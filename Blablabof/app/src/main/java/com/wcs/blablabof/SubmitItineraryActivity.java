package com.wcs.blablabof;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class SubmitItineraryActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String ITINERARIES_CHILD = "itineraries";

    private EditText mEditTextSubmitDeparture;
    private EditText mEditTextSubmitDestination;
    private EditText mEditTextSubmitPrice;
    private EditText mEditTextSubmitDate;
    private EditText mEditTextSubmitHour;
    private Button mButtonSubmitItinerary;

    private FirebaseUser mUser;

    private Calendar mCalendar;
    private DatePickerDialog.OnDateSetListener mOnDateSetListener;
    private TimePickerDialog.OnTimeSetListener mOnTimeSetListener;

    DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_itinerary);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(ITINERARIES_CHILD);

        mEditTextSubmitDeparture = (EditText) findViewById(R.id.editTextSubmitDeparture);
        mEditTextSubmitDestination = (EditText) findViewById(R.id.editTextSubmitDestination);
        mEditTextSubmitPrice = (EditText) findViewById(R.id.editTextSubmitPrice);
        mEditTextSubmitDate = (EditText) findViewById(R.id.editTextSubmitDate);
        mEditTextSubmitHour = (EditText) findViewById(R.id.editTextSubmitHour);
        mButtonSubmitItinerary = (Button) findViewById(R.id.buttonSubmitItinerary);

        mEditTextSubmitDate.setOnClickListener(this);
        mEditTextSubmitHour.setOnClickListener(this);
        mButtonSubmitItinerary.setOnClickListener(this);

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

        mOnTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mCalendar.set(Calendar.MINUTE, minute);
                updateHour();
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
            case R.id.editTextSubmitDate:
                new DatePickerDialog(SubmitItineraryActivity.this,
                        mOnDateSetListener,
                        mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.editTextSubmitHour:
                new TimePickerDialog(SubmitItineraryActivity.this,
                        mOnTimeSetListener,
                        mCalendar.get(Calendar.HOUR_OF_DAY),
                        mCalendar.get(Calendar.MINUTE), true).show();
                break;
            case R.id.buttonSubmitItinerary:
                if(mEditTextSubmitDeparture.length() != 0
                        && mEditTextSubmitDestination.length() !=0
                        && mEditTextSubmitPrice.length() != 0
                        && mEditTextSubmitDate.length() != 0
                        && mEditTextSubmitHour.length() != 0){
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/YY HH:mm");
                    ItineraryModel itineraryModel = new ItineraryModel();
                    try {
                        itineraryModel = new ItineraryModel(
                                dateFormat.parse(mEditTextSubmitDate.getText().toString()+ " " + mEditTextSubmitHour.getText().toString()),
                                Integer.parseInt(mEditTextSubmitPrice.getText().toString()),
                                mEditTextSubmitDeparture.getText().toString(),
                                mEditTextSubmitDestination.getText().toString(),
                                mUser.getUid(),
                                mUser.getDisplayName()
                                );
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    mDatabaseReference.push().setValue(itineraryModel);
                    finish();
                }
                else{
                    Toast.makeText(this, R.string.all_field_required, Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateDate(){
        String format = "dd/MM/YY";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.FRANCE);
        mEditTextSubmitDate.setText(sdf.format(mCalendar.getTime()));

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateHour(){
        String format = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.FRANCE);
        mEditTextSubmitHour.setText(sdf.format(mCalendar.getTime()));

    }
}
