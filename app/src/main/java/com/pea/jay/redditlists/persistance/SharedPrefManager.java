package com.pea.jay.redditlists.persistance;

import android.content.Context;
import android.content.SharedPreferences;

import com.pea.jay.redditlists.R;

/**
 * Created by Paul Wright on 10/12/2016.
 */
public class SharedPrefManager {
    Context context;
    SharedPreferences sharedPref;
    private boolean initialBootMain = true;
    private boolean initialBootList = true;
    private boolean showNSFW = false;
    private boolean randomColors = true;
    private int demoNumber = 0;
    SharedPreferences.Editor editor;

    public SharedPrefManager(Context context) {
        this.context = context;
        sharedPref = context.getSharedPreferences(context.getResources().getString(R.string.PREF_FILE_KEY), Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        initialBootMain = sharedPref.getBoolean(context.getResources().getString(R.string.pref_intial_boot_main), true);
        initialBootList = sharedPref.getBoolean(context.getResources().getString(R.string.pref_intial_boot_list), true);
        showNSFW = sharedPref.getBoolean(context.getResources().getString(R.string.pref_show_NSFW), false);
        randomColors = sharedPref.getBoolean(context.getResources().getString(R.string.random_colors), true);
        demoNumber = sharedPref.getInt(context.getResources().getString(R.string.demo_number), 0);
        editor.commit();
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
