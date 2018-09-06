package com.example.android.flexitask;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

/**
 * Adapted from Dalija Prasnikar GITHub solution
 * https://stackoverflow.com/questions/5533078/timepicker-in-preferencescreen/10608622.
 */

import android.support.v7.preference.Preference;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class TimePreferenceDialogFragmentCompat extends PreferenceDialogFragmentCompat implements DialogPreference.TargetFragment
    {
        TimePicker timePicker = null;

        @Override
        protected View onCreateDialogView(Context context)
        {
            timePicker = new TimePicker(context);
            return (timePicker);
        }

        @Override
        protected void onBindDialogView(View v)
        {
            super.onBindDialogView(v);
            timePicker.setIs24HourView(true);
            TimePreferenceObject pref = (TimePreferenceObject) getPreference();
            timePicker.setCurrentHour(pref.hour);
            timePicker.setCurrentMinute(pref.minute);
        }

        @Override
        public void onDialogClosed(boolean dialogChanged)
        {
            if (dialogChanged)
            {
                TimePreferenceObject preference = (TimePreferenceObject) getPreference();
                preference.hour = timePicker.getCurrentHour();
                preference.minute = timePicker.getCurrentMinute();

                int hour = timePicker.getCurrentHour();
                int min = timePicker.getCurrentMinute();

                //sets alarm
                setAlarm(hour,min);

                String value = TimePreferenceObject.timeToString(preference.hour, preference.minute);
                if (preference.callChangeListener(value)){
                    preference.persistStringValue(value);
                }
            }
        }

        public void setAlarm(int hour, int min){

            Log.e("setAlarm In Dialog: ", "alarm d");

            // Current time
            Calendar todayC = Calendar.getInstance();

            Calendar timePickerC = (Calendar) todayC.clone();
            timePickerC.set(Calendar.HOUR_OF_DAY, hour);
            timePickerC.set(Calendar.MINUTE, min);



            Log.e("alarm: ", "SYSTEM HOUR: " + String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + " GIVEN HOUR:  " +
                    String.valueOf(hour)));

            Log.e("alarm: ", "SYSTEM MIN: " + String.valueOf(Calendar.getInstance().get(Calendar.MINUTE) + " GIVEN MIN:  " +
                    String.valueOf(min)));


            // if it's after or equal to the notification hour / min schedule for next day
            if (todayC.after(timePickerC)){
                timePickerC.add(Calendar.DAY_OF_YEAR, 1); //add day
                Log.e("alarm: ", "Alarm will schedule for tomorrow!");
            }
            else{
                Log.e("alarm: ", "Alarm will schedule for today!");
            }

            AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(getContext(),AlertReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),1,intent,0);

            alarmManager.setExact(AlarmManager.RTC_WAKEUP,timePickerC.getTimeInMillis(),pendingIntent);

        }

        @Override
        public Preference findPreference(CharSequence charSequence)
        {
            return getPreference();
        }
    }

