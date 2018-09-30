package com.example.android.flexitask;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.example.android.flexitask.data.taskContract;
import com.example.android.flexitask.data.taskDBHelper;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by Ryan Mcgoff (4086944), Jerry Kumar (3821971), Jaydin Mcmullan (9702973)
 * This class is initialised by the Android system when an alarm is fired, the class
 * uses {@link NotificationHelper} to create a notification to broadcast to the user's device.
 * It is used to create a daily summary of due tasks notifications
 */
public class AlertReceiver extends BroadcastReceiver {

    private taskDBHelper mDbHelper;

    /**
     * This method is called when the class receives a message from the Android system (when a timed
     * alarm is fired). The method finds tasks that will be due over the next 7 days and uses the notification
     * helper class to construct and then display that notification.
     *
     * @param context app context
     * @param intent  intent passed to pending intent when creating alarm
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        /*checks shared preferences to check if the user has turned off the daily notification
        in the settings */
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean dailySwitch = preferences.getBoolean("daily", false);

        if (dailySwitch) {

            mDbHelper = new taskDBHelper(context);
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            //Array list to store task titles;
            ArrayList<String> notificationMessage = new ArrayList<String>();

            long today = Calendar.getInstance().getTimeInMillis();
            long m7DaysAhead = today + 604800000;

            //Cursors through the database to retrieve a list tasks due/overdue in the next 7 days
            Cursor cursorc = db.rawQuery("SELECT * FROM " + taskContract.TaskEntry.TABLE_NAME +
                    " WHERE " + String.valueOf(m7DaysAhead) + " > " + taskContract.TaskEntry.COLUMN_DATE + " OR " +
                    String.valueOf(today) + " > " + taskContract.TaskEntry.COLUMN_DATE, null);
            while (cursorc.moveToNext()) {
                int titleColumnIndex = cursorc.getColumnIndex(taskContract.TaskEntry.COLUMN_TASK_TITLE);
                String taskTitle = cursorc.getString(titleColumnIndex);
                notificationMessage.add(taskTitle);

            }
            //Calls notification helper to build notification, and then broadcasts it
            NotificationHelper mNotificationHelper = new NotificationHelper(context);
            NotificationCompat.Builder nb = mNotificationHelper.getChannel("Daily Summary", notificationMessage);
            mNotificationHelper.getNotificationManager().notify(1, nb.build());
        }


        //sets an alarm for the following day that will call this class and broadcast the daily notification again
        Calendar c = Calendar.getInstance();
        String alarmS = PreferenceManager.getDefaultSharedPreferences(context).getString("time", "08:00");

        c.add(Calendar.DAY_OF_YEAR, 1); //add day

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }
}
