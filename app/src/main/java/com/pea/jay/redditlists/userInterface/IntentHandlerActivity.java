package com.pea.jay.redditlists.userInterface;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.pea.jay.redditlists.R;

public class IntentHandlerActivity extends AppCompatActivity {

    public static String Intent_key_URL = "sharedURL";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_handler);

        Intent intent = getIntent();

        String url = intent.getStringExtra(Intent.EXTRA_TEXT);
        intent.removeExtra(Intent.EXTRA_TEXT);
        setIntent(null);

        url = url.replace("http://", "https://");
        url = url.replaceAll("\\s","");

        Log.d("IntentHandler", "url =" + url);

        Intent newIntent = new Intent(this, MainActivity.class);
        newIntent.putExtra(Intent_key_URL, url);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(newIntent);
        finish();


    }

}
