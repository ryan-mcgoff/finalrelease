package com.example.android.flexitask;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import me.itangqi.waveloadingview.WaveLoadingView;

/**
 * Created by rymcg on 13/09/2018.
 */

public class ProductivityActivity extends AppCompatActivity implements EditWeeklyGoalDialog.EditGoalListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productivity_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        WaveLoadingView waveLoadingView = findViewById(R.id.waveView);
        waveLoadingView.setProgressValue(70);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        TextView flexiCount = findViewById(R.id.flexiCount);
        TextView fixedCount = findViewById(R.id.fixedCoCount);
        TextView totalCount = findViewById(R.id.totalCount);
        int flexiCountInt = preferences.getInt("flexiCount", 0);
        int fixedCountInt = preferences.getInt("fixedCount", 0);
        flexiCount.setText(String.valueOf(flexiCountInt));
        fixedCount.setText(String.valueOf(fixedCountInt));
        totalCount.setText(String.valueOf(flexiCountInt+fixedCountInt));

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void applyNewGoal(int goal) {
        //set Goal
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor preferencesEdit =preferences.edit().putInt("goal",goal);
        preferencesEdit.commit();


    }
}
