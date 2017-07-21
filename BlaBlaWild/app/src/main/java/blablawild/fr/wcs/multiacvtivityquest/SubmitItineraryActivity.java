package blablawild.fr.wcs.multiacvtivityquest;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SubmitItineraryActivity extends AppCompatActivity {

    private EditText editTextDeparture;
    private EditText editTextDestination;
    private EditText editTextPrice;
    private EditText editTextDate;
    private EditText editTextHour;
    private Date chosenDate;
    private Button buttonSubmitSearch;
    private FirebaseUser mfirebaseUser;

    private Calendar mMyCalendar;
    private DatePickerDialog.OnDateSetListener mDatePicker;

    private ItineraryModel itineraryModel;
    private DatabaseReference refItinerary;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_itinerary);

        mfirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        editTextDeparture = (EditText) findViewById(R.id.editTextDeparture);
        editTextDestination = (EditText) findViewById(R.id.editTextDestination);
        editTextPrice = (EditText) findViewById(R.id.editTextPrice);
        editTextDate = (EditText) findViewById(R.id.editTextDate);
        editTextHour = (EditText) findViewById(R.id.editTextHour);
        buttonSubmitSearch = (Button) findViewById(R.id.buttonSubmitSearch);

        refItinerary = FirebaseDatabase.getInstance().getReference("Itineraries");

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

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SubmitItineraryActivity.this, mDatePicker,
                        mMyCalendar.get(Calendar.YEAR), mMyCalendar.get(Calendar.MONTH), mMyCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        buttonSubmitSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextDestination .length() != 0 && editTextDeparture.length() != 0 && editTextHour.length() != 0 && editTextDate.length() != 0 && editTextPrice.length() != 0 ) {


                    itineraryModel = new ItineraryModel(mfirebaseUser.getUid(), editTextDate.getText().toString() +"  "+ editTextHour.getText().toString(), Integer.valueOf(editTextPrice.getText().toString()), editTextDeparture.getText().toString(),
                            editTextDestination.getText().toString(), mfirebaseUser.getDisplayName());
                    refItinerary.push().setValue(itineraryModel);
                    Toast.makeText(SubmitItineraryActivity.this, getString(R.string.toast_new_trajet_done), Toast.LENGTH_SHORT).show();

                    finish();
                }
                else {
                    Toast.makeText(SubmitItineraryActivity.this, getText(R.string.toastResearch), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateLabel(){
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);

        editTextDate.setText(sdf.format(mMyCalendar.getTime()));
    }
}
