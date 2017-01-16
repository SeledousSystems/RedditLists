package com.pea.jay.redditlists.model;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.pea.jay.redditlists.R;

/**
 * Created by Paul Wright on 30/12/2016.
 */
public class BackgroundBuilder {

    public static int getColour(Context context, String string) {
        int color = ContextCompat.getColor(context, R.color.list_white);
        Log.d("Color String" , string);
        switch(string) {
            case "red":
                color = ContextCompat.getColor(context, R.color.list_red);
                break;
            case "blue":
                color = ContextCompat.getColor(context, R.color.list_blue);
                break;
            case "green":
                color = ContextCompat.getColor(context, R.color.list_green);
                break;
            case "grey":
                color = ContextCompat.getColor(context, R.color.list_grey);
                break;
            case "yellow":
                color = ContextCompat.getColor(context, R.color.list_yellow);
                break;
            default:
                break;
        }

        return color;
    }
}
