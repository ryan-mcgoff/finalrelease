package com.example.android.flexitask;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by rymcg on 21/09/2018.
 */

public class EditWeeklyGoalDialog extends AppCompatDialogFragment {

    private EditText editTextLabelName;
    private EditGoalListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //change dialog

        View view = inflater.inflate(R.layout.goal_dialog,null);

       final TextView editGoal = view.findViewById(R.id.edit_goal);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String goalValue =  String.valueOf(preferences.getInt("goal",3));
       editGoal.setText(goalValue);

        builder.setView(view).setTitle("Weekly Goal")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int goalValue = Integer.valueOf(editGoal.getText().toString());
                        listener.applyNewGoal(goalValue);

                    }
                });

        //editTextLabelName = view.findViewById(R.id.edit_label_name);


        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (EditGoalListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"" +
                    "Forgot to implement dialog listener");
        }
    }

    public interface EditGoalListener{
        void applyNewGoal (int goal);
    }
}

