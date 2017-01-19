package com.pea.jay.redditlists.model;


import android.util.Log;

import java.io.Serializable;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;

/**
 * Created by Paul Wright on 24/12/2016.
 * <p>
 * Reddit list POJO. Master object in the Reddit List Object Heirachy
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


    public static class DateBackwardComparator implements Comparator<RedditList> {
        public int compare(RedditList r1, RedditList r2) {
            return (int) (r1.getCreated() - r2.getCreated());
        }
    }

    public static class DateForwardComparator implements Comparator<RedditList> {
        public int compare(RedditList r1, RedditList r2) {
            return (int) (r2.getCreated() - r1.getCreated());
        }
    }

    public static class TitleBackwardComparator implements Comparator<RedditList> {
        public int compare(RedditList r1, RedditList r2) {
            String title1 = r1.getPost().getTitle().toLowerCase();
            String title2 = r2.getPost().getTitle().toLowerCase();
            Pattern p = Pattern.compile("\\p{L}");

            Matcher titleLetter1 = p.matcher(title1);
            Matcher titleLetter2 = p.matcher(title2);
            titleLetter1.find();
            titleLetter2.find();

            return (titleLetter2.group().compareTo(titleLetter1.group()));
        }
    }

    public static class TitleForwardComparator implements Comparator<RedditList> {
        public int compare(RedditList r1, RedditList r2) {
            String title1 = r1.getPost().getTitle().toLowerCase();
            String title2 = r2.getPost().getTitle().toLowerCase();
            Pattern p = Pattern.compile("\\p{L}");

            Matcher titleLetter1 = p.matcher(title1);
            Matcher titleLetter2 = p.matcher(title2);
            titleLetter1.find();
            titleLetter2.find();
            String l1 = titleLetter1.group();
            String l2 = titleLetter2.group();

            return l1.compareTo(l2);

        }
    }

    public static class SubredditForwardComparator implements Comparator<RedditList> {
        public int compare(RedditList r1, RedditList r2) {
            String sub1 = r1.getPost().getSubreddit().toLowerCase();
            String sub2 = r2.getPost().getSubreddit().toLowerCase();
            return sub1.compareTo(sub2);
        }
    }

    public static class SubredditBackwardComparator implements Comparator<RedditList> {
        public int compare(RedditList r1, RedditList r2) {
            String sub1 = r1.getPost().getSubreddit().toLowerCase();
            String sub2 = r2.getPost().getSubreddit().toLowerCase();
            return sub2.compareTo(sub1);
        }
    }

    public static class ColorComparator implements Comparator<RedditList> {
        public int compare(RedditList r1, RedditList r2) {
            String sub1 = r1.getColorString().toLowerCase();
            String sub2 = r2.getColorString().toLowerCase();
            return sub2.compareTo(sub1);
        }
    }


}
