package com.pea.jay.redditlists.model;


import java.io.Serializable;

/**
 * Created by Paul Wright on 24/12/2016.
 *
 * Reddit list POJO. Master object in the Reddit List Object Heirachy
 *
 */
public class RedditList implements Serializable {

    private RedditPost post;
    private long created;
    private boolean showLinks;
    private boolean showComments;
    private boolean isTreeStyle;
    private String rawURL;
    private String jsonURL;
    private int numComments = 500;
    private String colorString = "white";
    private boolean allReplies = false;

    public boolean isAllReplies() {
        return allReplies;
    }

    public void setAllReplies(boolean allReplies) {
        this.allReplies = allReplies;
    }

    public String getColorString() {
        return colorString;
    }

    public void setColorString(String colorString) {
        this.colorString = colorString;
    }

    private int color;
    private static final long serialVersionUID = 1;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getRawURL() {
        return rawURL;
    }

    public String getJsonURL() {
        return rawURL + ".json" + "?limit=" + numComments;
    }

    public void setJsonURL(String jsonURL) {
        this.jsonURL = jsonURL;
    }

    public int getNumComments() {
        return numComments;
    }

    public void setNumComments(int numComments) {
        this.numComments = numComments;
    }

    public boolean isShowComments() {
        return showComments;
    }

    public void setShowComments(boolean showComments) {
        this.showComments = showComments;
    }

    public boolean isShowLinks() {
        return showLinks;
    }

    public void setShowLinks(boolean showLinks) {
        this.showLinks = showLinks;
    }

    public boolean isTreeStyle() {
        return isTreeStyle;
    }

    public void setTreeStyle(boolean treeStyle) {
        isTreeStyle = treeStyle;
    }

    public void setRawURL(String rawURL) {
        this.rawURL = rawURL;
    }

    public long getCreated() {
        return created;
    }

    public RedditPost getPost() {
        return post;
    }

    public void setPost(RedditPost post) {
        this.post = post;
    }

    public RedditList() {
        this.created = System.currentTimeMillis();
    }

}
