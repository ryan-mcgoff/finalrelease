package com.example.android.flexitask;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Ryan McGoff (4086944), Jerry Kumar (3821971), Jaydin Mcmullan (9702973)
 * <p>
 * Creates a simple contextual help dialog based on which frgament the user is current on
 */
public class HelpDialog extends AppCompatDialogFragment {

    public static final String TITLE_KEY = "title";
    public static final String MESSAGE_KEY = "message";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.help_dialog, null);

        Bundle args = getArguments();
        String title = args.getString(TITLE_KEY, "help error");
        String message = args.getString(MESSAGE_KEY, "help error");

        builder.setView(view);
        builder.setTitle(title);
        TextView messageView = view.findViewById(R.id.dialog_message);
        messageView.setText(message);

        builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        return builder.create();
    }
}
