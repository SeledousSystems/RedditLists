package com.pea.jay.redditlists.userInterface;

import android.content.Context;
import android.net.ConnectivityManager;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchViewFragment extends Fragment {

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_search, container, false);
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
                Toast.makeText(context, subRedditAL.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        recentSearchTVs = new ArrayList<>();
        recentSearch1 = (TextView) view.findViewById(R.id.recent1TV);
        recentSearchTVs.add(recentSearch1);
        recentSearch2 = (TextView) view.findViewById(R.id.recent2TV);
        recentSearchTVs.add(recentSearch2);
        recentSearch3 = (TextView) view.findViewById(R.id.recent3TV);
        recentSearchTVs.add(recentSearch3);
        recentSearch4 = (TextView) view.findViewById(R.id.recent4TV);
        recentSearchTVs.add(recentSearch4);
        recentSearch5 = (TextView) view.findViewById(R.id.recent5TV);
        recentSearchTVs.add(recentSearch5);
        recentSearches = spm.getRecentSearches();
        buildRecentViews();

        return view;
    }

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


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSearchViewFragmentInteraction(uri);
        }
    }


    private void searchByColor(String colorString) {
        foundLists.clear();
        for (RedditList list : fullListAL) {
            if (list.getColorString().equals(colorString)) {
                foundLists.add(list);
            }
        }
        main.setDataList(foundLists);
    }


    private void searchByString(String string) {
        foundLists.clear();
        for (RedditList list : fullListAL) {
            if (list.getPost().getTitle().contains(string)) {
                foundLists.add(list);
            }
        }
        main.setDataList(foundLists);

    }

    private void searchBySubreddit(String string) {
        foundLists.clear();
        for (RedditList list : fullListAL) {
            if (list.getPost().getSubreddit().contains(string)) {
                foundLists.add(list);
            }
        }
        main.setDataList(foundLists);

    }

    /**
     * check network is connected
     *
     * @return
     */
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


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

            // subRedditGL.addView(subTag);
        }
    }

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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSearchViewFragmentInteraction(Uri uri);
    }
}
