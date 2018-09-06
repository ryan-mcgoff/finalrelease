package com.example.android.flexitask;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.example.android.flexitask.data.taskContract;
import com.example.android.flexitask.data.taskDBHelper;

import java.util.Calendar;

/**
 * Created by Ryan Mcgoff (4086944), Jerry Kumar (3821971), Jaydin Mcmullan (9702973)
 *
 * A {@link Fragment} subclass for the history fragment of the app
 * that implements the {@link LoaderManager] interface to retrieve deactived task data to the a cursor
 * adaptor for the fragment's listview. This is based on what filters the user has selected (chosen via buttons)
 *
 */
public class TaskHistoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int TASKLOADER = 0;
    TaskHistoryAdaptor mTaskCursorAdaptor;
    private taskDBHelper mDbHelper;

    private Button timeBtnSelected;
    private Button timeBtnDeselect;
    private Button taskBtnSelected;
    private Button taskBtnDeselect;

    private View rootView;

    private long todayDate;
    private long dateFilter;
    private int taskFilter;

    private int selectedDrawableID;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);


        rootView = inflater.inflate(R.layout.fragment_task_history, container, false);


        //checks colour preferences set by the user and makes appropriate changes
        checkColourChange();

        todayDate = Calendar.getInstance().getTimeInMillis();
        dateFilter = todayDate;
        taskFilter = taskContract.TaskEntry.TYPE_ALL;

        timeBtnSelected = rootView.findViewById(R.id.btnAnyTime);
        timeBtnSelected.setBackgroundResource(selectedDrawableID);

        taskBtnSelected = rootView.findViewById(R.id.btnAllTasks);
        taskBtnSelected.setBackgroundResource(selectedDrawableID);


        mDbHelper = new taskDBHelper(getActivity());

        // Find the ListView which will be populated with the tasks data
        final ListView timeLineListView = (ListView) rootView.findViewById(R.id.historyListView);

        mTaskCursorAdaptor = new TaskHistoryAdaptor(getActivity(), null);
        timeLineListView.setAdapter(mTaskCursorAdaptor);

        getLoaderManager().initLoader(TASKLOADER, null, this);

        RadioGroup timeButtonGroup = rootView.findViewById(R.id.btnTimeGroup);
        RadioGroup taskButtonGroup = rootView.findViewById(R.id.btnTaskGroup);


        /*Checks which time filter the user selects and restarts loader to fetch this data*/
        timeButtonGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.btnAnyTime:

                        timeBtnSelectDeselect(checkedId);
                        dateFilter = todayDate;
                        getLoaderManager().restartLoader(TASKLOADER, null, TaskHistoryFragment.this);
                        break;
                    case R.id.btnLastDay:
                        timeBtnSelectDeselect(checkedId);
                        dateFilter = todayDate - (86400000L);
                        getLoaderManager().restartLoader(TASKLOADER, null, TaskHistoryFragment.this);

                        break;
                    case R.id.btnLastWeek:
                        timeBtnSelectDeselect(checkedId);
                        dateFilter = todayDate - (86400000L*7);
                        getLoaderManager().restartLoader(TASKLOADER, null, TaskHistoryFragment.this);
                        break;
                    case R.id.btnLastMonth:
                        timeBtnSelectDeselect(checkedId);
                        dateFilter = todayDate - (86400000L*31);
                        getLoaderManager().restartLoader(TASKLOADER, null, TaskHistoryFragment.this);
                        break;
                    case R.id.btnLastYear:
                        timeBtnSelectDeselect(checkedId);
                        dateFilter = todayDate - (86400000L*365);
                        getLoaderManager().restartLoader(TASKLOADER, null, TaskHistoryFragment.this);
                        break;
                }
            }
        });

        /*Checks which history a user wants for a task  the user wants and restarts loader to fetch this data*/
        taskButtonGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.btnAllTasks:
                        taskBtnSelectDeselect(checkedId);
                        taskFilter = taskContract.TaskEntry.TYPE_ALL;
                        getLoaderManager().restartLoader(TASKLOADER, null, TaskHistoryFragment.this);
                        break;
                    case R.id.btnFixedTasks:
                        taskBtnSelectDeselect(checkedId);
                        taskFilter = taskContract.TaskEntry.TYPE_FIXED;
                        getLoaderManager().restartLoader(TASKLOADER, null, TaskHistoryFragment.this);
                        break;
                    case R.id.btnFlexiTasks:
                        taskBtnSelectDeselect(checkedId);
                        taskFilter = taskContract.TaskEntry.TYPE_FLEXI;
                        getLoaderManager().restartLoader(TASKLOADER, null, TaskHistoryFragment.this);
                        break;

                }

            }
        });


        return rootView;
    }

    /**
     * Method changes colours for the history fragment based on what colour blind mode the user has selected
     */
    private void checkColourChange() {
        String colourSetting = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getString("color_preference_key", "OCOLOUR");

        switch (colourSetting) {
            case ("DCOLOUR"):
                selectedDrawableID = R.drawable.oval_shape_selected_d;

                break;

            case ("PCOLOUR"):
                selectedDrawableID = R.drawable.oval_shape_selected_p;
                break;

            case ("TCOLOUR"):
                selectedDrawableID = R.drawable.oval_shape_selected_t;

                break;
            default:
                selectedDrawableID = R.drawable.oval_shape_selected;
        }
    }

    private void timeBtnSelectDeselect(int checkID){
        timeBtnDeselect = timeBtnSelected;
        timeBtnSelected = rootView.findViewById(checkID);
        timeBtnDeselect.setBackgroundResource(R.drawable.oval_shape);
        timeBtnSelected.setBackgroundResource(selectedDrawableID);
    }
    private void taskBtnSelectDeselect(int checkID){
        taskBtnDeselect = taskBtnSelected;
        taskBtnSelected = rootView.findViewById(checkID);
        taskBtnDeselect.setBackgroundResource(R.drawable.oval_shape);
        taskBtnSelected.setBackgroundResource(selectedDrawableID);
    }


    /**
     * This onCreateLoader method creates a new Cursor that contains the URI for the database and the columns wanted. It's passed
     * to the content resolver which uses the URI to determine which contentProvider we want. The contentProvider is then
     * responsible for interfacing with the database and returning a cursor (with the requested data from
     * our projection) back to the contentResolver, and finally back to the loadermanager. The loader manager then passes
     * this cursor object to the {@link #onLoadFinished(Loader, Cursor)} .
     *
     * @param id   the loader's ID
     * @param args any arguments you want to pass to the loader when creating it (NOT USED)
     * @return new cursorLoader with SQL projection and URI for the database we want to query. (this is passed to the cotent
     * resolver by the loaderManager)
     */
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        //Defines a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                taskContract.TaskEntry._ID,
                taskContract.TaskEntry.COLUMN_TASK_TITLE,
                taskContract.TaskEntry.COLUMN_DESCRIPTION,
                taskContract.TaskEntry.COLUMN_TYPE_TASK,
                taskContract.TaskEntry.COLUMN_LAST_COMPLETED,
                taskContract.TaskEntry.COLUMN_DATE,
                taskContract.TaskEntry.COLUMN_TIME,
                taskContract.TaskEntry.COLUMN_HISTORY,
                taskContract.TaskEntry.COLUMN_STATUS,
                taskContract.TaskEntry.COLUMN_RECCURING_PERIOD};

        String WHERE = "status='0'";

        if(dateFilter!=todayDate){
            Log.v("Date", "date FIlter not equal to today");
            WHERE+= " AND "+ taskContract.TaskEntry.COLUMN_LAST_COMPLETED + ">"+String.valueOf(dateFilter);
        }
        if(taskFilter!= taskContract.TaskEntry.TYPE_ALL){
            WHERE+= " AND task_type = ' " + String.valueOf(taskFilter)+"'";
        }

        return new CursorLoader(getActivity(),
                taskContract.TaskEntry.CONTENT_URI,
                projection,
                WHERE,
                null,
                "CAST(" + taskContract.TaskEntry.COLUMN_LAST_COMPLETED + " AS DOUBLE)");
    }

    /**
     * Once the cursor loader set up in {@link #onCreateLoader(int, Bundle)} has been given the
     * returned data cursor, the {@link LoaderManager} calls this method with the data.
     * This method then gives the cursor adaptor the data to process
     *
     * @param loader the cursor loader
     * @param data   the returned cursor with the requested data
     */
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mTaskCursorAdaptor.swapCursor(data);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        mTaskCursorAdaptor.swapCursor(null);
    }

}
