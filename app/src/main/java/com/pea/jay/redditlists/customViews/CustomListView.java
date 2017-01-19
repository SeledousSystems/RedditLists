package com.pea.jay.redditlists.customViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pea.jay.redditlists.R;
import com.pea.jay.redditlists.model.RedditList;

/**
 * Class that defines the Custom Card view for each card in the list view
 */
public class CustomListView extends LinearLayout {

    protected TextView postTitle, subreddit, comments;

    /**
     * Constructor for the CustomCardView which defines the view.
     */
    public CustomListView(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.main_cell_list, this, true);
        postTitle = (TextView) findViewById(R.id.listTitleTV);
        subreddit = (TextView) findViewById(R.id.subredditTV);
        comments = (TextView) findViewById(R.id.commentsTV);
    }

    /**
     * setCard method to assign the elements of the card.
     *
     * @param list
     */
    public void setCard(RedditList list) {

        //set the content for Text and Image view of the card
        postTitle.setText(list.getPost().getTitle());
        subreddit.setText(list.getPost().getSubreddit());
        comments.setText(list.getPost().getCommentList().size() + " Comments");
    }
}

