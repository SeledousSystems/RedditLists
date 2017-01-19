package com.pea.jay.redditlists.persistance;

import android.content.Context;
import android.content.SharedPreferences;

import com.pea.jay.redditlists.R;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Paul Wright on 10/12/2016.
 */
public class SharedPrefManager {
    Context context;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    private boolean initialBootMain = true;
    private boolean initialBootList = true;
    private boolean showNSFW = false;
    private boolean randomColors = true;
    private int demoNumber = 0;
    private HashSet recentSearchSet = new HashSet();
    private ArrayList<String> recentSearchList;

    public SharedPrefManager(Context context) {
        this.context = context;
        sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.PREF_FILE_KEY), Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        initialBootMain = sharedPref.getBoolean(context.getResources().getString(R.string.pref_intial_boot_main), true);
        initialBootList = sharedPref.getBoolean(context.getResources().getString(R.string.pref_intial_boot_list), true);
        showNSFW = sharedPref.getBoolean(context.getResources().getString(R.string.pref_show_NSFW), false);
        randomColors = sharedPref.getBoolean(context.getResources().getString(R.string.random_colors), true);
        demoNumber = sharedPref.getInt(context.getResources().getString(R.string.demo_number), 0);
        recentSearchSet = (HashSet<String>) sharedPref.getStringSet(context.getResources().getString(R.string.recent_search_set), new HashSet<String>());
        recentSearchList = new ArrayList<>(recentSearchSet);
        editor.commit();
    }

    public void saveRecentSearchSet(ArrayList<String> list) {
        this.recentSearchSet = new HashSet<>(list);
        editor.putStringSet(context.getResources().getString(R.string.recent_search_set), recentSearchSet);
    }


    public ArrayList<String> getRecentSearches() {
        recentSearchList.add("one");
        recentSearchList.add("two");
        recentSearchList.add("three");
        recentSearchList.add("four");
        recentSearchList.add("five");
        return recentSearchList;
    }

    public int getDemoNumber() {
        return demoNumber;
    }

    public void saveDemoNumber(int demoNumber) {
        editor.putInt(context.getResources().getString(R.string.demo_number), demoNumber);
        editor.commit();
        this.demoNumber = demoNumber;
    }


    public boolean isInitialBootMain() {
        return initialBootMain;
    }

    public void saveInitialBootMain(boolean initialBootMain) {
        editor.putBoolean(context.getResources().getString(R.string.pref_intial_boot_main), initialBootMain);
        editor.commit();
        this.initialBootMain = initialBootMain;
    }

    public void saveShowNSFW(boolean showNSFW) {
        editor.putBoolean(context.getResources().getString(R.string.pref_show_NSFW), showNSFW);
        editor.commit();
        this.showNSFW = showNSFW;
    }

    public void saveRandomColor(boolean randomColors) {
        editor.putBoolean(context.getResources().getString(R.string.random_colors), randomColors);
        editor.commit();
        this.randomColors = randomColors;
    }

    public boolean isRandomColors() {
        return randomColors;
    }


    public boolean isShowNSFW() {
        return showNSFW;
    }

    public boolean isInitialBootList() {
        return initialBootList;
    }

    public void saveInitialBootList(boolean initialBootList) {
        editor.putBoolean(context.getResources().getString(R.string.pref_show_NSFW), initialBootList);
        editor.commit();
        this.initialBootList = initialBootList;
    }
}
