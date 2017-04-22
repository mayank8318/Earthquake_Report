package com.example.android.quakereport;

/**
 * Created by mayank on 4/4/17.
 */

public class Earthquake {

    private String description;
    private double mag;
    private String date;
    private String time;
    private String url;

    public Earthquake(double m,String desc, String d, String t, String u)
    {
        description = desc;
        mag = m;
        date = d;
        time = t;
        url = u;
    }

    public String getDescription()
    {
        return description;
    }

    public double getMag()
    {
        return mag;
    }

    public String getDate()
    {
        return date;
    }

    public String getTime()
    {
        return time;
    }

    public String getUrl()
    {
        return url;
    }
}
