package com.pea.jay.redditlists.customViews;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pea.jay.redditlists.R;
import com.pea.jay.redditlists.model.Comment;
import com.pea.jay.redditlists.model.Link;
import com.pea.jay.redditlists.utilities.StringManager;
import com.pea.jay.redditlists.utilities.ItemTouchHelperAdapter;

import java.util.ArrayList;
import java.util.Collections;

public class CustomRecyclerListAdapter extends RecyclerView.Adapter<CustomRecyclerListViewHolder> implements ItemTouchHelperAdapter {
    private ArrayList<Comment> mDataset;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private Context context;
    private LinearLayout commentCellLL;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    // Provide a suitable constructor (depends on the kind of dataset)
    public CustomRecyclerListAdapter(Context context, ArrayList<Comment> myDataset, OnItemLongClickListener mOnItemLongClickListener, OnItemClickListener mOnItemClickListener) {
        this.mDataset = myDataset;
        this.context = context;
        this.mOnItemLongClickListener = mOnItemLongClickListener;
        this.mOnItemClickListener = mOnItemClickListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CustomRecyclerListViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        //context
        this.context = parent.getContext();

        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_list_comment, parent, false);

        // set the view's size, margins, paddings and layout parameters
        CustomRecyclerListViewHolder vh = new CustomRecyclerListViewHolder(v);
        return vh;
    }

    public ArrayList<Comment> getDataSet() {
        return mDataset;
    }

    public void update(ArrayList<Comment> data) {
        this.mDataset = data;
        notifyDataSetChanged();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mDataset, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mDataset, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    private void assignColor(String color) {
        Drawable colorBkGd;
        //set the tick from the arg
        switch (color) {
            case "white":
                colorBkGd = ContextCompat.getDrawable(context, R.drawable.card_white);
                break;
            case "grey":
                colorBkGd = ContextCompat.getDrawable(context, R.drawable.card_grey);
                break;
            case "yellow":
                colorBkGd = ContextCompat.getDrawable(context, R.drawable.card_yellow);
                break;
            case "green":
                colorBkGd = ContextCompat.getDrawable(context, R.drawable.card_green);
                break;
            case "blue":
                colorBkGd = ContextCompat.getDrawable(context, R.drawable.card_blue);
                break;
            case "red":
                colorBkGd = ContextCompat.getDrawable(context, R.drawable.card_red);
                break;
            default:
                colorBkGd = ContextCompat.getDrawable(context, R.drawable.card_white);
                break;
        }
        commentCellLL.setBackgroundDrawable(colorBkGd);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final CustomRecyclerListViewHolder holder, final int position) {

        Comment comment = null;
        comment = mDataset.get(position);

        commentCellLL = holder.commentCellLL;
        assignColor(comment.getColorString());

        final Drawable upArrow = ContextCompat.getDrawable(context, R.drawable.ic_keyboard_arrow_up_black_24dp);
        final Drawable downArrow = ContextCompat.getDrawable(context, R.drawable.ic_keyboard_arrow_down_black_24dp);

        final LinearLayout linkLL = holder.linkLL;
        linkLL.setVisibility(View.GONE);

        holder.commentTextTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(view, position);
            }
        });
        holder.commentTextTV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mOnItemLongClickListener.onLongClick(view, position);
                return true;
            }
        });

        holder.commentTextTV.setText(StringManager.noTrailingwhiteLines(Html.fromHtml(StringManager.generateHTMLCommentText(comment.getBody()))));
        holder.commentTextTV.setMovementMethod(LinkMovementMethod.getInstance());
        int linksNo = comment.getLinks().size();
        if (linksNo == 0) {
            holder.linkDivider.setVisibility(View.GONE);
            holder.linkHeaderLL.setVisibility(View.GONE);
            holder.linkLL.setVisibility(View.GONE);
        } else {
            holder.linkHeaderLL.setVisibility(View.VISIBLE);
            holder.linkHeaderLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (linkLL.getVisibility() == View.GONE) {
                        linkLL.setVisibility(View.VISIBLE);
                        holder.linkArrow.setImageDrawable(upArrow);
                    } else {
                        linkLL.setVisibility(View.GONE);
                        holder.linkArrow.setImageDrawable(downArrow);
                    }
                }
            });

            holder.linkDivider.setVisibility(View.VISIBLE);
            holder.linkHeaderLL.setVisibility(View.VISIBLE);
            holder.linkInfoTV.setVisibility(View.VISIBLE);
            if (linksNo == 1) {
                holder.linkInfoTV.setText("One link");
                String linkURL = comment.getLinks().get(0).getLinkURL();
                if (linkURL.contains("spoiler")) {
                    holder.linkInfoTV.setText("One Spoiler");
                }
            } else {
                holder.linkInfoTV.setText(comment.getLinks().size() + " links");

                for (Link link : comment.getLinks()) {
                    if (link.getLinkURL().contains("spoiler")) {
                        holder.linkInfoTV.setText(comment.getLinks().size() + " Links & Spoilers");
                    }
                }
            }

            LinearLayout gridLL = holder.linkLL;
            gridLL.removeAllViews();
            final LayoutInflater inflater = LayoutInflater.from(context);

            for (Link link : comment.getLinks()) {
                //create a view for the link
                View linkView = inflater.inflate(R.layout.cell_link, null, false);
                //get the textview for the link text and set the text
                final TextView linkTextTV = (TextView) linkView.findViewById(R.id.linkTextTV);

                linkTextTV.setText(StringManager.removeRedditFormating(link.getLinkText()));
                //get the textview for the link URL and set the URL

                final TextView linkURLTV = (TextView) linkView.findViewById(R.id.linkURLTV);

                final String fullLinkURL = link.getLinkURL();

                if (link.getLinkURL().contains("spoiler")) {
                    linkTextTV.setText("SPOILER - tap to reveal");
                    linkTextTV.setTextColor(ContextCompat.getColor(context, R.color.spoiler));

                    linkURLTV.setText(link.getLinkText());
                    linkURLTV.setTextColor(ContextCompat.getColor(context, R.color.primary_light));
                    linkURLTV.setVisibility(View.INVISIBLE);
                    linkURLTV.setTextColor(ContextCompat.getColor(context, R.color.primary_text));
                    //change view
                    linkView.setOnClickListener(new View.OnClickListener() {
                        boolean spoilerShown = false;
                        @Override
                        public void onClick(View view) {
                            if (!spoilerShown) {
                                spoilerShown = true;
                                linkURLTV.setVisibility(View.VISIBLE);
                            } else {
                                spoilerShown = false;
                                linkURLTV.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                } else {
                    linkTextTV.setText(StringManager.noTrailingwhiteLines(Html.fromHtml(StringManager.generateHTMLCommentText((link.getLinkText())))));
                    linkURLTV.setText(StringManager.removeURLstart(link.getLinkURL()));
                    if (link.getLinkText().equals("")) {
                        linkTextTV.setVisibility(View.GONE);
                    }
                    linkView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                String url = fullLinkURL.replace(" ", "");
                                i.setData(Uri.parse(url));
                                context.startActivity(i);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(context, "There appears to be something wrong with that link, sorry about that", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                //add the view to the LL view holder
                gridLL.addView(linkView);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onLongClick(View view, int position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}