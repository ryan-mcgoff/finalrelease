package com.example.android.flexitask;



import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.preference.DialogPreference;
import android.util.AttributeSet;
/**
 * Adapted from Dalija Prasnikar GITHub solution
 * https://stackoverflow.com/questions/5533078/timepicker-in-preferencescreen/10608622.
 */
public class TimePreferenceObject extends DialogPreference {

    int hour = 0;
    int minute = 0;


    private static int hourStrToInt(String value) {

        String[] timeArray = value.split(":");
        return (Integer.parseInt(timeArray[0]));

    }

    private static int minStrToInt(String value) {
        String[] timeArray = value.split(":");
        return (Integer.parseInt(timeArray[1]));

    }

    @SuppressLint("DefaultLocale")
    static String timeToString(int h, int m) {
        return String.format("%02d", h) + ":" + String.format("%02d", m);
    }

    public TimePreferenceObject(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray typedArray, int i) {

        return typedArray.getString(i);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        String value;
        if (restoreValue) {
            if (defaultValue == null) value = getPersistedString("00:00");
            else value = getPersistedString(defaultValue.toString());
        } else {
            value = defaultValue.toString();
        }

        hour = hourStrToInt(value);
        minute = minStrToInt(value);
    }

    void persistStringValue(String value) {
        persistString(value);
    }
}