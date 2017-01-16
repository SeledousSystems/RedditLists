package com.pea.jay.redditlists.customViews;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pea.jay.redditlists.R;

public class CustomRecyclerListViewHolder extends RecyclerView.ViewHolder {

    public TextView commentTextTV;
    public TextView linkInfoTV;
    public LinearLayout commentCellLL;
    public LinearLayout linkLL, linkHeaderLL;
    public View linkDivider;
    public ImageView linkArrow;

    public CustomRecyclerListViewHolder(View itemLayoutView) {
        super(itemLayoutView);
        commentCellLL = (LinearLayout) itemLayoutView.findViewById(R.id.commentCellLL);
        commentTextTV = (TextView) itemLayoutView.findViewById(R.id.commentTextTV);
        linkInfoTV = (TextView) itemLayoutView.findViewById(R.id.linkInfoTV);
        linkLL = (LinearLayout) itemLayoutView.findViewById(R.id.linkLL);
        linkHeaderLL =  (LinearLayout) itemLayoutView.findViewById(R.id.linkHeaderLL);
        linkDivider = itemLayoutView.findViewById(R.id.linkDivider);
        linkArrow = (ImageView) itemLayoutView.findViewById(R.id.linkArrow);
    }
}