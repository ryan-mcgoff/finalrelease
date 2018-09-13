package com.example.android.flexitask;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.preference.PreferenceManager;

import java.util.ArrayList;

/**
 * Created by rymcg on 9/09/2018.
 */



public class NotificationHelperReminders extends ContextWrapper {
    public static final String CHANNELID = "channel2ID";
    public static final String CHANNELNAME = "Reminders";
    private SharedPreferences sharedPreferences;

    private NotificationManager mNotificationManager;



    public NotificationHelperReminders(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
            createChannels();
        }
    }
    @TargetApi(Build.VERSION_CODES.O)
    public void createChannels(){
        //Creates channel

        NotificationChannel notificationChannel = new NotificationChannel(CHANNELID,CHANNELNAME,
                NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setLightColor(R.color.colorPrimary);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getNotificationManager().createNotificationChannel(notificationChannel);
    }

    public NotificationManager getNotificationManager(){

        if (mNotificationManager==null){
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        }
        return mNotificationManager;
    }


    /**
     * Builds a notification for alter receiver to use
     * @param title of the notification
     * @param message for the getChannel() method to parse
     * @return notification Builder object for alert receiver to broadcast
     */
    public NotificationCompat.Builder getChannel(String title, String message){

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(),CHANNELID)
                .setContentTitle(title)
                .setContentText("")
                .setSmallIcon(R.drawable.task_selector);

        return mBuilder;

    }

}
