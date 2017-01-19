/*
 *
 *   Copyright (c) 2016.
 *
 *  The source code contained in this file remains the intellectual property of Paul Wright (PeaJay).
 *  Any reuse, adaption or replication of this code, without express permission, is prohibited.
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the
 *  License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF
 *  ANY KIND, either express or implied.
 *
 *
 */

package com.pea.jay.redditlists.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.EditText;

import com.pea.jay.redditlists.R;
import com.pea.jay.redditlists.model.RedditList;
import com.pea.jay.redditlists.userInterface.MainActivity;

import it.gmariotti.changelibs.library.view.ChangeLogRecyclerView;


/**
 * Created by Paul on 9/11/2016.
 * <p>
 * class to handle dialogs and nav menu options
 */
public class DialogManager {
    static String TAG = "DialogManagerTAG";

    /**
     * method to create the info dialog
     *
     * @param context
     */
    public static void showInfoDialog(Context context) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Information");
        builder.setMessage(context.getResources().getString(R.string.info_body));
        builder.setNegativeButton("Close", null);
        builder.show();
    }

    /**
     * method to create the how to use dialog
     *
     * @param context
     */
    public static void showHowDialog(Context context) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("How To Use Lists for Reddit");
        builder.setMessage(context.getResources().getString(R.string.how_body));
        builder.setNegativeButton("Close", null);
        builder.show();
    }

    /**
     * method to create the version dialog
     *
     * @param context
     */
    public static void showVersionDialog(Context context) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Version");
        builder.setMessage("Version : " + context.getResources().getString(R.string.version) + "\nDeveloper : PeaJay");
        builder.setNegativeButton("Close", null);
        builder.show();
    }

    /**
     * method to create the how to use dialog
     *
     * @param context
     */
    public static void showPasteLinkDialog(Context context, final MainActivity main) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Build List from Reddit Link");
        final EditText input = new EditText(context);
        input.setHint("paste link here");
        builder.setPositiveButton("Build List", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int arg1) {
                if (StringManager.checkStringIsRedditComment(input.getText().toString())) {
                    String url = input.getText().toString().replace("http://", "https://");
                    url = url.replaceAll("\\s", "");
                    main.generateList(url);
                } else {
                    main.showToast(main.getResources().getString(R.string.toast_bad_link));
                }
                d.cancel();
            }
        });
        builder.setView(input);
        builder.setNegativeButton("Close", null);
        builder.show();
    }


    /**
     * method to create the how to use dialog
     *
     * @param context
     */

    public static void showNSFWDialog(Context context, final MainActivity main, final RedditList redditList) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("NSFW Post");
        builder.setMessage("This Post is tagged as NSFW (over 18yrs). Do you wish to add this post to your Lists?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface d, int arg1) {
                main.addList(redditList);
                d.cancel();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }


    /**
     * method to handle the rate app nav menu option
     *
     * @param context
     */
    public static void rate(Context context, final MainActivity main) {
        //TODO update uri
        String playURL = main.getResources().getString(R.string.play_store_url);
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(playURL)));
    }

    /**
     * method to handle the rate app nav menu option
     *
     * @param context
     */
    public static void launchRedditApp(Context context, MainActivity main) {

        try {
            String url = "https://www.reddit.com";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            context.startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
            main.showToast(main.getResources().getString(R.string.toast_no_internet));
        }
    }


    /**
     * Chow changelog dialog
     *
     * @param context
     * @param main
     */
    public static void showChangeLog(Context context, MainActivity main) {
        ChangeLogRecyclerView chgList = (ChangeLogRecyclerView) main.getLayoutInflater().inflate(R.layout.changelog_recycler, null);

        AlertDialog.Builder builder =
                new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle)
                        .setTitle("Change Log")
                        .setView(chgList)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.dismiss();
                                    }
                                }
                        );
        builder.show();
    }
}
