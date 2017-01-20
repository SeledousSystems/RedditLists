package com.pea.jay.redditlists.userInterface;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pea.jay.redditlists.R;
import com.pea.jay.redditlists.customViews.CustomRecyclerListAdapter;
import com.pea.jay.redditlists.model.Comment;
import com.pea.jay.redditlists.model.Link;
import com.pea.jay.redditlists.model.PostFactory;
import com.pea.jay.redditlists.model.RedditList;
import com.pea.jay.redditlists.persistance.GlobalListHolder;
import com.pea.jay.redditlists.utilities.StringManager;

import java.io.InputStream;
import java.util.ArrayList;

public class RecyclerListActivity extends AppCompatActivity implements View.OnClickListener, ListButtonBarFragment.OnFragmentInteractionListener, ListColorBarFragment.OnFragmentInteractionListener {
    private Context context;
    private RedditList redditList;
    private TextView titleTV, subredditTV, infoTV, commentTextTV, linkInfoTV;
    private ImageView postThumbIV, dropDownIV;
    private RecyclerView mRecyclerView;
    private FragmentManager fm = getSupportFragmentManager();
    private CustomRecyclerListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<Comment> commentList;
    private ItemTouchHelper itemTouchHelper;
    private String TAG = "RecyclerListActivity";
    private ItemTouchHelper.Callback ithCallback;
    private Rect r = new Rect();
    private ArrayList<RedditList> listsAL = new ArrayList<>();
    private ViewGroup viewGroup;
    private LinearLayout titleLL, expandedLL;
    private Button regenCommentsButton, editPostTitleButton, openPostButton;
    private boolean postExpanded = false;
    private Drawable upArrow;
    private Drawable downArrow;
    private LinearLayout postLinkLL, linkHeaderLL;
    private View linkDivider;
    private int downloadImageTimeOut = 4000;
    private int regenCommentsTimeOut = 10000;
    private String text = "Deleting...";
    private Drawable childDrawBkgd;
    private Paint textPaint = new Paint();
    private boolean itemSelected;
    private Comment touchedComment;
    private boolean scrollResetMode = false;
    private ListButtonBarFragment bbFrag;
    private ListColorBarFragment cbFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_recycler_list);
        this.context = this;

        //get intent redditList
        redditList = (RedditList) getIntent().getSerializableExtra(MainActivity.INTENT_LIST_OBJ);

        listsAL = GlobalListHolder.getInstance(context).getMasterList();

        for (RedditList list : listsAL) {
            if (list.getCreated() == redditList.getCreated()) {
                listsAL.set(listsAL.indexOf(list), redditList);
            }
        }

        commentList = redditList.getPost().getCommentList();

        if (redditList != null) {
            setUpViews();
        } else {
            startActivity(new Intent(RecyclerListActivity.this, MainActivity.class));
        }

        titleLL.setOnClickListener(this);

        //set up the paint for child draw
        textPaint.setColor(Color.WHITE);
        textPaint.setStrokeWidth(18);
        final float textSize = 60f;
        textPaint.setTextSize(textSize);
        childDrawBkgd = ContextCompat.getDrawable(context, R.drawable.swipe_right_child);

        mAdapter = new CustomRecyclerListAdapter(context, commentList, new CustomRecyclerListAdapter.OnItemLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                if (itemSelected) {
                    showOptions(false);
                }
            }
        }, new CustomRecyclerListAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "onclikc recycler");
                if (itemSelected) {
                    showOptions(false);
                } else {
                    touchedComment = commentList.get(position);
                    viewGroup = (ViewGroup) view.getParent();
                    viewGroup.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.selected_bg), PorterDuff.Mode.DARKEN);
                    showOptions(true);
                }
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        // improves performance if you know changes in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(context);
        // callback for the touch events Extend the Callback class
        ithCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT) {
            boolean moving = false;

            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
                moving = true;


            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                mAdapter.onItemMove(viewHolder.getAdapterPosition(),
                        target.getAdapterPosition());

                if (viewGroup != null) {
                    viewGroup.getBackground().setColorFilter(null);
                }
                showOptions(false);
                return true;
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                if (dX > 0) {
                    childDrawBkgd.setBounds(itemView.getLeft(), itemView.getTop(), (int) dX, itemView.getBottom());
                    childDrawBkgd.draw(c);
                    int xPos = itemView.getLeft() + 200;
                    int yPos = (int) (((itemView.getBottom() - itemView.getTop()) / 2) + itemView.getTop() - ((textPaint.descent() + textPaint.ascent()) / 2));
                    c.drawText(text, xPos, yPos, textPaint);
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);

                if (itemSelected) {
                    showOptions(false);
                }

                updateUI();
            }
        };

        // Create an `ItemTouchHelper` and attach it to the `RecyclerView`
        itemTouchHelper = new ItemTouchHelper(ithCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //set the background back to normal
                if (scrollResetMode) {
                    if (viewGroup != null) viewGroup.getBackground().setColorFilter(null);
                    if (itemSelected) showOptions(false);
                    if (postExpanded) handleTitleMode();
                    scrollResetMode = false;
                }
            }
        });

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mLayoutManager);

        addLinksToPostExpanded();
        postExpanded = true;
        handleTitleMode();
        showOptions(false);
        updateUI();
    }

    @Override
    protected void onPause() {
        GlobalListHolder.getInstance(context).storeMasterList();
        super.onPause();
    }


    /**
     * method to show toasts. used for fragments
     *
     * @param msg
     */
    public void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public void showOptions(boolean show) {
        Log.d(TAG, "showoptions " + show);
        itemSelected = show;
        if (show) {
            bbFrag.updateData(touchedComment);
            cbFrag.updateData(touchedComment);
            bbFrag.getView().setVisibility(View.VISIBLE);
            cbFrag.getView().setVisibility(View.VISIBLE);

        } else {
            //set the background back to normal
            if (viewGroup != null) viewGroup.getBackground().setColorFilter(null);
            //hide the buttonbar and colorbar fragments
            bbFrag.getView().setVisibility(View.GONE);
            cbFrag.getView().setVisibility(View.GONE);
        }

    }

    public void deleteComment(Comment comment) {
        commentList.remove(comment);
        updateUI();
    }

    private void addLinksToPostExpanded() {
        if (redditList.getPost().getEmbeddedLinks() == null) {
            redditList.getPost().setEmbeddedLinks(new ArrayList<Link>());
        }
        int linksNo = redditList.getPost().getEmbeddedLinks().size();
        if (linksNo == 0) {
            linkDivider.setVisibility(View.GONE);
            linkHeaderLL.setVisibility(View.GONE);
            postLinkLL.setVisibility(View.GONE);
        } else {
            linkHeaderLL.setVisibility(View.VISIBLE);
            linkDivider.setVisibility(View.VISIBLE);
            linkHeaderLL.setVisibility(View.VISIBLE);
            linkHeaderLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (postLinkLL.getVisibility() == View.GONE) {
                        postLinkLL.setVisibility(View.VISIBLE);
                    } else postLinkLL.setVisibility(View.GONE);
                }
            });
            postLinkLL.setVisibility(View.GONE);
            linkInfoTV.setVisibility(View.VISIBLE);
            if (linksNo == 1) {
                linkInfoTV.setText("One link");
                String linkURL = redditList.getPost().getEmbeddedLinks().get(0).getLinkURL();
                if (linkURL.contains("spoiler")) {
                    linkInfoTV.setText("One Spoiler");
                }
            } else {
                linkInfoTV.setText(linksNo + " links");
                for (Link link : redditList.getPost().getEmbeddedLinks()) {
                    if (link.getLinkURL().contains("spoiler")) {
                        linkInfoTV.setText(linksNo + " Links & Spoilers");
                    }
                }
            }

            if (redditList.getPost().getEmbeddedLinks().size() > 0) {
                final LayoutInflater inflater = LayoutInflater.from(context);

                for (Link link : redditList.getPost().getEmbeddedLinks()) {
                    //create a view for the link
                    View linkView = inflater.inflate(R.layout.cell_link, null, false);
                    //get the textview for the link text and set the text
                    final TextView linkTextTV = (TextView) linkView.findViewById(R.id.linkTextTV);
                    //linkTextTV.setText(StringManager.removeRedditFormating(link.getLinkText()));
                    //get the textview for the link URL and set the URL
                    final TextView linkURLTV = (TextView) linkView.findViewById(R.id.linkURLTV);
                    final String fullLinkURL = link.getLinkURL();
                    if (link.getLinkURL().contains("spoiler")) {
                        linkTextTV.setText("SPOILER - tap to reveal");
                        linkTextTV.setTextColor(ContextCompat.getColor(context, R.color.spoiler));
                        linkURLTV.setText(link.getLinkText());
                        linkURLTV.setVisibility(View.INVISIBLE);
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
                        linkTextTV.setText(StringManager.removeRedditFormating(link.getLinkText()));
                        linkURLTV.setText(StringManager.removeURLstart(link.getLinkURL()));
                        linkView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(fullLinkURL));
                                context.startActivity(i);
                            }
                        });
                    }
                    postLinkLL.addView(linkView);
                }

            }
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.postThumbIV:
                if (!isNetworkConnected()) {
                    Toast.makeText(context, "You must be connected to the internet to open Reddit", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(redditList.getRawURL()));
                        context.startActivity(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "There was an error opening the post link, please check your internet connection and retry.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.editPostTitleButton:
                handleTitleMode();
                launchEditTitleDialog();
                break;
            case R.id.regenCommentsButton:
                handleTitleMode();
                if (!isNetworkConnected()) {
                    Toast.makeText(context, "You must be connected to the internet to regenerate comments", Toast.LENGTH_LONG).show();
                } else {
                    final RegenerateCommentsFromURL regTask = new RegenerateCommentsFromURL();
                    regTask.execute();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (regTask.getStatus() == AsyncTask.Status.RUNNING) {
                                regTask.cancel(true);
                                Toast.makeText(context, "There was an error regenerating comments, please check your internet connection and retry.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, regenCommentsTimeOut);
                }
                break;
            case R.id.openPostButton:
                handleTitleMode();
                if (!isNetworkConnected()) {
                    Toast.makeText(context, "You must be connected to the internet to open Reddit", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        String postURL = redditList.getPost().getUrl();
                        i.setData(Uri.parse(postURL));
                        context.startActivity(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "There was an error opening Reddit. Please check your internet connection and try again.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.titleLL:
                handleTitleMode();
                break;
            default:
                break;
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private void handleTitleMode() {
        postExpanded = !postExpanded;
        if (postExpanded) {
            expandedLL.setVisibility(View.VISIBLE);
            dropDownIV.setImageDrawable(upArrow);
            mRecyclerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleTitleMode();
                }
            });
        } else {
            expandedLL.setVisibility(View.GONE);
            dropDownIV.setImageDrawable(downArrow);
            mRecyclerView.setOnClickListener(null);
        }
    }

    @Override
    public void onBackPressed() {
        if (postExpanded) {
            handleTitleMode();
        } else if (itemSelected) {
            showOptions(false);
        } else {
            super.onBackPressed();
        }
    }

    private void launchEditTitleDialog() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Edit Post Title");
        final EditText input = new EditText(context);
        input.setText(redditList.getPost().getTitle());
        builder.setPositiveButton("Save Post Title", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int arg1) {
                String newTitle = input.getText().toString();
                if (newTitle != "" || newTitle != null) {
                    redditList.getPost().setTitle(newTitle);
                    updateUI();
                    Toast.makeText(context, "Post Title Updated.", Toast.LENGTH_SHORT).show();
                }
                d.cancel();
            }
        });
        builder.setView(input);
        builder.setNegativeButton("Close", null);
        builder.show();
    }

    private void setUpViews() {
        titleLL = (LinearLayout) findViewById(R.id.titleLL);
        titleTV = (TextView) findViewById(R.id.postTitleTV);

        postThumbIV = (ImageView) findViewById(R.id.postThumbIV);
        postThumbIV.setVisibility(View.GONE);
        if (redditList.getPost().getThumbNail().equals("")) ;
        else {
            final DownloadImageTask diTask = new DownloadImageTask(postThumbIV);
            diTask.execute(redditList.getPost().getThumbNail());
            postThumbIV.setOnClickListener(this);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (diTask.getStatus() == AsyncTask.Status.RUNNING) {
                        diTask.cancel(true);
                        postThumbIV.setVisibility(View.GONE);
                    }
                }
            }, downloadImageTimeOut);
        }
        upArrow = ContextCompat.getDrawable(context, R.drawable.ic_keyboard_arrow_up_black_24dp);
        downArrow = ContextCompat.getDrawable(context, R.drawable.ic_keyboard_arrow_down_black_24dp);
        subredditTV = (TextView) findViewById(R.id.subredditTV);
        subredditTV.setText("r/" + redditList.getPost().getSubreddit());
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        infoTV = (TextView) findViewById(R.id.infoTV);
        dropDownIV = (ImageView) findViewById(R.id.dropDownIV);

        expandedLL = (LinearLayout) findViewById(R.id.expandedLL);

        regenCommentsButton = (Button) findViewById(R.id.regenCommentsButton);
        regenCommentsButton.setOnClickListener(this);
        editPostTitleButton = (Button) findViewById(R.id.editPostTitleButton);
        editPostTitleButton.setOnClickListener(this);
        openPostButton = (Button) findViewById(R.id.openPostButton);
        openPostButton.setOnClickListener(this);
        commentTextTV = (TextView) findViewById(R.id.commentTextTV);
        commentTextTV.setMovementMethod(new ScrollingMovementMethod());
        commentTextTV.setText(StringManager.noTrailingwhiteLines(Html.fromHtml(StringManager.generateHTMLCommentText(redditList.getPost().getSelfText()))));
        linkHeaderLL = (LinearLayout) findViewById(R.id.linkHeaderLL);
        if (redditList.getPost().getSelfText().equals("")) {
            commentTextTV.setGravity(Gravity.CENTER);
            commentTextTV.setText("~ No Post Comments or Links ~");
            linkHeaderLL.setVisibility(View.GONE);
        }
        postLinkLL = (LinearLayout) findViewById(R.id.postLinkLL);

        linkDivider = findViewById(R.id.linkDivider);
        linkInfoTV = (TextView) findViewById(R.id.linkInfoTV);

        bbFrag = (ListButtonBarFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_button_bar_list);
        cbFrag = (ListColorBarFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_color_bar_list);
    }

    public void updateUI() {
        mAdapter.notifyDataSetChanged();
        infoTV.setText(commentList.size() + " comments");
        titleTV.setText(redditList.getPost().getTitle());
    }

    /**
     * Fragment Listener Method for passing data between the Activity and the button bar fragment
     */
    @Override
    public void onListButtonBarFragmentInteraction(Uri uri) {
        ListButtonBarFragment listButtonBarFragment = (ListButtonBarFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_button_bar_list);
    }

    /**
     * Fragment Listener Method for passing data between the Activity and the color bar fragment
     */
    @Override
    public void onListColorBarFragmentInteraction(Uri uri) {
        ListColorBarFragment listColorBarFragment = (ListColorBarFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_color_bar_list);
    }

    private class RegenerateCommentsFromURL extends AsyncTask<Void, Void, ArrayList<Comment>> {

        protected ArrayList<Comment> doInBackground(Void... redditLists) {
            //todo catch the connection exceptions or test for internet
            //generate fresh comments
            ArrayList<Comment> freshCommentsList = new PostFactory().rebuildCommentListFromJsonString(redditList.getJsonURL());
            return freshCommentsList;
        }

        protected void onProgressUpdate(Void... progress) {
            //progressBar.setProgress(progress[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(context, "Refreshing Comments....", Toast.LENGTH_LONG).show();
        }

        protected void onPostExecute(ArrayList<Comment> freshCommentList) {
            commentList.clear();
            commentList.addAll(freshCommentList);
            updateUI();
            Toast.makeText(context, "Comments Refreshed.", Toast.LENGTH_LONG).show();
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            Bitmap mIcon11 = null;
            try {
                String urldisplay = urls[0];
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
                return null;
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            if (result == null) postThumbIV.setVisibility(View.GONE);
            else {
                postThumbIV.setVisibility(View.VISIBLE);
                postThumbIV.setImageBitmap(result);
            }
        }
    }
}
