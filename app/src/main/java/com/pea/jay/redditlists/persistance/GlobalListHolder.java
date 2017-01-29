package com.pea.jay.redditlists.persistance;

import android.app.Application;
import android.content.Context;

import com.pea.jay.redditlists.model.RedditList;

import java.util.ArrayList;

/**
 * Created by Paul Wright on 13/01/2017.
 */
public class GlobalListHolder extends Application {

    private static GlobalListHolder instance = null;
    private ArrayList<RedditList> masterList;
    private Context context;
    private boolean alreadyStoring = false;

    private GlobalListHolder(Context context) {
        super();
        this.context = context;
        //InternalStorage.setupArrayList(context);
        masterList = InternalStorage.getAllLists(context);

    }

    /**
     * don't call this constructor. Implemented for the application start only.
     * Call get instance to ensure singleton is abided
     */
    public GlobalListHolder() {
    }

    public static GlobalListHolder getInstance(Context context) {
        if (instance == null) {
            instance = new GlobalListHolder(context);
        }
        return instance;
    }

    public ArrayList<RedditList> getMasterList() {
        return masterList;
    }

    public void storeMasterList() {

        if (!alreadyStoring) {
            alreadyStoring = true;
            InternalStorage.writeList(context, masterList);
            alreadyStoring = false;
        }
    }
}
