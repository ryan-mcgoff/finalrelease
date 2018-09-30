package com.example.android.flexitask;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.preference.PreferenceManager;

import com.example.android.flexitask.data.taskContract;
import com.example.android.flexitask.data.taskDBHelper;

import java.util.Calendar;

/**
 * Created by Ryan Mcgoff (4086944), Jerry Kumar (3821971), Jaydin Mcmullan (9702973)
 * This class is called by the Android system when the user's device is turned on.
 * The class sets up the daily notification again and each individual task reminder
 * that was set by the user. This is necessary because the Android system deletes all pending
 * intents/alarms when the user's device is switched off, so they need to be set up again.
 */
public class DeviceBootReceiver extends BroadcastReceiver {

    /**
     * {@inheritDoc}
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        //checks that the class was in-fact called by the Android system booting up again/turning on
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            //reset all alarms and notifications
            setDailyNotification(context);
            setFixedNotifications(context);
        }
    }

    /**
     * Sets up daily notification again
     *
     * @param context the application's context
     */
    public void setDailyNotification(Context context) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        //get notification time preference
        String alarmS = PreferenceManager.getDefaultSharedPreferences(context).getString("time", "08:00");

        String[] timeArray = alarmS.split(":");
        int hour = (Integer.parseInt(timeArray[0]));
        int min = (Integer.parseInt(timeArray[1]));

        Calendar todayC = Calendar.getInstance();

        Calendar timePickerC = (Calendar) todayC.clone();
        timePickerC.set(Calendar.HOUR_OF_DAY, hour);
        timePickerC.set(Calendar.MINUTE, min);

        //if the time is after or equal to the notification hour / min schedule alarm for the next day
        if (todayC.after(timePickerC)) {
            timePickerC.add(Calendar.DAY_OF_YEAR, 1); //add day
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timePickerC.getTimeInMillis(), pendingIntent);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("label", "all");
        editor.apply();
    }


    /**
     * Sets up each fixed task notification that was set by the user.
     *
     * @param context the application's context
     */
    public void setFixedNotifications(Context context) {

        taskDBHelper mDbHelper = new taskDBHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Calendar todayDate = Calendar.getInstance();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

        //Creates a query request for Fixed tasks with set reminder fields
        Cursor cursorc = db.rawQuery("SELECT * FROM " + taskContract.TaskEntry.TABLE_NAME + " WHERE " +
                taskContract.TaskEntry.COLUMN_TYPE_TASK + " = " + taskContract.TaskEntry.TYPE_FIXED +
                " AND " + taskContract.TaskEntry.COLUMN_REMINDER_UNIT + " != ''", null);

        while (cursorc.moveToNext()) {
            int titleColumnIndex = cursorc.getColumnIndex(taskContract.TaskEntry.COLUMN_TASK_TITLE);
            int idColumnIndex = cursorc.getColumnIndex(taskContract.TaskEntry._ID);
            int ReminderUnitBeforeColumnIndex = cursorc.getColumnIndex(taskContract.TaskEntry.COLUMN_REMINDER_UNIT_BEFORE);
            int ReminderUnitColumnIndex = cursorc.getColumnIndex(taskContract.TaskEntry.COLUMN_REMINDER_UNIT);
            int dueDateColumnIndex = cursorc.getColumnIndex(taskContract.TaskEntry.COLUMN_DATETIME);

            String taskTitle = cursorc.getString(titleColumnIndex);
            int taskID = cursorc.getInt(idColumnIndex);
            String mReminderUnitBefore = cursorc.getString(ReminderUnitBeforeColumnIndex);
            String mReminderUnit = cursorc.getString(ReminderUnitColumnIndex);
            long dueDate = cursorc.getLong(dueDateColumnIndex);

            Intent intent = new Intent(context, AlertReceiverReminder.class);
            intent.putExtra("title", taskTitle);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, taskID, intent, 0);

            Calendar reminderCalander = Calendar.getInstance();
            reminderCalander.setTimeInMillis(dueDate);

            //makes sure the alarm date hasn't already passed before setting alarm
            if (!todayDate.after(reminderCalander)) {

                switch (mReminderUnitBefore) {
                    case ("Minutes before"):
                        reminderCalander.add(Calendar.MINUTE, -Integer.valueOf(mReminderUnit));
                        break;
                    case ("Hours before"):
                        reminderCalander.add(Calendar.HOUR_OF_DAY, -Integer.valueOf(mReminderUnit));
                        break;
                    case ("Days before"):
                        reminderCalander.add(Calendar.DAY_OF_YEAR, -Integer.valueOf(mReminderUnit));
                        break;

                    case ("Weeks before"):
                        reminderCalander.add(Calendar.WEEK_OF_YEAR, -Integer.valueOf(mReminderUnit));
                        break;
                }
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, (reminderCalander.getTimeInMillis()), pendingIntent);
            }
        }
    }
}
