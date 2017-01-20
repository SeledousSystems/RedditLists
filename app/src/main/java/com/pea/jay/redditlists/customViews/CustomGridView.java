package com.pea.jay.redditlists.customViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pea.jay.redditlists.R;

/**
 * Class that defines the Custom Card view for each card in the list view
 */
public class CustomGridView extends LinearLayout {

    protected TextView subredditTV;

    public CustomGridView(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.subreddit_search_tag_layout, this, true);
        subredditTV = (TextView) findViewById(R.id.subbredditTagTV);
    }

    public void setSubreddit(String s) {
        subredditTV.setText("r/" + s);
    }
}

