package com.pea.jay.redditlists.customViews;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pea.jay.redditlists.R;

public class CustomRecyclerViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView subReddit;
    public TextView date;
    public LinearLayout gridLL;

    public CustomRecyclerViewHolder(View itemLayoutView) {
        super(itemLayoutView);
        title = (TextView) itemLayoutView.findViewById(R.id.listTitleTV);
        subReddit = (TextView) itemLayoutView.findViewById(R.id.subredditTV);
        date = (TextView) itemLayoutView.findViewById(R.id.dateTV);
        gridLL = (LinearLayout) itemLayoutView.findViewById(R.id.gridCellLL);
    }
}