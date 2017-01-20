package com.pea.jay.redditlists.customViews;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.pea.jay.redditlists.R;

import java.util.List;


/**
 *
 */
public class CustomGridViewAdaptor extends ArrayAdapter<String> {

    public CustomGridViewAdaptor(Context context, List<String> subreddits) {
        super(context, R.layout.subreddit_search_tag_layout, subreddits);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomGridView view = new CustomGridView(getContext());
        view.setSubreddit(getItem(position));
        return view;
    }
}

