package com.pea.jay.redditlists.userInterface;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pea.jay.redditlists.R;
import com.pea.jay.redditlists.customViews.CustomGridView;
import com.pea.jay.redditlists.customViews.CustomGridViewAdaptor;
import com.pea.jay.redditlists.customViews.CustomListViewAdaptor;
import com.pea.jay.redditlists.model.RedditList;
import com.pea.jay.redditlists.persistance.GlobalListHolder;
import com.pea.jay.redditlists.persistance.SharedPrefManager;

import java.util.ArrayList;
import java.util.Collections;

public class SearchActivity extends AppCompatActivity {

    private static String TAG = "Search_Activity";
    private ListView listView;
    private Context context;
    private CustomListViewAdaptor listViewAdaptor;
    private ArrayList<RedditList> lists;
    private ArrayList<RedditList> fullListAL;
    private ArrayList<String> subRedditAL;
    private RelativeLayout subredditRL;
    private ArrayList<String> recentSearches;
    private SharedPrefManager spm;
    private ArrayList<TextView> recentSearchTVs;
    private TextView recentSearch1, recentSearch2, recentSearch3, recentSearch4, recentSearch5;
    private LinearLayout recentSearchLL;
    private GridView gridView;
    private CustomGridView customGridView;
    private CustomGridViewAdaptor customGridViewAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        spm = new SharedPrefManager(context);
        //subredditRL = (RelativeLayout) findViewById(R.id.subredditRL);
        recentSearchLL = (LinearLayout) findViewById(R.id.recentSearchLL);


        lists = new ArrayList<>();

        listView = (ListView) findViewById(R.id.listView);
        listViewAdaptor = new CustomListViewAdaptor(context, lists);
        listView.setAdapter(listViewAdaptor);
        listView.setVisibility(View.GONE);

        fullListAL = GlobalListHolder.getInstance(context).getMasterList();
        subRedditAL = new ArrayList<>();
        buildSubTags();

        gridView = (GridView) findViewById(R.id.subredditGV);
        customGridViewAdaptor = new CustomGridViewAdaptor(this, subRedditAL);

        gridView.setAdapter(customGridViewAdaptor);

        gridView.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, subRedditAL.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        recentSearchTVs = new ArrayList<>();
        recentSearch1 = (TextView) findViewById(R.id.recent1TV);
        recentSearchTVs.add(recentSearch1);
        recentSearch2 = (TextView) findViewById(R.id.recent2TV);
        recentSearchTVs.add(recentSearch2);
        recentSearch3 = (TextView) findViewById(R.id.recent3TV);
        recentSearchTVs.add(recentSearch3);
        recentSearch4 = (TextView) findViewById(R.id.recent4TV);
        recentSearchTVs.add(recentSearch4);
        recentSearch5 = (TextView) findViewById(R.id.recent5TV);
        recentSearchTVs.add(recentSearch5);
        recentSearches = spm.getRecentSearches();
        buildrecentViews();
    }

    private void buildrecentViews() {
        Log.d(TAG, " Size of recent " + recentSearches.size());
        Log.d(TAG, " Size of TVs " + recentSearchTVs.size());
        for (int i = 0; i < recentSearchTVs.size(); i++) {
            if (i < recentSearches.size()) {
                recentSearchTVs.get(i).setText(recentSearches.get(i));
                Log.d(TAG, i + recentSearches.get(i));
            } else
                recentSearchTVs.get(i).setVisibility(View.GONE);
        }
    }


    private void buildSubTags() {
        for (RedditList list : fullListAL) {

            String subReddit = list.getPost().getSubreddit();
            Log.d(TAG, subReddit + " Subreddit ");
            if (!subRedditAL.contains(subReddit)) {
                subRedditAL.add(subReddit);
                Log.d(TAG, subReddit + " Subreddit 99999");
            }
        }
        Collections.sort(subRedditAL);

        for (String sub : subRedditAL) {
            RelativeLayout subTag = (RelativeLayout) this.getLayoutInflater().inflate(R.layout.subreddit_search_tag_layout, null, false);
            TextView subredditTV = (TextView) subTag.findViewById(R.id.subbredditTagTV);
            subredditTV.setText("r/" + sub);

            // subRedditGL.addView(subTag);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Inflate the menu; this adds items to the action bar if it is present.
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != "") {
                    showRecentSearches(false);

                } else {
                    showRecentSearches(true);
                }
                return false;
            }
        });
        return true;
    }

    private void showRecentSearches(boolean show) {
        if (show) {
            recentSearchLL.setVisibility(View.VISIBLE);

        } else {
            recentSearchLL.setVisibility(View.GONE);
        }

    }


    @Override
    protected void onPause() {
        spm.saveRecentSearchSet(recentSearches);
        super.onPause();
    }
}
