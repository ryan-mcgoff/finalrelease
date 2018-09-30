package com.example.android.flexitask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v7.preference.PreferenceManager;

/**
 * Created by Ryan Mcgoff (4086944), Jerry Kumar (3821971), Jaydin Mcmullan (9702973)
 * This class is initialised by the Android system when an alarm is fired, the class
 * uses {@link NotificationHelper} to create a notification reminder to broadcast to the user's device.
 * It is used for fixedtask notifications.
 */
public class AlertReceiverReminder extends BroadcastReceiver {

    /**
     * This method is called when the class receives a message from the Android system (when a timed
     * alarm is fired). The method creates a notification displaying the task title for the set
     * notification reminder.
     *
     * @param context app context
     * @param intent intent passed to pending intent when creating alarm holding the task's name
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        /*to ensure each notification appears seperate/doesn't overwrite other notifications,
        a iterative notification ID is used (up to 100 notifications)*/
        int notificationID = Integer.valueOf(preferences.getString("notificationID","2"));
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        if(notificationID>100){
            notificationID = 2;
        }else{
            editor.putString("notificationID",String.valueOf(notificationID+1));
            editor.apply();
        }

        //creates notification
        NotificationHelperReminders mNotificationHelper = new NotificationHelperReminders(context);
        NotificationCompat.Builder nb = mNotificationHelper.getChannel("Task Reminder","Task info");
        mNotificationHelper.getNotificationManager().notify(notificationID,nb.build());

    }
}
