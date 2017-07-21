package blablawild.fr.wcs.multiacvtivityquest;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.inputmethodservice.ExtractEditText;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class SearchItineraryActivity extends AppCompatActivity {

    private Button buttonResearch;
    private EditText mStart;
    private EditText mDestination;
    private EditText mDate;
    private Intent intentViewSearch;
    private Intent intentGoSearch;
    private Calendar mMyCalendar;
    private DatePickerDialog.OnDateSetListener mDatePicker;
    public final static String EXTRA_REQUEST = "search_request";


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_itinerary);

        buttonResearch = (Button) findViewById(R.id.buttonResearch);
        mStart = (EditText) findViewById(R.id.editStart);
        mDestination = (EditText) findViewById(R.id.editDestination);
        mDate = (EditText) findViewById(R.id.editDate);
        intentViewSearch = new Intent(this, ViewSearchItineraryResultsListActivity.class);



        mMyCalendar = Calendar.getInstance();
        mDatePicker = new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mMyCalendar.set(Calendar.YEAR, year);
                mMyCalendar.set(Calendar.MONTH, month);
                mMyCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();

            }
        };



        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SearchItineraryActivity.this, mDatePicker,
                        mMyCalendar.get(Calendar.YEAR), mMyCalendar.get(Calendar.MONTH), mMyCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        buttonResearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mStart.length() == 0 || mDestination.length() == 0){

                    Toast.makeText(getApplicationContext(), getString(R.string.toastResearch), Toast.LENGTH_SHORT).show();
                }

                else {

                    String start = mStart.getText().toString();
                    String destination = mDestination.getText().toString();
                    String date = mDate.getText().toString();
                    SearchRequestModel searchRequest = new SearchRequestModel(start, destination, date);
                    intentViewSearch.putExtra(EXTRA_REQUEST, searchRequest);

                    startActivity(intentViewSearch);
                }
            }
        });

        intentGoSearch = getIntent();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateLabel(){
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);

        mDate.setText(sdf.format(mMyCalendar.getTime()));
    }
}
