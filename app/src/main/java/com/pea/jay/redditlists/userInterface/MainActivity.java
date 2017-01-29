package com.pea.jay.redditlists.userInterface;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.pea.jay.redditlists.R;
import com.pea.jay.redditlists.customViews.CustomRecyclerGridAdapter;
import com.pea.jay.redditlists.model.RedditList;
import com.pea.jay.redditlists.model.RedditListFactory;
import com.pea.jay.redditlists.persistance.GlobalListHolder;
import com.pea.jay.redditlists.persistance.SharedPrefManager;
import com.pea.jay.redditlists.utilities.DialogManager;
import com.pea.jay.redditlists.utilities.StringManager;

import java.util.ArrayList;
import java.util.Collections;

import static android.os.AsyncTask.SERIAL_EXECUTOR;

public class MainActivity extends AppCompatActivity implements GridButtonBarFragment.OnFragmentInteractionListener, GridColorBarFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener, PopupMenu.OnMenuItemClickListener {   //SearchViewFragment.OnFragmentInteractionListener,

    public static String INTENT_LIST_OBJ = "list_obj";
    public static String pastedURL = "Pasted_URL_Intent";
    SearchViewFragment svFrag;
    private RedditList touchedList;
    private ArrayList<RedditList> listAL = new ArrayList<>();
    private Context context;
    private String rawURL = "";
    private String TAG = "Main_Activity";
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mGridLayoutmanager;
    private CustomRecyclerGridAdapter mAdapter;
    private RedditList redditList;
    private ProgressBar progressBar;
    private boolean itemSelected = false;
    private ItemTouchHelper ith;
    private LinearLayout buttonLL;
    private Button redditButton, linkButton;
    private ActionBarDrawerToggle drawerToggle;
    private boolean showNSFW = false;
    private boolean randomColors = true;
    private boolean mainIntialBoot = true;
    private int demoNumber = 8;
    private MainActivity main;
    private int genListTimeOut = 8000;
    private String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqWiOisEa46xRJoVvczCrst/kYKMOBKtO2oY38RU1v+aWK5rfNOQaSzQl+RwHkX8CFFnAmexykOvtks0he/+jj+doWmMCdKw4++mdc6lJOmaZgOWm2cZ4L2f99HDM/mYD/MHkKnfP4dBDy4BfKSgK1ujwQPrnxrFrLjfDMgkwjYGgetqN751DhGJaEiewIcY67eN0ASvhT3qxKEUhTiOcF5P6I5jRgZmsyCwwhyZb5xzVFXI6Ojq4ggVLYJlLNR0iy0KlIvwmQImdZS7s5IvyaOpKcagScGnT//cS0nnMuSEH1ySrF0q0kySfarImn8LM0GSkBSVlRAKb76fkGXKXHwIDAQAB";
    private View selectedView;
    private SharedPrefManager spm;
    private NavigationView navigationView;
    private boolean asynchRunning = false;
    private LinearLayout mainLL;
    private boolean searchMode = false;
    private SearchView searchView;
    private SearchViewFragment searchViewFragment;
    private GridColorBarFragment gridColorBarFragment;
    private GridButtonBarFragment gridButtonBarFragment;
    private CustomRecyclerGridAdapter.OnItemLongClickListener gridOnItemLongClickListener;
    private CustomRecyclerGridAdapter.OnItemClickListener gridOnItemClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set the context and object for passing to other classes to be able to call this instances public methods
        context = this;
        main = this;

        //set the action bar
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));

        //spm for save preferences for intial boot and NSFW mode
        spm = new SharedPrefManager(context);
        mainIntialBoot = spm.isInitialBootMain();
        showNSFW = spm.isShowNSFW();
        randomColors = spm.isRandomColors();
        demoNumber = spm.getDemoNumber();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (demoNumber > 7) {
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_add_demo_lists).setVisible(false);
        }

        //add slideout menu icon to action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //setup the slide out nav drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        MenuItem switchItemNSFW = navigationView.getMenu().findItem(R.id.nav_nsfw);
        CompoundButton switchViewNSFW = (CompoundButton) MenuItemCompat.getActionView(switchItemNSFW);
        switchViewNSFW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                spm.saveShowNSFW(isChecked);
                showNSFW = isChecked;
            }
        });
        switchViewNSFW.setChecked(showNSFW);

        MenuItem switchItemColors = navigationView.getMenu().findItem(R.id.nav_random_colors);
        CompoundButton switchViewColors = (CompoundButton) MenuItemCompat.getActionView(switchItemColors);
        switchViewColors.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                spm.saveRandomColor(isChecked);
                randomColors = isChecked;
            }
        });
        switchViewColors.setChecked(randomColors);

        //set up the stored data for the arrayLists and get the array lists
        listAL = GlobalListHolder.getInstance(context).getMasterList();

        //set up progress bar
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.primary)
                , android.graphics.PorterDuff.Mode.SRC_IN);
        progressBar.setVisibility(View.GONE);

        //set up the recyler view, adapter, layout manager and spacing decorator
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        gridOnItemClickListener = new CustomRecyclerGridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "onclick");
                if (itemSelected) {
                    if (selectedView != null) selectedView.setSelected(false);
                    handleItemSelected(false);
                } else {
                    Intent listIntent;
                    RedditList list = listAL.get(position);
                    listIntent = new Intent(MainActivity.this, RecyclerListActivity.class);
                    listIntent.putExtra(INTENT_LIST_OBJ, list);
                    startActivity(listIntent);
                }
            }
        };

        gridOnItemLongClickListener = new CustomRecyclerGridAdapter.OnItemLongClickListener() {

            @Override
            public void onLongClick(View view, int position) {
                Log.d(TAG, " logclick itemselected " + itemSelected);
                if (!itemSelected) {
                    touchedList = listAL.get(position);
                    selectedView = view;
                    view.setSelected(true);
                    handleItemSelected(true);
                } else {
                    handleItemSelected(false);
                    selectedView.setSelected(false);
                }
            }
        };

        mAdapter = new CustomRecyclerGridAdapter(listAL, gridOnItemClickListener, gridOnItemLongClickListener);
        mAdapter.hasStableIds();
        mRecyclerView.setAdapter(mAdapter);

        // use a grid Layout manager, assign a 2 width grid for portrait, 3 for landscape
        Configuration configuration = getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mGridLayoutmanager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        } else {
            mGridLayoutmanager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        }
        mRecyclerView.setLayoutManager(mGridLayoutmanager);

        // Item touch helper for managing gestures and list changes
        ItemTouchHelper.Callback _ithCallback = new ItemTouchHelper.Callback() {
            boolean moving = false;

            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                moving = true;
                Log.d(TAG, "onMOve");
                selectedView = viewHolder.itemView;
                viewHolder.itemView.setSelected(true);
                mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
            }

            //handle the dropped item
            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                if (moving) {
                    Log.d(TAG, " clearView");
                    moving = false;
                    if (selectedView != null) selectedView.setSelected(false);
                    handleItemSelected(false);
                    moving = false;
                }
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            }

            //defines the enabled move directions in each state (idle, swiping, dragging).
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                        ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.START | ItemTouchHelper.END);
            }
        };

        ith = new ItemTouchHelper(_ithCallback);
        ith.attachToRecyclerView(mRecyclerView);

        //set up buttons
        buttonLL = (LinearLayout) findViewById(R.id.main_buttonLL);
        redditButton = (Button) findViewById(R.id.redditButton);
        redditButton.setOnClickListener(new View.OnClickListener()

                                        {
                                            @Override
                                            public void onClick(View view) {
                                                //TODO dialog for notifying user launch happening, checkbox to not show again
                                                if (!isNetworkConnected()) {
                                                    Toast.makeText(context, main.getResources().getString(R.string.toast_no_internet), Toast.LENGTH_LONG).show();
                                                } else {
                                                    if (!asynchRunning)
                                                        DialogManager.launchRedditApp(context, main);
                                                }
                                            }
                                        }
        );
        linkButton = (Button)

                findViewById(R.id.linkButton);
        linkButton.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              DialogManager.showPasteLinkDialog(context, main);
                                          }
                                      }
        );

        // Get intent of shared links, check if intent exists validity
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        Log.d(TAG, " intent string " + rawURL);
        if (extras != null) {
            if (extras.containsKey(IntentHandlerActivity.Intent_key_URL)) {
                rawURL = intent.getStringExtra(IntentHandlerActivity.Intent_key_URL);
                intent.removeExtra(IntentHandlerActivity.Intent_key_URL);
                setIntent(null);
                if (extras.containsKey(IntentHandlerActivity.Intent_key_URL))
                    Log.d(TAG, intent.getStringExtra(IntentHandlerActivity.Intent_key_URL) + "  intent here");
                if (!isNetworkConnected()) {
                    Toast.makeText(context, context.getString(R.string.toast_no_internet), Toast.LENGTH_LONG).show();
                } else if (rawURL == null) {
                    showToast(context.getString(R.string.toast_bad_link));
                } else {
                    if (StringManager.checkStringIsRedditComment(rawURL)) {
                        generateList(rawURL);
                    } else {
                        Toast.makeText(context, context.getString(R.string.toast_bad_link), Toast.LENGTH_LONG).show();
                    }
                }
            }

        }

        // Get the intent, verify the action and get the query
        Intent searchIntent = getIntent();

        if (searchIntent != null && Intent.ACTION_SEARCH.equals(searchIntent.getAction())) {
            String query = searchIntent.getStringExtra(SearchManager.QUERY);
            Log.d(TAG, "searching" + query);
        }


        //initial boot logic for showing coachOverlay and demo lists
        if (mainIntialBoot) {
            addDemoLists();
            onCoachMark();
            spm.saveInitialBootMain(false);
        }
        gridButtonBarFragment = (GridButtonBarFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_button_bar_grid);
        gridColorBarFragment = (GridColorBarFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_color_bar_grid);
        //svFrag = (SearchViewFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_search_view);
        //svFrag.getView().setVisibility(View.GONE);
        //final calls to ensure main activity is in its intial state
        //searchView = (SearchView) findViewById(R.id.searchView);
        handleItemSelected(false);
        updateUI();
    }

    @Override
    protected void onStop() {
        GlobalListHolder.getInstance(context).storeMasterList();
        super.onStop();
    }

    /**
     * check network is connected
     *
     * @return
     */
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    /**
     * public method to add lists and refresh the UI
     *
     * @param redditList
     */
    public void addList(RedditList redditList) {
        listAL.add(0, redditList);
        mAdapter.update(listAL);
        Toast.makeText(context, "Reddit Post added to your lists.", Toast.LENGTH_SHORT).show();
    }

    /**
     * method to add demo lists on first boot
     */
    private void addDemoLists() {
        generateList(getResources().getString(R.string.demo1));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                generateList(getResources().getString(R.string.demo2));
            }
        }, 6000);
    }

    private void loadExamples() {
        final String[] array = new String[]{
                "https://www.reddit.com/r/gaming/comments/2aib66/the_best_game_you_can_finish_in_less_than_a_day/",
                "https://www.reddit.com/r/AskReddit/comments/xg7zj/someone_stole_my_credit_card_and_bought_and_ipad/",
                "https://www.reddit.com/r/AskReddit/comments/1k8tm3/whats_the_best_long_con_you_ever_pulled/",
                "https://www.reddit.com/r/androidapps/comments/4vj8fo/best_apps_of_2016_so_far/",
                "https://www.reddit.com/r/Fitness/comments/5nhaoi/tensed_shoulders_try_these_few_stretches_very/",
                "https://www.reddit.com/r/AskReddit/comments/4eg63g/what_song_started_you_into_your_favorite_genre_of/",
                "https://www.reddit.com/r/AskReddit/comments/3z7oi1/what_are_some_must_watch_documentaries/",
                "https://www.reddit.com/r/AskReddit/comments/3zjfym/which_youtube_channel_do_you_watch/"
        };
        if (!isNetworkConnected()) {
            Toast.makeText(context, getResources().getString(R.string.toast_no_internet), Toast.LENGTH_LONG).show();
        } else {
            if (demoNumber < array.length) {
                generateList(array[demoNumber]);
                demoNumber++;
                spm.saveDemoNumber(demoNumber);
            }
            if (demoNumber == array.length) {
                Menu nav_Menu = navigationView.getMenu();
                nav_Menu.findItem(R.id.nav_add_demo_lists).setVisible(false);
            }
        }
    }

    /**
     * generate list method from a URL. Times out after 8 secs
     *
     * @param url
     */
    public void generateList(String url) {
        redditList = new RedditList();
        redditList.setRawURL(url);
        //redditList.setColor(R.color.list_white);
        if (randomColors) {
            redditList.setColorString(StringManager.getRandomString(context));
        } else {
            redditList.setColorString("white");
        }
        final GenerateListFromURL genListTask = new GenerateListFromURL();
        genListTask.executeOnExecutor(SERIAL_EXECUTOR, redditList);
        // genListTask.execute(redditList);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (genListTask.getStatus() == AsyncTask.Status.RUNNING) {
                    genListTask.cancel(true);
                    Toast.makeText(context, "There was an error generating your List. Please check your internet connection and retry.", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        }, genListTimeOut);
    }

    /**
     * handle restart of activity
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        listAL = GlobalListHolder.getInstance(context).getMasterList();
        updateUI();
    }

    /**
     * handle resume of activity
     */
    @Override
    protected void onResume() {
        super.onResume();
        listAL = GlobalListHolder.getInstance(context).getMasterList();
        updateUI();
    }

    /**
     * Fragment Listener Method for passing data between the Activity and the button bar fragment
     */
    @Override
    public void onGridButtonBarFragmentInteraction(Uri uri) {
        gridButtonBarFragment = (GridButtonBarFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_button_bar);
    }

    /**
     * Fragment Listener Method for passing data between the Activity and the color bar fragment
     */
    @Override
    public void onGridColorBarFragmentInteraction(Uri uri) {
        gridColorBarFragment = (GridColorBarFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_color_bar);
    }

//    @Override
//    public void onSearchViewFragmentInteraction(Uri uri) {
//        searchViewFragment = (SearchViewFragment) getSupportFragmentManager().findFragmentById(R.id.action_search);
//    }

    private void onCoachMark() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.coach_mark);
        dialog.setCanceledOnTouchOutside(true);
        //for dismissing anywhere you touch
        View masterView = dialog.findViewById(R.id.coach_mark_master_view);
        masterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * handle the show options for when a list is long pressed
     *
     * @param selected
     */
    public void handleItemSelected(boolean selected) {

        Log.d(TAG, " handle method itemselected " + itemSelected);
        if (selected) {
            //hide the normal buttons
            buttonLL.setVisibility(View.GONE);
            //update the fragments
            gridButtonBarFragment.updateData(touchedList);
            gridColorBarFragment.updateData(touchedList);
            //show the buttonbar and colorbar fragments
            gridButtonBarFragment.getView().setVisibility(View.VISIBLE);
            gridColorBarFragment.getView().setVisibility(View.VISIBLE);
        } else {
            //hide the buttonbar and colorbar fragments
            gridButtonBarFragment.getView().setVisibility(View.GONE);
            gridColorBarFragment.getView().setVisibility(View.GONE);
            //show normal buttons
            buttonLL.setVisibility(View.VISIBLE);
            //remove the selected items
            if (selectedView != null) selectedView.setSelected(false);
            mAdapter.notifyDataSetChanged();
        }
        //mAdapter.notifyDataSetChanged();
        itemSelected = selected;
    }

    //to be implemented in the future
    private void handleSearchMode(boolean setSearchMode) {
        searchMode = setSearchMode;
        if (searchMode) {
            searchView.setVisibility(View.VISIBLE);
        } else {
            searchView.setVisibility(View.GONE);
        }
    }

    /**
     * delete list method
     *
     * @param list
     */
    public void deleteList(final RedditList list) {
        listAL.remove(list);
        updateUI();
    }

    /**
     * Inflate the menu; this adds items to the action bar if it is present.
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //MenuItem searchItem = menu.findItem(R.id.action_search);
        //SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        //final MenuItem miSearch = menu.findItem(R.id.action_search);
        //SearchView searchView = (SearchView) MenuItemCompat.getActionView(miSearch);
//        searchView.setQueryHint("Searh For List");

//        MenuItemCompat.setOnActionExpandListener(miSearch, new MenuItemCompat.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) {
//                svFrag.getView().setVisibility(View.GONE);
//
//                return true;  // Return true to collapse action view
//            }
//
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem item) {
//                svFrag.getView().setVisibility(View.VISIBLE);
//                return true;
//            }
//        });
//
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Toast.makeText(context, query, Toast.LENGTH_LONG).show();
//                miSearch.collapseActionView();
//                searchViewFragment.getView().setVisibility(View.GONE);
//                searchViewFragment.searchByString(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//
//                if (newText.equals("")) {
//                    searchViewFragment.getView().setVisibility(View.VISIBLE);
//
//                } else {
//                    searchViewFragment.searchByString(newText);
//                }
//
//
//                return false;
//            }
//        });


        return true;
    }

    /**
     * Handle navigation view item clicks.
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_info:
                DialogManager.showInfoDialog(context);
                break;
            case R.id.nav_rate:
                DialogManager.rate(context, this);
                break;
            case R.id.nav_how:
                DialogManager.showHowDialog(context);
                break;
            case R.id.nav_add_demo_lists:
                loadExamples();
                break;
            case R.id.nav_version:
                DialogManager.showVersionDialog(context);
                break;
            case R.id.nav_change_log:
                DialogManager.showChangeLog(context, this);
                break;
            default:
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * handle nav bar open and then require a double back button press to exit
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (itemSelected) {
            handleItemSelected(false);
        } else {
            super.onBackPressed();
            GlobalListHolder.getInstance(context).storeMasterList();
            finish();
            return;
        }
    }

    /**
     * method to update the adapter data for the recycler list
     */
    private void updateUI() {
        mAdapter.notifyDataSetChanged();
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
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * onOptions for the icon options in the menu bar
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        switch (id) {
//            case R.id.action_search:
//                Log.d(TAG, " Onclick searchview");
//                break;
            case R.id.action_sort:
                showSortPopup(findViewById(R.id.action_sort));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * search popup menu
     */
    public void showSortPopup(View v) {
        PopupMenu popup = new PopupMenu(context, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_sort, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }

    public void setDataList(ArrayList<RedditList> list) {
        mAdapter.setMDataset(list);
    }

    public void restoreDataList() {
        mAdapter.setMDataset(listAL);
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.sort_date_new:
                Collections.sort(listAL, new RedditList.DateForwardComparator());
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.sort_date_old:
                Collections.sort(listAL, new RedditList.DateBackwardComparator());
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.sort_title_a_z:
                Collections.sort(listAL, new RedditList.TitleForwardComparator());
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.sort_title_z_a:
                Collections.sort(listAL, new RedditList.TitleBackwardComparator());
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.sort_subreddit_a_z:
                Collections.sort(listAL, new RedditList.SubredditForwardComparator());
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.sort_subreddit_z_a:
                Collections.sort(listAL, new RedditList.SubredditBackwardComparator());
                mAdapter.notifyDataSetChanged();
            case R.id.sort_color:
                Collections.sort(listAL, new RedditList.ColorComparator());
                mAdapter.notifyDataSetChanged();

                break;
        }

        return false;
    }

    /**
     * private async class for fetching lists from Reddit via the factory methods
     */
    private class GenerateListFromURL extends AsyncTask<RedditList, Integer, RedditList> {

        //broadcast receiver receives updates for the progress bar
        private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            }
        };

        protected RedditList doInBackground(RedditList... redditLists) {
            Log.d(TAG, " do in background - execute");
            RedditList rl;
            try {
                rl = new RedditListFactory().getListObject(context, redditLists[0]);
            } catch (Exception e) {
                e.printStackTrace();
                rl = null;
            }
            return rl;
        }

        protected void onProgressUpdate(Integer... progress) {
            progressBar.setProgress(progress[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            asynchRunning = true;
            if (!isNetworkConnected()) {
                Toast.makeText(context, getResources().getString(R.string.toast_no_internet), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Fetching List...", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.VISIBLE);
            }
        }

        protected void onPostExecute(RedditList list) {
            Log.d(TAG, " post - execute");
            if (list == null) {
                Toast.makeText(context, "There was an error generating your List. Please check your internet connection and retry.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            } else {
                //make sure the title doesnt match others in the list
                String title = list.getPost().getTitle();
                int titleMatches = 0;
                for (RedditList currList : listAL) {
                    if (currList.getPost().getTitle().contains(title)) {
                        titleMatches++;
                    }
                }
                if (titleMatches > 0)
                    list.getPost().setTitle(title + "(" + titleMatches + ")");

                //check if post is NSFW and ensure the show NSFW option has been selected.
                if (list.getPost().isOver18() && !showNSFW) {
                    DialogManager.showNSFWDialog(context, main, list);

                } else {
                    //add the list and refresh
                    listAL.add(0, list);
                    mAdapter.update(listAL);
                    mGridLayoutmanager.scrollToPositionWithOffset(0, 0);
                    Toast.makeText(context, "Reddit Post added to your lists.", Toast.LENGTH_SHORT).show();
                }

                //show final progress bar and then vanish it
                progressBar.setProgress(100);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                }, 200);
                // Unregister since the activity is about to be closed.
                LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
                asynchRunning = false;
            }
        }
    }
}

