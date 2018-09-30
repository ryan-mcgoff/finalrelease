package com.example.android.flexitask;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import me.itangqi.waveloadingview.WaveLoadingView;

/**
 * Created by rymcg on 13/09/2018.
 */

public class ProductivityActivity extends AppCompatActivity implements EditWeeklyGoalDialog.EditGoalListener {

    private Toolbar toolbar;
    private WaveLoadingView waveLoadingView;
    private Button editButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productivity_activity);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        waveLoadingView = findViewById(R.id.waveView);
        editButton = findViewById(R.id.editButton);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        TextView flexiCount = findViewById(R.id.flexiCount);
        TextView fixedCount = findViewById(R.id.fixedCoCount);
        TextView totalCount = findViewById(R.id.totalCount);
        int flexiCountInt = preferences.getInt("flexiCount", 0);
        int fixedCountInt = preferences.getInt("fixedCount", 0);
        flexiCount.setText(String.valueOf(flexiCountInt));
        fixedCount.setText(String.valueOf(fixedCountInt));
        totalCount.setText(String.valueOf(flexiCountInt+fixedCountInt));

        //get goal value and week completed tasks value
        TextView goalDisplay = findViewById(R.id.goalDisplayLabel);
        long weekTasks = preferences.getLong("weekTasks",0);
        long goal = preferences.getInt("goal",5);

        String goalDisplayMessage = String.valueOf(weekTasks) + "/" + String.valueOf(goal) + " Tasks";

        int percentage;

        if(goal!=0) {
            percentage = (int) ((weekTasks * 100 / goal));
        }
        else{
            percentage = 0;
        }

        totalCount.setText(String.valueOf(flexiCountInt+fixedCountInt));

        if(percentage>100){
            waveLoadingView.setProgressValue(100);

        }
        else{
            waveLoadingView.setProgressValue(percentage);
        }

        goalDisplay.setText(goalDisplayMessage);
        colorSwitch();
    }

    /*Checks user colour preferences and changes UI*/
    private void colorSwitch() {
        String colourSetting = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .getString("color_preference_key", "OCOLOUR");

        switch (colourSetting) {
            case ("DCOLOUR"):
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryD));
                waveLoadingView.setWaveColor(getResources().getColor(R.color.colorPrimaryD));
                editButton.setTextColor(getResources().getColor(R.color.colorPrimaryD));


                break;

            case ("PCOLOUR"):

                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryP));
                waveLoadingView.setWaveColor(getResources().getColor(R.color.colorPrimaryP));
                editButton.setTextColor(getResources().getColor(R.color.colorPrimaryP));

                break;

            case ("TCOLOUR"):
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryT));
                waveLoadingView.setWaveColor(getResources().getColor(R.color.colorPrimaryT));
                editButton.setTextColor(getResources().getColor(R.color.colorPrimaryT));

                break;
            default:
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                waveLoadingView.setWaveColor(getResources().getColor(R.color.colorPrimary));
                editButton.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    /**Called when Edit Button is clicked*/
    public void openDialog(View view){
        EditWeeklyGoalDialog goalDialog = new EditWeeklyGoalDialog();
        goalDialog.show(getSupportFragmentManager(),"newEditGoalDialog");
    }


    @Override
    public void applyNewGoal(int goal) {
        //set Goal
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor preferencesEdit =preferences.edit().putInt("goal",goal);
        preferencesEdit.apply();


    }
}
