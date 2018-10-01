package com.example.android.flexitask;

import android.content.Intent;
import android.test.mock.MockContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rymcg on 1/10/2018.
 */
public class TestContext extends MockContext
{
    private ArrayList<Intent> mReceivedIntents = new ArrayList<Intent>();

    @Override
    public String getPackageName()
    {
        return "com.example.android.flexitask";
    }

    @Override
    public void startActivity(Intent xiIntent)
    {
        mReceivedIntents.add(xiIntent);
    }

    public List<Intent> getReceivedIntents()
    {
        return mReceivedIntents;
    }
}