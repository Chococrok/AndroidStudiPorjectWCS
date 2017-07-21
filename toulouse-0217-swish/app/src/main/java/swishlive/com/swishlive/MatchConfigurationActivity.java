package swishlive.com.swishlive;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.android.grafika.CameraCaptureActivity;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;

import org.json.JSONException;
import org.parceler.Parcels;

import java.util.regex.Pattern;

public class MatchConfigurationActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = "MatchConfigActivity";
    public static final String STREAMING_KEY = "url_key";
    public static final String GAME_MODEL = "GameModel";
    public static final String SPORT_KEY = "SportKey";
    public static final String URL_PATH = "/me/live_videos?description=";

    private static final int REQUEST_STREAM = 2;
    private static String[] PERMISSIONS_STREAM = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAPTURE_VIDEO_OUTPUT,
            Manifest.permission.CAPTURE_AUDIO_OUTPUT,
    };


    private GridView gridViewSportChoices;
    private EditText editTextTeamOne;
    private EditText editTextTeamTwo;
    private Button buttonConfigurationDone;
    private String streamURL;
    private Profile userProfile;

    private GridViewAdapter gridViewAdapter;
    private View previousSelection;

    private SportModel sportModel;
    private int sportKey;

    private TeamModel teamOne;
    private TeamModel teamTwo;

    private String streamingKey, teamOneName,teamTwoName,title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_configuration);
        if(Profile.getCurrentProfile()!=null){
            userProfile = Profile.getCurrentProfile();
        }else{
            Toast.makeText(MatchConfigurationActivity.this, R.string.notConnected,Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MatchConfigurationActivity.this, MainActivity.class));
            finish();
        }

        gridViewSportChoices = (GridView) findViewById(R.id.gridViewSportChoices);
        sportKey = -1;

        gridViewAdapter = new GridViewAdapter(this);
        gridViewSportChoices.setAdapter(gridViewAdapter);
        gridViewSportChoices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                /* Following if is used to prevent tennis matches to be created
                   Just remove it when Tennis fragment is ready to use
                 */

                if (i == 2) {

                    AlertDialog.Builder adb = new AlertDialog.Builder(MatchConfigurationActivity.this)
                            .setMessage(R.string.tennisNotSupported)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    adb.show();
                    return;

                }
                if (previousSelection == null) {
                    previousSelection = view;
                } else {
                    previousSelection.setBackground(getDrawable(R.drawable.grid_item_normal));
                    previousSelection = view;
                }
                view.setBackground(getDrawable(R.drawable.grid_item_selected));
                switch (i){

                    case 0 :
                       title = "VolleyBall";
                        break;

                    case 1 :
                        title = "BasketBall";
                        break;

                    case 2 :
                        title = "Tennis";
                        break;

                    case 3 :
                        title = "HandBall";
                        break;

                    case 4 :
                        title = "FootBall";
                        break;

                    case 5 :
                        title = "Rugby";
                        break;
                }

                sportKey = i;

            }
        });

        editTextTeamOne = (EditText) findViewById(R.id.editTextTeamOne);
        editTextTeamOne.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    teamOneName = editTextTeamOne.getText().toString().trim();
                    validationTeamName(editTextTeamOne, teamOneName);
                }
            }
        });

        editTextTeamTwo = (EditText) findViewById(R.id.editTextTeamTwo);
        editTextTeamTwo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    teamTwoName = editTextTeamTwo.getText().toString().trim();
                    validationTeamName(editTextTeamTwo, teamTwoName);
                }
            }
        });


        buttonConfigurationDone = (Button) findViewById(R.id.buttonConfigurationDone);
        buttonConfigurationDone.setOnClickListener(this);


        verifyPermissions();
    }

    private boolean validationTeamName(EditText EditTextTeamName, String teamName) {

        boolean hasError = false;

        if (teamName.length() == 0) {

            EditTextTeamName.setError(getString(R.string.needTeamName));
            hasError = true;
        }

        return hasError;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.buttonConfigurationDone :

                teamOneName = editTextTeamOne.getText().toString().trim();
                teamTwoName = editTextTeamTwo.getText().toString().trim();
                if (!Pattern.matches("[\\w\\s]+", teamOneName+teamTwoName)){
                    Toast.makeText(this, R.string.teamNamingRestriction, Toast.LENGTH_SHORT).show();
                    return;
                }



                boolean teamOneError = validationTeamName(editTextTeamOne, teamOneName);
                boolean teamTwoError = validationTeamName(editTextTeamTwo, teamTwoName);

                if (!teamOneError && !teamTwoError && sportKey != -1){

                    teamOne = new TeamModel(teamOneName);
                    teamTwo = new TeamModel(teamTwoName);

                    sportModel = new SportModel();
                    sportModel.setTeamOne(teamOne);
                    sportModel.setTeamTwo(teamTwo);

                    GameModel gameModel = new GameModel(teamOne, teamTwo, sportModel);

                    this.performGraphRequest(gameModel);

                } else {

                    AlertDialog.Builder adb = new AlertDialog.Builder(this)
                            .setMessage(R.string.needInputFilling)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });

                    adb.show();
                }
                break;
        }

    }

    public void verifyPermissions() {
        for (String permission : PERMISSIONS_STREAM) {
            int permissionResult = ActivityCompat.checkSelfPermission(MatchConfigurationActivity.this, permission);
            if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        MatchConfigurationActivity.this,
                        PERMISSIONS_STREAM,
                        REQUEST_STREAM
                );
                return;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STREAM) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
        }
    }

    private void performGraphRequest(GameModel gameModel){

        final GameModel sendedGameModel = gameModel;

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.open_stream));
        progressDialog.show();

        StringBuilder description = new StringBuilder();
        description.append(title);
        description.append(":+");
        description.append(teamOneName);
        description.append("+Vs+");
        description.append(teamTwoName);
        String field = description.toString().replace(' ', '+');

        GraphRequest request = new GraphRequest(
                AccessToken.getCurrentAccessToken(), URL_PATH + field, null,
                HttpMethod.POST,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            /*if (response.getError().) {
                                Log.d(TAG, response.getError().getErrorMessage());
                                Toast.makeText(MatchConfigurationActivity.this, R.string.errorToast, Toast.LENGTH_SHORT).show();
                                return;
                            }*/
                            streamURL=response.getJSONObject().get("stream_url").toString();
                            streamingKey = streamURL.substring(37);
                            Log.e(TAG,streamingKey);

                            Intent startStreamingIntent = new Intent(MatchConfigurationActivity.this, CameraCaptureActivity.class);
                            startStreamingIntent.putExtra(GAME_MODEL, Parcels.wrap(sendedGameModel));
                            startStreamingIntent.putExtra(SPORT_KEY, sportKey);
                            startStreamingIntent.putExtra(STREAMING_KEY, streamingKey);
                            startActivity(startStreamingIntent);
                            Log.d(TAG, sendedGameModel.getSportModel().getName() + " Match created with " + teamOneName + " and " + teamTwoName);
                            progressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        request.executeAsync();
    }
}
