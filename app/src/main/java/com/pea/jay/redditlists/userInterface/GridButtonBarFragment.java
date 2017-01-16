package com.pea.jay.redditlists.userInterface;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pea.jay.redditlists.R;
import com.pea.jay.redditlists.model.RedditList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GridButtonBarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GridButtonBarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GridButtonBarFragment extends Fragment {
    private RedditList rl;

    private String TAG = "ButtonBarFrag";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public GridButtonBarFragment() {
        // Required empty public constructor
    }

    public void updateData(RedditList rl) {
        this.rl = rl;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GridButtonBarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GridButtonBarFragment newInstance() {
        GridButtonBarFragment fragment = new GridButtonBarFragment();
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
        View view = inflater.inflate(R.layout.fragment_button_bar_grid, container, false);
        Button deleteButton = (Button) view.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity main = (MainActivity) getActivity();
                main.showToast(rl.getPost().getTitle() + " - has been deleted.");
                main.deleteList(rl);
                main.handleItemSelected(false);
                Log.d(TAG, "delete Button");
            }
        });

        Button copyButton = (Button) view.findViewById(R.id.copyButton);
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity main = (MainActivity) getActivity();
                main.generateList(rl.getRawURL());
                main.handleItemSelected(false);

            }
        });

        Button viewButton = (Button) view.findViewById(R.id.viewButton);
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity main = (MainActivity) getActivity();
                Intent listIntent = new Intent(main, RecyclerListActivity.class);
                listIntent.putExtra(MainActivity.INTENT_LIST_OBJ, rl);
                main.handleItemSelected(false);
                startActivity(listIntent);
            }
        });

        Button openPostButton = (Button) view.findViewById(R.id.openPostButton);
        openPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity main = (MainActivity) getActivity();
                if (!isNetworkConnected()) {
                    // main.showSnackBar("You are not connected to the internet. You must have internet access to open Reddit Posts.", 2, null);
                    main.showToast("You are not connected to the internet. You must have internet access to open Reddit Posts.");
                } else {
                    try {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(rl.getRawURL()));
                        main.handleItemSelected(false);
                        startActivity(i);
                        main.showToast("Opening Reddit.");
                    } catch (Exception e) {
                        e.printStackTrace();
                        main.showToast("Couldn't open Reddit at this time. Please check your internet and try again.");
                    }
                }
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onGridButtonBarFragmentInteraction(uri);
        }
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
        void onGridButtonBarFragmentInteraction(Uri uri);
    }
}
