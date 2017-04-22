/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import android.app.LoaderManager;
import android.content.Loader;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;


public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Earthquake>> {
    public final String UR = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=2";
    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    ListView earthquakeListView;
    TextView empty;
    //ArrayList<Earthquake> earthquakes;
    EarthquakeArrayAdapter earthquakeArrayAdapter;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        progressBar = (ProgressBar) findViewById(R.id.loading_indicator);
        empty = (TextView) findViewById(R.id.empty_view);
        earthquakeListView = (ListView) findViewById(R.id.list);
        earthquakeListView.setEmptyView(empty);
        earthquakeArrayAdapter = new EarthquakeArrayAdapter(EarthquakeActivity.this, new ArrayList<Earthquake>());
        // Create a new {@link ArrayAdapter} of earthquakes
        earthquakeListView.setAdapter(earthquakeArrayAdapter);

        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if((networkInfo != null) && networkInfo.isConnected())
        {
            // Set the adapter on the {@link ListView}
            // so the list can be populated in the user interface
            getLoaderManager().initLoader(0, null, this);

            earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Earthquake current = (Earthquake) adapterView.getItemAtPosition(i);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(current.getUrl()));
                    startActivity(intent);
                }
            });
        }

        // Create a fake list of earthquake locations.
        // Find a reference to the {@link ListView} in the layout
        else {
            empty.setText("No internet");
            progressBar.setVisibility(View.GONE);
        }
    }


    @Override
    public Loader<ArrayList<Earthquake>> onCreateLoader(int id, Bundle args) {
        return new EarthquakeLoader(this,UR);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Earthquake>> loader, ArrayList<Earthquake> data) {

        progressBar.setVisibility(View.GONE);
        earthquakeArrayAdapter.clear();

        if(data != null && data.isEmpty()==false)
            earthquakeArrayAdapter.addAll(data);

        empty.setText("NOPEEE");
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Earthquake>> loader) {
        earthquakeArrayAdapter.clear();
    }
}
