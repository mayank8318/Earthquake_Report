package com.example.android.quakereport;

import android.renderscript.ScriptGroup;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.SimpleTimeZone;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /** Sample JSON response for a USGS query */
    //private static final String SAMPLE_JSON_RESPONSE ="https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2012-01-01&endtime=2012-12-01&minmagnitude=6";

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    private static URL createURL(String url)
    {
        URL url1 = null;
        try {
            url1 = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url1;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String response = "";
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(10000000);
            httpURLConnection.setConnectTimeout(100000000);
            httpURLConnection.connect();

            if(httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                response = readInputStream(inputStream);
            }
            else
            {
                Log.v("Error", httpURLConnection.getResponseCode()+"");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(httpURLConnection != null)
                httpURLConnection.disconnect();
            if(inputStream != null)
                inputStream.close();
        }

        return response;
    }

    private static String readInputStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line ="";
        while ((line = bufferedReader.readLine())!=null)
        {
            output.append(line);
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Earthquake> extractEarthquakes(String urlString) throws IOException {


        URL url = createURL(urlString);

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Earthquake> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

            JSONObject jsonObject = new JSONObject(makeHttpRequest(url));
            JSONArray features = jsonObject.optJSONArray("features");

            for(int i=0; i<features.length(); i++)
            {
                JSONObject props = features.getJSONObject(i).getJSONObject("properties");
                double mag = props.getDouble("mag");
                String time = props.getLong("time")+"";
                String place = props.getString("place");

                Date date = new Date(Long.parseLong(time));
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");

                SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");

                DecimalFormat decimalFormat = new DecimalFormat("0.0");


                earthquakes.add(new Earthquake(Double.parseDouble(decimalFormat.format(mag)),place,dateFormat.format(date),timeFormat.format(date),props.getString("url")));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

}