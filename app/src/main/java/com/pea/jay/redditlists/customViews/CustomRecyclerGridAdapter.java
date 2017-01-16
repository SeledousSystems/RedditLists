package com.pea.jay.redditlists.customViews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pea.jay.redditlists.R;
import com.pea.jay.redditlists.model.BackgroundBuilder;
import com.pea.jay.redditlists.model.RedditList;
import com.pea.jay.redditlists.model.RedditPost;
import com.pea.jay.redditlists.utilities.ItemTouchHelperAdapter;

import java.util.ArrayList;
import java.util.Collections;


public class CustomRecyclerGridAdapter extends RecyclerView.Adapter<CustomRecyclerViewHolder> implements ItemTouchHelperAdapter {
    private ArrayList<RedditList> mDataset;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private Context context;

    // Provide a suitable constructor (depends on the kind of dataset)
    public CustomRecyclerGridAdapter(ArrayList<RedditList> myDataset, OnItemClickListener mOnItemClickListener, OnItemLongClickListener mOnItemLongClickListener) {
        this.mDataset = myDataset;
        this.mOnItemClickListener = mOnItemClickListener;
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    public ArrayList<RedditList> getmDataset() {
        return mDataset;
    }

    @Override
    public void onItemDismiss(int position) {
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

    // Create new views (invoked by the layout manager)
    @Override
    public CustomRecyclerViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        //context
        this.context = parent.getContext();

        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_grid, parent, false);

        // set the view's size, margins, paddings and layout parameters
        CustomRecyclerViewHolder vh = new CustomRecyclerViewHolder(v);
        return vh;
    }

    public void update(ArrayList<RedditList> data) {
        this.mDataset = data;
        notifyDataSetChanged();
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(CustomRecyclerViewHolder holder, final int position) {

        RedditList list = mDataset.get(position);
        RedditPost post = list.getPost();

        holder.title.setBackgroundColor(BackgroundBuilder.getColour(context, mDataset.get(position).getColorString()));
        holder.title.setText(mDataset.get(position).getPost().getTitle());
        holder.subReddit.setText("r/" + mDataset.get(position).getPost().getSubreddit());
        holder.date.setText(post.getCommentList().size() + " Comments");

        holder.gridLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(view, position);
            }
        });
        holder.gridLL.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mOnItemLongClickListener.onLongClick(view, position);
                return true;
            }
        });
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