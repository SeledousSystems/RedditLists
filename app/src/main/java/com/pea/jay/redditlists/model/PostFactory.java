package com.pea.jay.redditlists.model;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.pea.jay.redditlists.libs.JSONJavaMaster.JSONArray;
import com.pea.jay.redditlists.libs.JSONJavaMaster.JSONObject;

import java.util.ArrayList;

public class PostFactory {

    private RedditPost post = new RedditPost();
    private String JSON_URL = "";
    private JSONArray jsonArray_Top;
    private JSONObject postObject;
    private JSONObject commentObject;
    private JSONArray commentsArray;
    private String kindT1 = "t1";
    private String kindMore = "more";
    private Context context;
    Comment comment;
    private ArrayList<Comment> commentList = new ArrayList<>();
    private ArrayList<String> commentIds = new ArrayList<>();
    private ArrayList<String> moreCommentIds = new ArrayList<>();

    public RedditPost getPostObject(Context context, String JSONString) {
        this.context = context;
        //progress update
        //progressUpdate(30);
        Log.d("Main_activity", JSON_URL);
        // assign the string to the factor parameters
        this.JSON_URL = JSONString;

//        Log.d("Main_activity", JSON_URL);
        // set up the top level JSONObjects
        setUpPostFactoryParams();
        //progress update
        //progressUpdate(60);
        setPostParameters();
        // process the original JSON String as it contains all comment
        // objectives
        //buildCommentTreeFromJsonString();
        buildCommentListFromJsonString();
        buildPostCommentIDArrayList();
        //progress update
        //progressUpdate(90);
        return post;
    }

    // Send an Intent w. The Intent sent should be received by the ReceiverActivity.
    private void progressUpdate(int i) {
        Log.d("sender", "Broadcasting message  " + i);
        Intent intent = new Intent("progress-update");
        // add the update data to the intent to update the progress bar
        intent.putExtra("progress", i);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private void setUpPostFactoryParams() {
        System.out.println(" XXXXXXX " + JSON_URL);
        jsonArray_Top = new JSONArray(JSON_URL);
        postObject = jsonArray_Top.getJSONObject(0);
        commentObject = jsonArray_Top.getJSONObject(1);
        commentsArray = commentObject.getJSONObject("data").getJSONArray("children");
    }

    private void setPostParameters() {
        JSONObject postData = postObject.getJSONObject("data").getJSONArray("children").getJSONObject(0)
                .getJSONObject("data");

        post.setSubreddit(postData.optString("subreddit"));
        post.setContestMode(postData.optBoolean("contest_mode"));
        post.setTitle(postData.optString("title"));
        post.setAuthor(postData.optString("author"));
        post.setPoints(postData.optInt("score"));
        post.setNumComments(postData.optInt("num_comments"));
        post.setThumbNail(postData.optString("thumbnail"));
        post.setId(postData.optString("id"));
        post.setPermalink(postData.optString("permalink"));
        post.setSelfText(postData.optString("selftext"));
        post.setName(postData.optString("name"));
        post.setSelf(postData.optBoolean("is_self"));
        post.setUrl(postData.optString("url"));
        post.setGilded(postData.optBoolean("gilded"));
        post.setDowns(postData.optInt("downs"));
        post.setUps(postData.optInt("ups"));
        post.setCreatedUTC(postData.optLong("created_utc"));
        post.setOver18(postData.optBoolean("over_18"));
        post.setScore(postData.optInt("score"));
        post.setEmbeddedLinks(LinkFactory.buildLinkArray(post.getSelfText()));
    }

    //build the commentID arrays, these can be used to view each comment individually with replies
    private void buildPostCommentIDArrayList() {
        int k = 0;
        for (int i = 0; i < commentsArray.length(); i++) {
            JSONObject commentObj = commentsArray.getJSONObject(i);
            String kind = commentObj.optString("kind");

            if (kind.equals(kindT1)) {
                // get the comment id from the listing (comment) object
                String id = commentObj.getJSONObject("data").optString("id");
                commentIds.add(id);
            }
            // get the comment id from the children array of more comments
            if (kind.equals(kindMore)) {
                // get the children array with the more comments ID in it.
                JSONArray moreCommentsIdArray = commentObj.getJSONObject("data").getJSONArray("children");
                // iterate through the array and add the comment IDs to the
                // array
                for (int j = 0; j < moreCommentsIdArray.length(); j++) {
                    String id_2 = moreCommentsIdArray.getString(j);
                    commentIds.add(id_2);
                    moreCommentIds.add(id_2);
                }
            }
        }
        post.setAllCommentIds(commentIds);
        post.setMoreCommetIds(moreCommentIds);
    }

    private void buildCommentListFromJsonString() {
        // create rootNode ArrayList to hold all the comment root nodes

        // get the array, will have two listing obj which are post(0) and
        // comments(1)
        JSONArray jsonArray = new JSONArray(JSON_URL);

        // get the post header object
        JSONObject postHeaderObj = jsonArray.getJSONObject(0);
        // get the comments (all Comments) object
        JSONObject commentsDataObj_2 = jsonArray.getJSONObject(1);

        // gets the Data Object within the comments obj
        JSONObject commentsDataObj = commentsDataObj_2.getJSONObject("data");

        // gets the array of comment Objects, each is one commentObject e.g. a
        // comment and all replies to be added to the tree
        JSONArray jsonCommentsArray = commentsDataObj.getJSONArray("children");

        for (int i = 0; i < jsonCommentsArray.length(); i++) {
            // gets the comment object of a single comment inside the object
            // inside the comment Array
            if (!jsonCommentsArray.getJSONObject(i).optString("kind").equals("more")) {
                JSONObject commentJSONObj = jsonCommentsArray.getJSONObject(i).getJSONObject("data");
                //System.out.println("Starting build");
                Comment comment = buildCommentObj(post, commentJSONObj);
                if (comment != null)
                    commentList.add(comment);
            }
        }
        post.setCommentList(commentList);
        //setComments(rootNodes);
    }

    public ArrayList<Comment> rebuildCommentListFromJsonString(String JSON_URL) {
        // create ArrayList to hold all the comments
        ArrayList<Comment> rebuildcommentList = new ArrayList<>();
        // get the array, will have two listing obj which are post(0) and
        // comments(1)

        String JSON_String = RemoteData.readContents(JSON_URL);
        JSONArray jsonArray = new JSONArray(JSON_String);

        // get the post header object
        JSONObject postHeaderObj = jsonArray.getJSONObject(0);
        // get the comments (all Comments) object
        JSONObject commentsDataObj_2 = jsonArray.getJSONObject(1);

        // gets the Data Object within the comments obj
        JSONObject commentsDataObj = commentsDataObj_2.getJSONObject("data");

        // gets the array of comment Objects, each is one commentObject e.g. a
        // comment and all replies to be added to the tree
        JSONArray jsonCommentsArray = commentsDataObj.getJSONArray("children");

        for (int i = 0; i < jsonCommentsArray.length(); i++) {
            // gets the comment object of a single comment inside the object
            // inside the comment Array
            if (!jsonCommentsArray.getJSONObject(i).optString("kind").equals("more")) {
                JSONObject commentJSONObj = jsonCommentsArray.getJSONObject(i).getJSONObject("data");
                Comment comment = buildCommentObj(post, commentJSONObj);
                if (comment != null) rebuildcommentList.add(comment);
            }
        }
        return rebuildcommentList;
    }

    public static void commentIdtoObj(RedditPost post) {
        ArrayList<String> commentIds = post.getAllCommentIds();

        for (String id : commentIds) {
            String URL = "https://www.reddit.com/r/" + post.getSubreddit() + "/comments/" + post.getId() + "/-/" + id
                    + ".json";
            String JSONData = RemoteData.readContents(URL);
            JSONArray topArray = new JSONArray(JSONData);
            JSONObject commentObject = topArray.getJSONObject(1).getJSONObject("data").getJSONArray("children")
                    .getJSONObject(0).getJSONObject("data");
            if (!commentObject.optString("id").equals(id)) ;
        }
    }

    // build java comment Obj from a JSON comment Obj
    public Comment buildCommentObj(RedditPost post, JSONObject jsonObj) {

        if (!jsonObj.optString("body").equals("[deleted]") && !jsonObj.optString("body").equals("[removed]")) {
            Comment comment = new Comment();
            comment.setPost(post);
            comment.setBody(jsonObj.optString("body"));
            comment.setName(jsonObj.optString("name"));
            comment.setParentId(jsonObj.optString("parent_id"));
            comment.setAuthor(jsonObj.optString("author"));
            comment.setApiId(jsonObj.optString("id"));
            comment.setLinkId(jsonObj.optString("link_id"));
            comment.setScore(jsonObj.optInt("score"));
            comment.setDowns(jsonObj.optInt("downs"));
            comment.setUps(jsonObj.optInt("ups"));
            comment.setCreatedDateTime(jsonObj.optLong("created"));
            comment.setKind(jsonObj.optString("kind"));

            //add the comment links via the links factory
            comment.setLinks(LinkFactory.buildLinkArray(comment.getBody()));

            return comment;
        } else {
            return null;
        }

    }

}
