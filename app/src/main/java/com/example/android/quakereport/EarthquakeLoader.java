package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.io.IOException;

import java.util.ArrayList;

/**
 * Created by mayank on 10/4/17.
 */

public class EarthquakeLoader extends AsyncTaskLoader<ArrayList<Earthquake>> {

    String mURL;
    public EarthquakeLoader(Context context,String URL)
    {
        super(context);
        mURL = URL;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Earthquake> loadInBackground() {
        ArrayList<Earthquake> earthquakes = new ArrayList<>() ;
        try {
            earthquakes = QueryUtils.extractEarthquakes(mURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return earthquakes;
    }
}
