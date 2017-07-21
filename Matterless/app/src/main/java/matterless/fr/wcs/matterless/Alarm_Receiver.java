package matterless.fr.wcs.matterless;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.LocationAvailability;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Alarm_Receiver extends BroadcastReceiver {


    public static final String TAG = "Alarm_Receiver";
    public static final String INTENT_CANCEL_NOTIF = "INTENT_CANCEL_NOTIF";

    public static final int IDNotification = 1;
    private static int mNbMessage = 0;

    private UserCredentials muserCredentials;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(final Context context, Intent intent) {

        if(intent.getAction() == null) {

            String messageContent = intent.getStringExtra(MyService.MESSAGE_CONTENT);
            String messageName = intent.getStringExtra(MyService.MESSAGE_NAME);
            String channelId = intent.getStringExtra(MyService.CHANNEL_ID);

            Log.e(TAG, "reached the alarm receiver");

            muserCredentials = new UserCredentials();
            muserCredentials = UserCredentials.fromFile(context, MainActivity.FILE_NAME);


            sendNotification(context, intent);

            Post post = new Post();
            post.setMessage(messageContent);
            post.setChannelId(channelId);
            MattermostService sendPost = ServiceGenerator.RETROFIT.create(MattermostService.class);
            Call<Post> callPost = sendPost.sendPost(("Bearer " + muserCredentials.getToken()), post);
            callPost.enqueue(new Callback<Post>() {
                @Override
                public void onResponse(Call<Post> call, Response<Post> response) {
                    if (response.isSuccessful()) {
                        Log.e(TAG, "Post sended" + response.toString());

                    } else {
                        Log.e(TAG, String.valueOf("response was not sucessfullll" + response.toString() + response.headers()));
                    }
                }

                @Override
                public void onFailure(Call<Post> call, Throwable t) {

                }
            });
        }

        else if (intent.getAction().equals(intent.ACTION_BOOT_COMPLETED)){
            Intent toService = new Intent(context, MyService.class);
            toService.setAction(MyService.INTENT_START_BOT);
            context.startService(toService);
        }
        else if(intent.getAction().equals(MyService.LOCATION)){

            GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

            String messageContent = intent.getStringExtra(MyService.MESSAGE_CONTENT);
            String messageName = intent.getStringExtra(MyService.MESSAGE_NAME);
            String channelId = intent.getStringExtra(MyService.CHANNEL_ID);

            Log.e(TAG, "reached the alarm receiver");

            muserCredentials = new UserCredentials();
            muserCredentials = UserCredentials.fromFile(context, MainActivity.FILE_NAME);


            sendNotification(context, intent);

            Post post = new Post();
            post.setMessage(messageContent);
            post.setChannelId(channelId);
            MattermostService sendPost = ServiceGenerator.RETROFIT.create(MattermostService.class);
            Call<Post> callPost = sendPost.sendPost(("Bearer " + muserCredentials.getToken()), post);
            callPost.enqueue(new Callback<Post>() {
                @Override
                public void onResponse(Call<Post> call, Response<Post> response) {
                    if (response.isSuccessful()) {
                        Log.e(TAG, "Post sended" + response.toString());

                    } else {
                        Log.e(TAG, String.valueOf("response was not sucessfullll" + response.toString() + response.headers()));
                    }
                }

                @Override
                public void onFailure(Call<Post> call, Throwable t) {

                }
            });


        } else if(intent.getAction().equals(INTENT_CANCEL_NOTIF)){
            mNbMessage =0;
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void sendNotification(final Context context, Intent intent){

        String messageContent = intent.getStringExtra(MyService.MESSAGE_CONTENT);
        String messageName = intent.getStringExtra(MyService.MESSAGE_NAME);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder noti = new NotificationCompat.Builder(context);

        if (mNbMessage == 0) {
            mNbMessage += 1;
            noti.setContentTitle(messageName + context.getString(R.string.sended))
                    .setContentText(messageContent)
                    .setSmallIcon(R.mipmap.icon);
        }
        else{
            mNbMessage += 1;
            noti.setContentTitle(messageName + " "
                    + context.getString(R.string.and) + " "
                    + mNbMessage + " "
                    + context.getString(R.string.and_other) + " "
                    + context.getString(R.string.sended_plural))
                    .setContentText(messageContent + ", ...")
                    .setSmallIcon(R.mipmap.icon);
        }

        Intent intentCancelNotif = new Intent(context, Alarm_Receiver.class);
        intentCancelNotif.setAction(INTENT_CANCEL_NOTIF);

        noti.setDeleteIntent(PendingIntent.getBroadcast(context,
                0,
                intentCancelNotif,
                PendingIntent.FLAG_ONE_SHOT));

        notificationManager.notify(IDNotification, noti.build());
    }

}
