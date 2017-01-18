package com.pea.jay.redditlists.userInterface;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pea.jay.redditlists.R;
import com.pea.jay.redditlists.model.RedditList;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GridColorBarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GridColorBarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GridColorBarFragment extends Fragment implements View.OnClickListener {
    private String colorString = "white";
    private RelativeLayout red, blue, white, grey, yellow, green;
    private TextView whiteTV, greyTV, redTV, yellowTV, blueTV, greenTV;
    private View view;
    private RedditList rl;
    private ArrayList<TextView> ticks = new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    public GridColorBarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GridColorBarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GridColorBarFragment newInstance() {
        GridColorBarFragment fragment = new GridColorBarFragment();
        return fragment;
    }

    private void storeColorChange(String color) {
        rl.setColorString(color);
        MainActivity main = (MainActivity) getActivity();
        main.handleItemSelected(false);
    }

    private void updateColorBar(String color) {
        // vanish all ticks
        for (TextView tv : ticks) {
            tv.setVisibility(View.INVISIBLE);
        }
        //set the tick from the arg
        switch (color) {
            case "white":
                whiteTV.setVisibility(View.VISIBLE);
                break;
            case "grey":
                greyTV.setVisibility(View.VISIBLE);
                break;
            case "yellow":
                yellowTV.setVisibility(View.VISIBLE);
                break;
            case "green":
                greenTV.setVisibility(View.VISIBLE);
                break;
            case "blue":
                blueTV.setVisibility(View.VISIBLE);
                break;
            case "red":
                redTV.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void manageColorChanged(String color) {
        updateColorBar(color);
        storeColorChange(color);
    }


    private void setUpViews() {
        red = (RelativeLayout) view.findViewById(R.id.red);
        red.setOnClickListener(this);
        blue = (RelativeLayout) view.findViewById(R.id.blue);
        blue.setOnClickListener(this);
        yellow = (RelativeLayout) view.findViewById(R.id.yellow);
        yellow.setOnClickListener(this);
        green = (RelativeLayout) view.findViewById(R.id.green);
        green.setOnClickListener(this);
        grey = (RelativeLayout) view.findViewById(R.id.grey);
        grey.setOnClickListener(this);
        white = (RelativeLayout) view.findViewById(R.id.white);
        white.setOnClickListener(this);

        greenTV = (TextView) view.findViewById(R.id.greenTV);
        whiteTV = (TextView) view.findViewById(R.id.whiteTV);
        blueTV = (TextView) view.findViewById(R.id.blueTV);
        redTV = (TextView) view.findViewById(R.id.redTV);
        yellowTV = (TextView) view.findViewById(R.id.yellowTV);
        whiteTV = (TextView) view.findViewById(R.id.whiteTV);
        greyTV = (TextView) view.findViewById(R.id.greyTV);
        ticks.add(redTV);
        ticks.add(greenTV);
        ticks.add(blueTV);
        ticks.add(redTV);
        ticks.add(greyTV);
        ticks.add(yellowTV);
        ticks.add(whiteTV);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.white:
                manageColorChanged("white");
                break;
            case R.id.red:
                manageColorChanged("red");
                break;
            case R.id.blue:
                manageColorChanged("blue");
                break;
            case R.id.yellow:
                manageColorChanged("yellow");
                break;
            case R.id.green:
                manageColorChanged("green");
                break;
            case R.id.grey:
                manageColorChanged("grey");
                break;
            default:
                break;
        }

    }

    public void updateData(RedditList rl) {
        this.rl = rl;
        updateColorBar(rl.getColorString());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_color_bar_grid, container, false);
        setUpViews();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onGridColorBarFragmentInteraction(uri);
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
        void onGridColorBarFragmentInteraction(Uri uri);
    }
}
