package matterless.fr.wcs.matterless;

import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class MessageListActivity extends AppCompatActivity {


    private UserCredentials muserCredentials;

    private ListView mListViewMessage;

    private CustomListAdapter mAdapter;

    private FirebaseDatabase mDatabase;
    Query mRef;


    private Button buttonAddEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        mDatabase = FirebaseDatabase.getInstance();

        mListViewMessage = (ListView) findViewById(R.id.listViewMessage);
        buttonAddEvent = (Button) findViewById(R.id.buttonAddEvent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        muserCredentials = new UserCredentials();
        muserCredentials = UserCredentials.fromFile(MessageListActivity.this, MainActivity.FILE_NAME);

        mRef = mDatabase.getReference("Messages/"+muserCredentials.getUserID());

        mAdapter = new CustomListAdapter(mRef, MessageListActivity.this, R.layout.message_list_item);

        mListViewMessage.setAdapter(mAdapter);

        /*On item click listener sur la liste pour lance le profil du message,
        *
        * Je passe en intent l'instance de Message concernée pour afficher ses caractéristiques,
        * et sa position dans la liste pour gérer sa suppression par l'utilisateur si besoin
        * */

        mListViewMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MessageListActivity.this, MessageDetailsActivity.class);
                Message message = (Message) mAdapter.getItem(position);


                String ref = mAdapter.getmKey(position);  //ID de l'item dans Firebase

                intent.putExtra("message", message); //instance de message
                intent.putExtra("ref", ref);
                intent.putExtra("LAT", message.getLat());
                intent.putExtra("LNG", message.getLng());

                startActivity(intent);
            }
        });

        /* onClickListener sur le bouton addEvent.
        *
        * On lance la messageSettingActivity pour ajouter un événement à la liste
        * */
        buttonAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder messageTypeDialog = new AlertDialog.Builder(MessageListActivity.this);

                messageTypeDialog.setTitle(R.string.alertDialogMessageTypeTitle);
                messageTypeDialog.setMessage(R.string.alertDialogMessageTypeContent);
                messageTypeDialog.setPositiveButton("Ponctuel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intentToTimeMessage = new Intent(MessageListActivity.this, MessageSettingActivity.class);
                        startActivity(intentToTimeMessage);
                        dialog.dismiss();
                    }
                });
                messageTypeDialog.setNegativeButton("Géolocalisé", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intentToMessageSettingGeoLoc = new Intent(MessageListActivity.this, MessageSettingGeolocActivity.class);
                        startActivity(intentToMessageSettingGeoLoc);
                        dialog.dismiss();
                    }
                });
                messageTypeDialog.show();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        muserCredentials = null;
    }
}
