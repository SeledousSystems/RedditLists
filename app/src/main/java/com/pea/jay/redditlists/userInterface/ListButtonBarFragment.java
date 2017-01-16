package com.pea.jay.redditlists.userInterface;

import android.content.ClipData;
import android.content.ClipboardManager;
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
import com.pea.jay.redditlists.model.Comment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListButtonBarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListButtonBarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListButtonBarFragment extends Fragment {
    private Comment comment;

    private String TAG = "ButtonBarFrag";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ListButtonBarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GridButtonBarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListButtonBarFragment newInstance() {
        ListButtonBarFragment fragment = new ListButtonBarFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void updateData(Comment comment) {
        this.comment = comment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_button_bar_list, container, false);
        Button deleteButton = (Button) view.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerListActivity rla = (RecyclerListActivity) getActivity();
                rla.deleteComment(comment);
                rla.showToast("Comment deleted.");
                rla.showOptions(false);
            }
        });

        Button copyButton = (Button) view.findViewById(R.id.copyTextButton);
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "copy Button");
                RecyclerListActivity rla = (RecyclerListActivity) getActivity();
                //get the clipboard service
                ClipboardManager clipboard = (ClipboardManager)
                        rla.getSystemService(Context.CLIPBOARD_SERVICE);
                // Creates a new text clip to put on the clipboard
                ClipData clip = ClipData.newPlainText("comment", comment.getBody());
                // Set the clipboard's primary clip.
                clipboard.setPrimaryClip(clip);
                rla.showToast("Comment Text Copied to Clipboard.");
                rla.showOptions(false);
            }
        });

        Button openPostButton = (Button) view.findViewById(R.id.openCommentButton);
        openPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerListActivity rla = (RecyclerListActivity) getActivity();
                if (!isNetworkConnected()) {
                    rla.showToast("You are not connected to the internet. You must have internet access to open Reddit Comments.");
                } else {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    String URI = "https://reddit.com" + comment.getPost().getPermalink() + comment.getName().substring(3);
                    Log.d(TAG, "URI = " + URI);
                    i.setData(Uri.parse(URI));
                    rla.showOptions(false);
                    startActivity(i);
                }
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onListButtonBarFragmentInteraction(uri);
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
        void onListButtonBarFragmentInteraction(Uri uri);
    }
}
