package matterless.fr.wcs.matterless;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MessageSettingActivity extends AppCompatActivity /*implements View.OnClickListener*/ {



    public static final String BOT_SIGNATURE = " #Matterless";


    private Intent intent;
    private String ref;

    private Button buttonTimePicker;
    private Button buttonSelectDay;
    private Button buttonCreateEvent;
    private Button buttonChoseChannel;

    private EditText mEditTextMessageName;
    private EditText mEditTextMessageContent;

    private Message mFutureMessage;
    private ArrayList<String> finalDays;

    private ChannelRequest mChannelRequest;
    private FirebaseDatabase database;
    private DatabaseReference mRef;

    private int hour;
    private int minute;

    private UserCredentials muserCredentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_setting);

        database = FirebaseDatabase.getInstance();

        intent = getIntent();
        ref = intent.getStringExtra("ref");

        buttonTimePicker = (Button) findViewById(R.id.buttonTimePicker);
        buttonSelectDay = (Button) findViewById(R.id.buttonSelectDay);
        buttonCreateEvent = (Button) findViewById(R.id.buttonCreateEvent);
        buttonChoseChannel = (Button) findViewById(R.id.buttonChoseChannel);

        mEditTextMessageName = (EditText) findViewById(R.id.editTextMessageName);
        mEditTextMessageContent = (EditText) findViewById(R.id.editTextMessageContent);

        finalDays = new ArrayList<>();

        /*
        * Je regarde si l'intent qui démarre l'activity contient une instance de Message pour savoir
        * si l'user est en train d'éditer un message existant ou si il en crée un.
        *
        * S'il y a un message dans l'intent, j'en affiche les caractéristiques dans les Views associées
        * */

        if (intent.hasExtra("message")) {


            mFutureMessage = intent.getParcelableExtra("message");

            buttonTimePicker.setText(mFutureMessage.getmTimeHour() + ":" + mFutureMessage.getmTimeMinute());
            buttonSelectDay.setText(mFutureMessage.getDaysEnabled());
            mEditTextMessageName.setText(mFutureMessage.getmName());
            mEditTextMessageContent.setText(mFutureMessage.getmMessageContent());
            buttonCreateEvent.setText(R.string.ValidateButton);

        }

        else {
            mFutureMessage = new Message(getResources().getStringArray(R.array.daysOfWeekArray));
            Calendar calendar = Calendar.getInstance();
            mFutureMessage.setmTimeHour(calendar.get(Calendar.HOUR_OF_DAY));
            mFutureMessage.setmTimeMinute(calendar.get(Calendar.MINUTE));
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

                    final AlertDialog.Builder channelDialog = new AlertDialog.Builder(MessageSettingActivity.this);
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
        * Liste de  7 Booleans, tous false, qui sera passé en paramètre à mon AlertDialogBuilder pour
        * la sélection des jours de la semaine.
        *
        * Ils permettent d'afficher des cases cochées ou non dans l'AlertDialog à choix multiples.
        *
        * Je vérifie une fois de plus si l'intent contenait une instance de Message, et modifie le
        * tableau de booleans en conséquence pour cocher certaines cases en fonction des jours
        * programmés si le message doit être édité et non créé
        * */
        final boolean[] _selections = {false, false, false, false, false, false, false};

        if (intent.hasExtra("message") && mFutureMessage.getmDays() != null) {

            for (int i = 0; i < mFutureMessage.getmDays().size(); i++) {

                switch (mFutureMessage.getmDays().get(i).isEnabled() ? 1 : 0) {

                    case 1:
                        _selections[i] = true;
                        mFutureMessage.getmDays().get(i).setEnabled(true);
                        break;
                }
            }
        }

        /*
        * AlertDialogBuilder qui affiche la liste de jours définie en ressources, et qui permet de
        * sélectionner un ou plusieurs jours pour l'envoi du message.
        *
        * Le onCheckedChangeListenerajoute ou enlève des jours à la liste de Days de l'objet message
        * en fonction des actionsde l'user et le bouton valider affiche les jours dans le boutton.
        * */
        buttonSelectDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder dayDialog = new AlertDialog.Builder(MessageSettingActivity.this);
                final String[] day = getResources().getStringArray(R.array.daysOfWeekArray);

                dayDialog.setTitle("Choisis tes jours");

                dayDialog.setMultiChoiceItems(day, _selections, new DialogInterface.OnMultiChoiceClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                        if (isChecked) {
                            mFutureMessage.getmDays().get(which).setEnabled(true);
                        } else if (!isChecked) {
                            mFutureMessage.getmDays().get(which).setEnabled(false);
                        }

                    }
                });


                dayDialog.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        buttonSelectDay.setText(mFutureMessage.getDaysEnabled());
                        dialog.dismiss();
                    }
                });

                dayDialog.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                }).show();
            }
        });

        /* timePickerDialog qui s'ouvre par défaut à l'heure actuelle et permet à l'user de choisir
        * l'heure d'envoi du message.
        *
        * On vérifie une fois de plus si l'intent contient un Message
        * et on affiche l'heure de ce dernier plutot que celle du systeme si c'est le cas
        *
        * Quand l'user termine, les timeHour et timeMinute du message sont
        * mis à jours et le text du bouton affiche l'heure choisie.
        * */
        buttonTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();

                if (intent.hasExtra("message")) {
                    hour = mFutureMessage.getmTimeHour();
                    minute = mFutureMessage.getmTimeMinute();
                } else {
                    hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    minute = mcurrentTime.get(Calendar.MINUTE);
                }
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MessageSettingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mFutureMessage.setmTimeHour(selectedHour);
                        mFutureMessage.setmTimeMinute(selectedMinute);

                        //ce if/else gère le cas où l'user choisis une minute inférieure à 10
                        //pour ajouter un 0 avant dans le text du bouton
                        if (mFutureMessage.getmTimeMinute() < 10) {
                            buttonTimePicker.setText(mFutureMessage.getmTimeHour() + ":0" + mFutureMessage.getmTimeMinute());
                        } else {
                            buttonTimePicker.setText(mFutureMessage.getmTimeHour() + ":" + mFutureMessage.getmTimeMinute());
                        }
                    }
                }, hour, minute, true);
                mTimePicker.show();
            }
        });


        /* En cas de clic sur le bouton valider, on crée une String qui contient le contenu de
        * l'editTextMessageContent et on vérifie si l'user a choisi d'ajouter la signatue ou non.
        *
        * On ajoute à cette string la signature en fonction du choix de l'user.
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

                if (mFutureMessage.getDaysEnabled().length() == 0 || mEditTextMessageName.getText().toString().length() == 0 || mEditTextMessageContent.getText().toString().length() == 0 ||mFutureMessage.getmChannelName() == null) {

                    Toast.makeText(MessageSettingActivity.this, R.string.toastComplete, Toast.LENGTH_SHORT).show();
                }

                else if(!intent.hasExtra("message")){
                    mFutureMessage.setmName(mEditTextMessageName.getText().toString().trim());
                    mFutureMessage.setmMessageContent(finalContent.trim());

                    mRef = database.getReference("Messages/" + muserCredentials.getUserID());
                    mRef.push().setValue(mFutureMessage);
                    finish();
                }
                else {
                    mFutureMessage.setmName(mEditTextMessageName.getText().toString().trim());
                    mFutureMessage.setmMessageContent(finalContent.trim());

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
        muserCredentials = UserCredentials.fromFile(MessageSettingActivity.this, MainActivity.FILE_NAME);


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
    }

    @Override
    protected void onStop() {
        super.onStop();

        muserCredentials = null;
    }
}