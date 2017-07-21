package matterless.fr.wcs.matterless;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by wilder on 19/04/17.
 */

public class MessageSettingGeolocActivity extends AppCompatActivity {

    public final String FILE_NAME = "FILE_NAME";
    public static final String BOT_SIGNATURE = "#Matterless ";   // signature du bot

    private Intent intent;
    private String ref;

    private Button buttonGeoloc;
    private Button buttonCreateEvent;
    private Button buttonChoseChannel;

    private EditText mEditTextMessageName;
    private EditText mEditTextMessageContent;

    private Message mFutureMessage;

    private ChannelRequest mChannelRequest;
    private FirebaseDatabase database;
    private DatabaseReference mRef;

    private LatLng mLatLng;

    private FileInputStream mfileInputStream;
    private UserCredentials muserCredentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_setting_geoloc);

        database = FirebaseDatabase.getInstance();

        intent = getIntent();
        ref = intent.getStringExtra("ref");

        buttonGeoloc = (Button) findViewById(R.id.buttonGeoloc);
        buttonCreateEvent = (Button) findViewById(R.id.buttonCreateEvent);
        buttonChoseChannel = (Button) findViewById(R.id.buttonChoseChannel);

        mEditTextMessageName = (EditText) findViewById(R.id.editTextMessageName);
        mEditTextMessageContent = (EditText) findViewById(R.id.editTextMessageContent);

        /*
        * Je regarde si l'intent qui démarre l'activity contient une instance de Message pour savoir
        * si l'user est en train d'éditer un message existant ou si il en crée un.
        *
        * S'il y a un message dans l'intent, j'en affiche les caractéristiques dans les Views associées
        * */

        if (intent.hasExtra("message")) {


            mFutureMessage = intent.getParcelableExtra("message");
            mLatLng = new LatLng(intent.getDoubleExtra("LAT", 0), intent.getDoubleExtra("LNG", 0));

            mEditTextMessageName.setText(mFutureMessage.getmName());
            mEditTextMessageContent.setText(mFutureMessage.getmMessageContent());
            buttonCreateEvent.setText(R.string.ValidateButton);

        }

        else {
            mFutureMessage = new Message();
        }

        /*
        * AlertDialog qui affiche les channels de discussion de l'utilisateur actuellement connecté
        * et propose de choisir.
        *
        * On vérifie également si l'intent qui commence l'activity contient
        * déjà une instance de Message.
        *
        * Si oui on coche en avance le channel sur lequel ce message
        * devait s'envoyer
        * */
        buttonChoseChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mChannelRequest != null) {

                    final AlertDialog.Builder channelDialog = new AlertDialog.Builder(MessageSettingGeolocActivity.this);
                    final String[] channelList = mChannelRequest.getChannelNames(mChannelRequest.getPublicChannel());
                    int checkboxId = 1;
                    if (intent.hasExtra("message")) {

                        for (int i = 0; i < channelList.length; i++) {

                            if (mFutureMessage.getmChannelName().equals(channelList[i])) {
                                checkboxId = i;
                            }
                        }
                    }


                    channelDialog.setTitle(R.string.choseChannelButtonText);
                    channelDialog.setSingleChoiceItems(channelList, checkboxId, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            mFutureMessage.setmChannelName(mChannelRequest.getPublicChannel().get(which).getDisplayName());
                            mFutureMessage.setmChannelId(mChannelRequest.getPublicChannel().get(which).getId());
                            buttonChoseChannel.setText(mFutureMessage.getmChannelName());
                            dialog.dismiss();
                        }
                    }).show();
                }
            }
        });


        /*
        * On vérifie une fois de plus si l'intent contient un Message et on affiche la géoloc
        *
        * Quand l'user termine, est mis à jours.
        * */
        buttonGeoloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToMapsActivity = new Intent(MessageSettingGeolocActivity.this, MapsActivity.class);
                startActivity(intentToMapsActivity);

                if (intent.hasExtra("message")) {

                } else {

                }
            }
        });


        /* En cas de clic sur le bouton valider, on crée une String qui contient le contenu de
        * l'editTextMessageContent et la signatue.
        *
        * On vérifie ensuite si l'utilisateur a bien rempli tout les champs. On affiche un toast le
        * cas échéant.
        *
        * Si tout les champs sont remplis on vérifie si l'user était en mode création ou édition.
        *
        *   En édition on crée une nouvelle instance de message et on l'upload sur firebase à la place de
        *   l'objet précédent.
        *
        *   En création on crée une nouvelle instance de message et on l'upload sur firebase sur un
        *   nouvel emplacement.
        *
        * On tue ensuite l'activity.
        * */
        buttonCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String finalContent = mEditTextMessageContent.getText().toString();
                if (!finalContent.contains(BOT_SIGNATURE)){
                    finalContent = finalContent + BOT_SIGNATURE;
                }

                if (mLatLng.latitude == 0.0d ||  mFutureMessage.getmChannelId() == null|| mEditTextMessageName.getText().toString() == null || mEditTextMessageContent.getText().toString() == null || mFutureMessage.getmChannelName() == null) {

                    Toast.makeText(MessageSettingGeolocActivity.this, R.string.toastComplete, Toast.LENGTH_SHORT).show();
                }

                else if(!intent.hasExtra("message")){
                    mFutureMessage.setmName(mEditTextMessageName.getText().toString().trim());
                    mFutureMessage.setmMessageContent(finalContent.trim());
                    mFutureMessage.setLat(mLatLng.latitude);
                    mFutureMessage.setLng(mLatLng.longitude);
                    mFutureMessage.setEventID(new Random().nextInt());

                    mRef = database.getReference("Messages/" + muserCredentials.getUserID());
                    mRef.push().setValue(mFutureMessage);
                    finish();
                }
                else {
                    mFutureMessage.setmName(mEditTextMessageName.getText().toString().trim());
                    mFutureMessage.setmMessageContent(finalContent.trim());
                    mFutureMessage.setLat(mLatLng.latitude);
                    mFutureMessage.setLng(mLatLng.longitude);
                    mFutureMessage.setEventID(new Random().nextInt());

                    mRef = database.getReference("Messages/" + muserCredentials.getUserID());
                    mRef.child(ref).setValue(mFutureMessage);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        muserCredentials = new UserCredentials();
        muserCredentials = UserCredentials.fromFile(this, FILE_NAME);

        mRef = database.getReference("Messages/" + muserCredentials.getUserID());

        MattermostService mattermostService =
                ServiceGenerator.RETROFIT.create(MattermostService.class);
        Call<ChannelRequest> call = mattermostService.getChannels("Bearer " + muserCredentials.getToken());
        call.enqueue(new Callback<ChannelRequest>() {
            @Override
            public void onResponse(Call<ChannelRequest> call, Response<ChannelRequest> response) {
                mChannelRequest = response.body();
            }

            @Override
            public void onFailure(Call<ChannelRequest> call, Throwable t) {
                Log.e(MainActivity.TAG, t.toString());
            }
        });

        SharedPreferences settings = getSharedPreferences(MapsActivity.LATLNG, 0);
        if (settings.getFloat(MapsActivity.LAT, (float) 0) != 0) {
            mLatLng = new LatLng((double) settings.getFloat(MapsActivity.LAT, (float) 0), (double) settings.getFloat(MapsActivity.LNG, (float) -0));
            SharedPreferences.Editor editor = settings.edit();
            editor.clear();
            editor.apply();
        }


    }

    @Override
    protected void onStop() {
        super.onStop();

        muserCredentials = null;
    }
}
