package com.example.android.flexitask;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.android.flexitask.data.taskContract;
import com.example.android.flexitask.data.taskDBHelper;

import java.util.Calendar;

/**
 * Created by Ryan Mcgoff (4086944), Jerry Kumar (3821971), Jaydin Mcmullan (9702973)
 * This class is the main class which acts as a container for all the fragments
 * It inflates an XML layout which contains a nav draw, it then programtically checks for when an item in this
 * nav draw is selected and switch the current fragment it is displaying with the new one.
 * The class also sets up an alarm anytime the user opens the App.
 */
public class mainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NewTaskLabelDialog.NewTaskLabelDialogListener {

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private android.support.v7.widget.Toolbar toolbar;
    private NavigationView navigationView;
    private SubMenu subMenu;
    private SharedPreferences sharedPreferences;
    private int itemID;
    private int labelCount = 2;
    private MenuItem createNew;
    private View header;


    /**
     * {@inheritDoc}
     */
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    /**
     * {@inheritDoc}
     * Inflates a custom XML layout for the dialog to use
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintest);

        /*registers a brodcastmanager to receive intents with label delete
         this allow the timeline fragments to communicate with main
        and notify it when a label has been deleted*/
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("labeldelete"));

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        long goalWeek = preferences.getLong("goalWeek", 0);


        Calendar calanderGoal = Calendar.getInstance();
        long thisWeek = calanderGoal.get(Calendar.WEEK_OF_YEAR);
        //checks if its a new week
        if ((goalWeek != thisWeek)) {
            SharedPreferences.Editor preferencesEdit = preferences.edit().putLong("goalWeek", thisWeek);
            preferencesEdit.apply();
            //resets weekly tasks to 0 (for new week)
            SharedPreferences.Editor preferencesEdit2 = preferences.edit().putLong("weekTasks", 0);
            preferencesEdit2.apply();
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.nav_draw);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                int flexiCountInt = preferences.getInt("flexiCount", 0);
                int fixedCountInt = preferences.getInt("fixedCount", 0);
                long weekTasks = preferences.getLong("weekTasks", 0);
                long goal = preferences.getInt("goal", 5);

                TextView flipFlexi = findViewById(R.id.flipFlexi);
                TextView flipFixed = findViewById(R.id.flipFixed);
                TextView flipWeekly = findViewById(R.id.flipWeekly);
                String flipFixedMessage = "Life time FixedTasks " + String.valueOf(fixedCountInt);
                String flipFlexiMessage = "Life time FlexiTasks " + String.valueOf(flexiCountInt);
                String flipWeeklyMessage = "Weekly goal " + weekTasks + "/" + goal;

                if (flipFixed != null) {
                    flipFixed.setText(flipFixedMessage);
                }
                if (flipFlexi != null) {
                    flipFlexi.setText(flipFlexiMessage);
                }
                if (flipWeekly != null) {
                    flipWeekly.setText(flipWeeklyMessage);
                }
            }
        };
        actionBarDrawerToggle.setDrawerSlideAnimationEnabled(false);

        //Set the drawer toggle as the DrawerListener
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        setAlarm();
        MenuItem menuItem = null;

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        header = navigationView.getHeaderView(0);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                headerOnClick();

            }
        });

        displayView(R.id.nav_tasks, menuItem);

        Menu menu = navigationView.getMenu();
        subMenu = menu.addSubMenu("Labels");

        createNew = subMenu.add(0, 1, Menu.NONE, "Create new...").setIcon(R.drawable.ic_add_black_24dp).setCheckable(false).setChecked(false);

        taskDBHelper mDbHelper = new taskDBHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //Add labels to submenu
        Cursor cursorc = db.rawQuery("SELECT * FROM " + taskContract.TaskEntry.LABEL_TABLE_NAME, null);
        while (cursorc.moveToNext()) {
            int labelNameColumnIndex = cursorc.getColumnIndex(taskContract.TaskEntry.COLUMN_LABEL_NAME);
            String labelName = cursorc.getString(labelNameColumnIndex);

            subMenu.add(1, labelCount, 0, labelName);
            labelCount++;
            subMenu.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        db.close();

        subMenu.setGroupCheckable(0, true, true);
        navigationView.setNavigationItemSelectedListener(this);
        colorSwitch();
    }


    /**
     * Sets daily alarm for the first time
     */
    public void setAlarm() {
        //get notification time preference
        String alarmS = PreferenceManager.getDefaultSharedPreferences(this).getString("time", "08:00");

        String[] timeArray = alarmS.split(":");
        int hour = (Integer.parseInt(timeArray[0]));
        int min = (Integer.parseInt(timeArray[1]));

        Calendar todayC = Calendar.getInstance();

        Calendar timePickerC = (Calendar) todayC.clone();
        timePickerC.set(Calendar.HOUR_OF_DAY, hour);
        timePickerC.set(Calendar.MINUTE, min);

        // if the time is after or equal to the notification hour / min schedule alarm for the next day
        if (todayC.after(timePickerC)) {
            timePickerC.add(Calendar.DAY_OF_YEAR, 1); //add day
        }

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timePickerC.getTimeInMillis(), pendingIntent);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("label", "all");
        editor.apply();

    }

    /**
     * {@inheritDoc}
     * Displays the corresponding fragment to the item the user has clicked on from the navigation
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        displayView(item.getItemId(), item);
        return true;
    }

    public void headerOnClick() {
        Intent intent = new Intent(this, ProductivityActivity.class);
        startActivity(intent);
    }

    /**
     * Checks which fragment the user has cliked on in the in the navigation draw
     * and begins transitioning to that fragment inside MainActiivty's XML content Frame.
     *
     * @param viewId is the ID of the fragment the user has clicked
     */
    public void displayView(int viewId, MenuItem item) {
        Fragment fragment = null;
        String title = "Tasks";
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.nav_draw);
        Log.v(String.valueOf(viewId), "");
        switch (viewId) {
            case R.id.nav_tasks:
                fragment = new TimelineFragmentsContainer();
                title = "Tasks";
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("label", "All");
                editor.apply();
                drawer.closeDrawer(GravityCompat.START);

                break;
            case R.id.nav_history:
                fragment = new TaskHistoryFragment();
                title = "History";
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_settings:
                fragment = new AppSettingsFragment();
                title = "Settings";
                drawer.closeDrawer(GravityCompat.START);
                break;
            case 1:
                openDialog();
                createNew.setChecked(false).setCheckable(false);
                break;
            default:

                Log.d("itemid", String.valueOf(itemID));

                if (itemID != 0) {
                    int lastItemID = itemID;
                    Log.d("item", "rec message");
                    MenuItem lastItem = subMenu.findItem(lastItemID);

                    if (lastItem != null) {
                        lastItem.setChecked(false);
                    }
                }

                itemID = item.getItemId();
                item.setChecked(true);

                title = item.getTitle().toString();
                SharedPreferences.Editor editor2 = sharedPreferences.edit();
                editor2.putString("label", title);
                editor2.apply();
                Bundle args = new Bundle();
                args.putString("label", item.getTitle().toString());
                subMenu.getItem().getSubMenu();
                //subMenu.removeItem(viewId);
                fragment = new TimelineFragmentsContainer();
                fragment.setArguments(args);
                drawer.closeDrawer(GravityCompat.START);
        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        }

        setTitle(title);

    }

    /*Checks user colour preferences and changes UI*/
    private void colorSwitch() {
        String colourSetting = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .getString("color_preference_key", "OCOLOUR");

        switch (colourSetting) {
            case ("DCOLOUR"):
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryD));
                header.setBackgroundColor(getResources().getColor(R.color.colorPrimaryD));


                break;

            case ("PCOLOUR"):

                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryP));
                header.setBackgroundColor(getResources().getColor(R.color.colorPrimaryP));

                break;

            case ("TCOLOUR"):
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryT));
                header.setBackgroundColor(getResources().getColor(R.color.colorPrimaryT));

                break;
            default:
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                header.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
    }


    /**
     * Displays dialog that lets the user create a new label
     */
    private void openDialog() {

        NewTaskLabelDialog newTaskLabelDialog = new NewTaskLabelDialog();
        newTaskLabelDialog.show(getSupportFragmentManager(), "newTaskLabelDialog");
    }


    /*When the BroadcastReceiver manager receives a message from the timeline
    *notifying it that a label has been deleted, the BroadcastReceiver navigates the display
    * back to display all tasks and removes the label from the submenu.
     */
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String labelToDelete = intent.getStringExtra("labelToDelete");
            displayView(R.id.nav_tasks, null);
            subMenu.removeItem(itemID);
        }
    };

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }


    /**
     * Interfaces with NewTask dialog, when a new label is created this method adds it to the
     * navigation draw and the SQL database
     *
     * @param newTextLabel to be added to databse and nav bar
     */
    @Override
    public void applyNewTaskLabelName(String newTextLabel) {
        Toast.makeText(this, newTextLabel, Toast.LENGTH_LONG).show();
        taskDBHelper mDbHelper = new taskDBHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(taskContract.TaskEntry.COLUMN_LABEL_NAME, newTextLabel);
        db.insert(taskContract.TaskEntry.LABEL_TABLE_NAME, null, cv);
        db.close();
        Menu menu = navigationView.getMenu();
        subMenu.add(0, labelCount, 0, newTextLabel);
        labelCount++;
        navigationView.setNavigationItemSelectedListener(this);

    }

}
