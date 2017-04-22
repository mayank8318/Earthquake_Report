package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mayank on 4/4/17.
 */

public class EarthquakeArrayAdapter extends ArrayAdapter<Earthquake> {

    public EarthquakeArrayAdapter(Context context, ArrayList<Earthquake> xyz)
    {
        super(context,0,xyz);
    }

    private int getMagnitudeColor(double mag)
    {
        if(mag <= 1 )
            return ContextCompat.getColor(getContext(),R.color.magnitude1);
        else if(mag <=2)
            return ContextCompat.getColor((getContext()),R.color.magnitude2);
        else if(mag <=3)
            return  ContextCompat.getColor(getContext(),R.color.magnitude3);
        else if(mag <=4)
            return ContextCompat.getColor(getContext(),R.color.magnitude4);
        else if(mag <= 5)
            return ContextCompat.getColor(getContext(),R.color.magnitude5);
        else if(mag <=6)
            return ContextCompat.getColor(getContext(),R.color.magnitude6);
        else if(mag <= 7)
            return ContextCompat.getColor(getContext(),R.color.magnitude7);
        else if (mag <= 8)
            return ContextCompat.getColor(getContext(),R.color.magnitude8);
        else if(mag <= 9)
            return ContextCompat.getColor(getContext(),R.color.magnitude9);
        else
            return ContextCompat.getColor(getContext(),R.color.magnitude10plus);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if(listItemView == null)
        {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_item,parent,false);
        }

        Earthquake current = getItem(position);

        TextView textView = (TextView) listItemView.findViewById(R.id.magnitude);
        GradientDrawable magnitudeCircle = (GradientDrawable) textView.getBackground();
        int magnitudeColor = getMagnitudeColor(current.getMag());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        textView.setText(Double.toString(current.getMag()));

        TextView textView2 = (TextView) listItemView.findViewById(R.id.description_primary);
        TextView textView22 = (TextView) listItemView.findViewById(R.id.description_offset);
        String desc = current.getDescription();

        if(desc.contains("km"))
        {
            String spiltt[] = desc.split("of ");
            textView2.setText(spiltt[1]);
            textView22.setText(spiltt[0] + " of,");
        }
        else
        {
            textView2.setText(current.getDescription());
            textView22.setText("Near the");
        }

        TextView textView3 = (TextView) listItemView.findViewById(R.id.date);
        textView3.setText(current.getDate());

        TextView textView4 = (TextView) listItemView.findViewById(R.id.time);
        textView4.setText(current.getTime());

        return  listItemView;
    }
}
