package patrimoine.wcs.fr.toulousemonuments;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.octo.android.robospice.GsonGoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import patrimoine.wcs.fr.toulousemonuments.models.MonumentModel;

public class DescriptionActivity extends AppCompatActivity {

    private TextView mTextViewTitle;
    private ImageView mImageViewDescriptionMain;
    private TextView mTextViewDescription;
    private TextView mTextviewScore;
    private ImageButton mImageButtonRating;

    private SpiceManager mSpiceManager;
    private int mPosition;
    private MonumentModel mMonument;
    private boolean isNavigationDescriptionSelected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        Intent intentFromMainActivity = getIntent();
        mPosition = intentFromMainActivity.getIntExtra(MainActivity.POSITION, -1);

        mTextViewTitle = (TextView) findViewById(R.id.textViewTitle);
        mTextViewDescription = (TextView) findViewById(R.id.textViewDescription);
        mTextviewScore = (TextView) findViewById(R.id.textViewScore);
        mImageViewDescriptionMain = (ImageView) findViewById(R.id.imageViewDescriptionMain);
        mImageButtonRating = (ImageButton) findViewById(R.id.imageButtonRating);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_description:
                        isNavigationDescriptionSelected = true;
                        DescriptionFragment desciptionFragment = new DescriptionFragment(mPosition, mMonument);
                        loadFragment(desciptionFragment);
                        return true;
                    case R.id.navigation_picture:
                        isNavigationDescriptionSelected = false;
                        loadFragment(AddPhotos.newInstance());
                        return true;
                    case R.id.navigation_comment:
                        isNavigationDescriptionSelected = false;
                        CommentFragment commentFragment = new CommentFragment(mPosition, mMonument);
                        loadFragment(commentFragment);
                        return true;
                }
                return false;
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
        mSpiceManager.execute(monumentRequest, MainActivity.CACHE, DurationInMillis.ONE_WEEK, new DescriptionActivity.MonumentRequestListener());
    }

    private void loadFragment(BaseFragment f) {
        getFragmentManager().beginTransaction().replace(R.id.framLayoutDescription, f).commit();
    }

    private class MonumentRequestListener implements RequestListener<MonumentModel> {


        @Override
        public void onRequestFailure(SpiceException spiceException) {

        }

        @Override
        public void onRequestSuccess(MonumentModel monument) {
            mMonument = monument;
            if(isNavigationDescriptionSelected) {

                DescriptionFragment desciptionFragment = new DescriptionFragment(mPosition, mMonument);
                loadFragment(desciptionFragment);
            }

        }
    }
}



