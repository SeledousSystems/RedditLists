package com.pea.jay.redditlists.userInterface;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pea.jay.redditlists.R;
import com.pea.jay.redditlists.customViews.CustomGridView;
import com.pea.jay.redditlists.customViews.CustomGridViewAdaptor;
import com.pea.jay.redditlists.customViews.CustomListViewAdaptor;
import com.pea.jay.redditlists.model.RedditList;
import com.pea.jay.redditlists.persistance.GlobalListHolder;
import com.pea.jay.redditlists.persistance.SharedPrefManager;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchViewFragment extends Fragment implements View.OnClickListener {

    private Context context;
    private CustomListViewAdaptor listViewAdaptor;
    private ArrayList<RedditList> foundLists = new ArrayList<>();
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
    private MainActivity main;

    private String TAG = "SearchViewFrag";

    private OnFragmentInteractionListener mListener;

    public SearchViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GridButtonBarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchViewFragment newInstance() {
        SearchViewFragment fragment = new SearchViewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * onCreate method called when the fragment is called
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        context = getActivity().getBaseContext();

        main = (MainActivity) getActivity();

        spm = new SharedPrefManager(context);
        //subredditRL = (RelativeLayout) findViewById(R.id.subredditRL);
        recentSearchLL = (LinearLayout) view.findViewById(R.id.recentSearchLL);

        fullListAL = GlobalListHolder.getInstance(context).getMasterList();
        subRedditAL = new ArrayList<>();
        buildSubTags();

        gridView = (GridView) view.findViewById(R.id.subredditGV);
        customGridViewAdaptor = new CustomGridViewAdaptor(this.getActivity(), subRedditAL);
        gridView.setAdapter(customGridViewAdaptor);
        gridView.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchBySubreddit(subRedditAL.get(position));
            }
        });

        recentSearchTVs = new ArrayList<>();
        recentSearch1 = (TextView) view.findViewById(R.id.recent1TV);
        recentSearch1.setOnClickListener(this);
        recentSearchTVs.add(recentSearch1);
        recentSearch2 = (TextView) view.findViewById(R.id.recent2TV);
        recentSearchTVs.add(recentSearch2);
        recentSearch2.setOnClickListener(this);
        recentSearch3 = (TextView) view.findViewById(R.id.recent3TV);
        recentSearch3.setOnClickListener(this);
        recentSearchTVs.add(recentSearch3);
        recentSearch4 = (TextView) view.findViewById(R.id.recent4TV);
        recentSearch4.setOnClickListener(this);
        recentSearchTVs.add(recentSearch4);
        recentSearch5 = (TextView) view.findViewById(R.id.recent5TV);
        recentSearch5.setOnClickListener(this);
        recentSearchTVs.add(recentSearch5);
        recentSearches = spm.getRecentSearches();
        buildRecentViews();

        return view;
    }

    /**
     * method for builfing the recent search string views
     */
    private void buildRecentViews() {
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

    /**
     * onClick methdo to handle the colour and recent search click listeners
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case (R.id.recent1TV):
                searchByString(recentSearch1.getText().toString());
                break;
            case (R.id.recent2TV):
                searchByString(recentSearch2.getText().toString());
                break;
            case (R.id.recent3TV):
                searchByString(recentSearch3.getText().toString());
                break;
            case (R.id.recent4TV):
                searchByString(recentSearch4.getText().toString());
                break;
            case (R.id.recent5TV):
                searchByString(recentSearch5.getText().toString());
                break;
            case (R.id.blue):
                searchByColor("blue");
                break;
            case (R.id.red):
                searchByColor("red");
                break;
            case (R.id.green):
                searchByColor("green");
                break;
            case (R.id.grey):
                searchByColor("grey");
                break;
            case (R.id.white):
                searchByColor("white");
                break;
            case (R.id.yellow):
                searchByColor("yellow");
                break;
            default:
                break;
        }
    }

    /**
     * Method to search by list color
     *
     * @param colorString
     */
    public void searchByColor(String colorString) {
        foundLists.clear();
        for (RedditList list : fullListAL) {
            if (list.getColorString().equals(colorString)) {
                foundLists.add(list);
            }
        }
        main.setDataList(foundLists);
    }

    /**
     * Method to search list title by string
     *
     * @param string
     */
    public void searchByString(String string) {
        foundLists.clear();
        for (RedditList list : fullListAL) {
            if (list.getPost().getTitle().contains(string)) {
                foundLists.add(list);
            }
        }
        main.setDataList(foundLists);
    }

    /**
     * Method to search by Subreddit
     *
     * @param string
     */
    public void searchBySubreddit(String string) {
        foundLists.clear();
        for (RedditList list : fullListAL) {
            if (list.getPost().getSubreddit().contains(string)) {
                foundLists.add(list);
            }
        }
        main.setDataList(foundLists);
    }

    /**
     * method to build the tags layout
     */
    private void buildSubTags() {
        for (RedditList list : fullListAL) {
            String subReddit = list.getPost().getSubreddit();
            if (!subRedditAL.contains(subReddit)) {
                subRedditAL.add(subReddit);
            }
        }
        Collections.sort(subRedditAL);

        for (String sub : subRedditAL) {
            RelativeLayout subTag = (RelativeLayout) this.getActivity().getLayoutInflater().inflate(R.layout.subreddit_search_tag_layout, null, false);
            TextView subredditTV = (TextView) subTag.findViewById(R.id.subbredditTagTV);
            subredditTV.setText("r/" + sub);
        }
    }

    /**
     * onAttach method for when the fragment attaches to the activity
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * onDetach method for when method detaches from activity
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSearchViewFragmentInteraction(Uri uri);
    }
}
