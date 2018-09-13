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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

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
        final View view = inflater.inflate(R.layout.task_reminder_dialog,null);

        String units = getArguments().getString("units");
        String unitsBefore = getArguments().getString("unitsBefore");

        if(units.equals("")&&unitsBefore.equals("")){
            RadioButton b = (RadioButton) view.findViewById(R.id.minsBefore);
            b.setChecked(true);
        }
        else{
            EditText unitsView = view.findViewById(R.id.editReminderUnits);
            unitsView.setText(units);
            switch(unitsBefore){
                case "Hours before":
                    RadioButton bHour = (RadioButton) view.findViewById(R.id.hoursBefore);
                    bHour.setChecked(true);
                    break;
                case "Days before":
                    RadioButton bDay = (RadioButton) view.findViewById(R.id.daysBefore);
                    bDay.setChecked(true);
                    break;
                case "Weeks before":
                    RadioButton bWeek = (RadioButton) view.findViewById(R.id.weeksBefore);
                    bWeek.setChecked(true);
                    break;
                default:
                    RadioButton bMin = (RadioButton) view.findViewById(R.id.minsBefore);
                    bMin.setChecked(true);
            }
        }




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

                RadioGroup radioGroup = view.findViewById(R.id.unitsBeforeRadioGroup);
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton selectedRadio =  view.findViewById(selectedId);

                String unitsBefore = selectedRadio.getText().toString();

                listener.applyNotificationReminder(units,unitsBefore);


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
        void applyNotificationReminder (String unitsReminder,String unitsBefore);
    }

}
