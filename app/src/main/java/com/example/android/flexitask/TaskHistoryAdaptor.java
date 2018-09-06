package com.example.android.flexitask;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.flexitask.data.taskContract;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ryan Mcgoff (4086944), Jerry Kumar (3821971), Jaydin Mcmullan (9702973)
 *
 * A Cursor Adaptor  lets Android manage resources more efficiently
 * by retrieving and releasing row and column values when the user scrolls, rather than loading everything into memory.
 * This is a custom extension of CursorAdaptor for tasks
 *
 */
public class TaskHistoryAdaptor extends CursorAdapter {

    public TaskHistoryAdaptor(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //find views for list item
        TextView titleTextView = view.findViewById(R.id.titleListView);
        TextView descriptionTextView = view.findViewById(R.id.descriptionListView);
        TextView dateView = view.findViewById(R.id.testView);

        //find the column values
        int titleColumnIndex = cursor.getColumnIndex(taskContract.TaskEntry.COLUMN_TASK_TITLE);
        int descriptionColumnIndex = cursor.getColumnIndex(taskContract.TaskEntry.COLUMN_TYPE_TASK);
        int lastCompletedIndex = cursor.getColumnIndex(taskContract.TaskEntry.COLUMN_LAST_COMPLETED);

        //Read the values for current Tasks
        String titleString = cursor.getString(titleColumnIndex);


        int type = cursor.getInt(descriptionColumnIndex);
        String descriptionString = "";

        if(type==(taskContract.TaskEntry.TYPE_FIXED)){
            descriptionString = "Fixed Task";

        }
        else{
            descriptionString = "Flexi Task";

        }

        //int taskType = cursor.getInt(taskTypeColumnIndex);
        long lastCompletedLong = cursor.getLong(lastCompletedIndex);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = formatter.format(new Date(lastCompletedLong));

        dateView.setText(dateString);
        titleTextView.setText(titleString);
        descriptionTextView.setText(descriptionString);

    }
}
