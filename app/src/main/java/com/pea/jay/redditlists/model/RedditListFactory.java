package com.pea.jay.redditlists.model;

import android.content.Context;
import android.util.Log;

/**
 * Created by Paul Wright on 24/12/2016.
 */
public class RedditListFactory {


    public RedditList getListObject(Context context, RedditList redditList) {
        redditList.setPost(new PostFactory().getPostObject(context, RemoteData.readContents(redditList.getJsonURL())));
        redditList.getPost().setRedditList(redditList);
        return redditList;
    }
}
