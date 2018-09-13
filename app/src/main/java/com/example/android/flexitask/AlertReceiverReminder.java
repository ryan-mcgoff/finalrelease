package com.example.android.flexitask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v7.preference.PreferenceManager;

/**
 * Created by rymcg on 9/09/2018.
 */

public class AlertReceiverReminder extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int notificationID = Integer.valueOf(preferences.getString("notificationID","2"));
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("notificationID",String.valueOf(notificationID+1));
        editor.apply();

        NotificationHelperReminders mNotificationHelper = new NotificationHelperReminders(context);
        NotificationCompat.Builder nb = mNotificationHelper.getChannel("Task Reminder","Task info");
        mNotificationHelper.getNotificationManager().notify(notificationID,nb.build());

    }
}
