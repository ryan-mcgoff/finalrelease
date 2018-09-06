package com.example.android.flexitask;

/**
 * Adapted from Dalija Prasnikar GITHub solution
 * https://stackoverflow.com/questions/5533078/timepicker-in-preferencescreen/10608622.
 */
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.preference.DialogPreference;
import android.util.AttributeSet;

public class TimePreferenceObject extends DialogPreference {

    public int hour = 0;
    public int minute = 0;


    public static int hourStrToInt(String value) {

            String[] timeArray = value.split(":");
            int hourInt =  (Integer.parseInt(timeArray[0]));
            return hourInt;

    }

    public static int minStrToInt(String value){
            String[] timeArray = value.split(":");
            int minInt = (Integer.parseInt(timeArray[1]));
            return minInt;

    }

    public static String timeToString(int h, int m)
    {
        return String.format("%02d", h) + ":" + String.format("%02d", m);
    }

    public TimePreferenceObject(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray typedArray, int i) {

        return typedArray.getString(i);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue)
    {
        String value;
        if (restoreValue)
        {
            if (defaultValue == null) value = getPersistedString("00:00");
            else value = getPersistedString(defaultValue.toString());
        }
        else {
            value = defaultValue.toString();
        }

        hour = hourStrToInt(value);
        minute = minStrToInt(value);
    }

    public void persistStringValue(String value)
    {
        persistString(value);
    }
}