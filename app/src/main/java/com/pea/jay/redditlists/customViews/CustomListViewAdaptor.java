package com.pea.jay.redditlists.customViews;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.pea.jay.redditlists.R;
import com.pea.jay.redditlists.model.RedditList;

import java.util.List;


/**
 *
 */
public class CustomListViewAdaptor extends ArrayAdapter<RedditList> {

    public CustomListViewAdaptor(Context context, List<RedditList> lists) {
        super(context, R.layout.main_cell_list, lists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomListView view = new CustomListView(getContext());
        view.setCard(getItem(position));
        return view;
    }
}

