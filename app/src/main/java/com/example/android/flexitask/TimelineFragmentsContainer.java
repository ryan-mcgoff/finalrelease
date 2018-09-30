package com.example.android.flexitask;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.example.android.flexitask.data.taskDBHelper;
import com.github.clans.fab.FloatingActionMenu;


/**
 * Created by Ryan Mcgoff (4086944), Jerry Kumar (3821971), Jaydin Mcmullan (9702973)
 * <p>
 * A fragment that holds the {@link TaskPageAdaptor} for {@link FlexiTaskTimeLine} and {@link FixedTaskTimeLine} fragments
 */
public class TimelineFragmentsContainer extends Fragment {


    private TabLayout tabLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content of the activity to use the activity_main.xml layout file
        View rootView = inflater.inflate(R.layout.activity_main, container, false);
        // Find the view pager that will allow the user to swipe between fragment
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        TaskPageAdaptor adapter = new TaskPageAdaptor(getActivity(), getChildFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Find the tab layout that shows the tabs
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);
        colorSwitch();
        //set here

        return rootView;
    }

    /**
     * Checks user colour preferences and makes appropriate UI changes
     */
    private void colorSwitch() {
        String colourSetting = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getString("color_preference_key", "OCOLOUR");

        switch (colourSetting) {
            case ("DCOLOUR"):
                tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryD));

                break;

            case ("PCOLOUR"):

                tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryP));

                break;

            case ("TCOLOUR"):
                tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryT));

                break;
            default:
                tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
    }

}
