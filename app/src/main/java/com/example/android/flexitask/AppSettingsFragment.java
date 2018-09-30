package com.example.android.flexitask;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by Ryan Mcgoff (4086944), Jerry Kumar (3821971), Jaydin Mcmullan (9702973)
 * This class handles the App settings, adding preferences from resource and implementing the time
 * dialog from https://stackoverflow.com/questions/5533078/timepicker-in-preferencescreen/10608622
 * (CREDIT TO Dalija Prasnikar)
 */
public class AppSettingsFragment extends PreferenceFragmentCompat {


    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        DialogFragment dialogFragment = null;
        if (preference instanceof TimePreferenceObject)
        {
            dialogFragment = new TimePreferenceDialogFragmentCompat();
            Bundle bundle = new Bundle(1);
            bundle.putString("key", preference.getKey());
            dialogFragment.setArguments(bundle);
        }

        if (dialogFragment != null)
        {
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(this.getFragmentManager(), "android.support.v7.preference.PreferenceFragment.DIALOG");
        }
        else
        {
            super.onDisplayPreferenceDialog(preference);
        }

    }


    /**
     * {@inheritDoc}
     * Inflates menu and removes the delete label item as the settings fragment doesn't need it
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.timeline_menu, menu);
        menu.removeItem(menu.findItem(R.id.deleteLabel).getItemId());
    }

    /**
     * {@inheritDoc}
     * Displays the setting's help message when selected from the options menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //displays the setting's help message when clicked
            case R.id.help:
                HelpDialog bd = new HelpDialog();
                Bundle b = new Bundle();
                b.putString(HelpDialog.TITLE_KEY, "Settings help");
                b.putString(HelpDialog.MESSAGE_KEY, getResources().getString(R.string.settingsHelpMessage));
                bd.setArguments(b);
                bd.show(getFragmentManager(), "help fragment");

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * {@inheritDoc}
     * Displays/inflates a custom preference resource file
     */
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
    }
}
