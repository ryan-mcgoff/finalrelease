package com.example.android.flexitask;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by rymcg on 6/09/2018.
 */

public class TaskReminderDialog extends AppCompatDialogFragment {

    private EditText editUnitReminder;
    private TaskReminderDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.task_reminder_dialog,null);


        builder.setView(view)
                .setTitle("Set notification reminder")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String units = editUnitReminder.getText().toString();
                listener.applyNotificationReminder(units);


            }
        });

        editUnitReminder = view.findViewById(R.id.editReminderUnits);



        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {listener = (TaskReminderDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"" +
                    "Forgot to implement TaskReminderDialogListener");
        }
    }

    public interface TaskReminderDialogListener{
        void applyNotificationReminder (String unitsReminder);
    }

}
