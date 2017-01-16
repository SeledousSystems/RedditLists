package com.pea.jay.redditlists.model;

import java.io.Serializable;
import java.util.ArrayList;

public class RedditPost implements Serializable {

    private String subreddit;
    private String title;
    private String author;
    private int points;
    private int numComments;
    private boolean isSelf;
    private String permalink;
    private String id;
    private String thumbNail;
    private String type;
    private String selfText;
    private String name;
    private String url;
    private String permaLink;
    private boolean contestMode;
    private boolean gilded;
    private boolean over18;
    private int score;
    private int downs;
    private int ups;
    private double upvoteRatio;
    private long createdUTC;
    private String thumNail;
    private boolean hasLinks;
    private int numTopComments = 100;
    private ArrayList<String> allCommentIds = new ArrayList<>();
    private ArrayList<String> moreCommetIds = new ArrayList<>();
    private RedditList redditList;
    private static final long serialVersionUID = 1;
    private ArrayList<Comment> commentList = new ArrayList<>();
    private ArrayList<Link> embeddedLinks = new ArrayList<>();

    public RedditPost(RedditList redditList) {
        this.redditList = redditList;
    }

    public ArrayList<String> getMoreCommetIds() {
        return moreCommetIds;
    }

    public void setMoreCommetIds(ArrayList<String> moreCommetIds) {
        this.moreCommetIds = moreCommetIds;
    }

    public RedditPost() {
    }

    public ArrayList<Link> getEmbeddedLinks() {
        return embeddedLinks;
    }

    public void setEmbeddedLinks(ArrayList<Link> embeddedLinks) {
        this.embeddedLinks = embeddedLinks;
    }

    public ArrayList<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(ArrayList<Comment> commentList) {
        this.commentList = commentList;
    }

    public RedditList getRedditList() {
        return redditList;
    }

    public void setRedditList(RedditList redditList) {
        this.redditList = redditList;
    }

    public String getThumNail() {
        return thumNail;
    }

    public void setThumNail(String thumNail) {
        this.thumNail = thumNail;
    }

    public boolean isHasLinks() {
        return hasLinks;
    }

    public void setHasLinks(boolean hasLinks) {
        this.hasLinks = hasLinks;
    }

    public int getNumTopComments() {
        return numTopComments;
    }

    public void setNumTopComments(int numTopComments) {
        this.numTopComments = numTopComments;
    }

    public boolean isSelf() {
        return isSelf;
    }

    public void setSelf(boolean isSelf) {
        this.isSelf = isSelf;
    }

    public int getUps() {
        return ups;
    }

    public void setUps(int ups) {
        this.ups = ups;
    }

    @Override
    public String toString() {
        return "com.pea.jay.redditlists.model.RedditPost [subreddit=" + subreddit + ", title=" + title + ", author=" + author + ", points=" + points
                + ", numComments=" + numComments + ", isSelf=" + isSelf + ", permalink=" + permalink + ", id=" + id
                + ", thumbNail=" + thumbNail + "," + " type=" + type + ", selfText=" + selfText
                + ", name=" + name + ", url=" + url + ", permaLink=" + permaLink + ", contestMode=" + contestMode
                + ", gilded=" + gilded + ", over18=" + over18 + ", score=" + score + ", downs=" + downs + ", ups=" + ups
                + ", upvoteRatio=" + upvoteRatio + ", createdUTC=" + createdUTC + ", postMediaEmbedded="
                + ", postIds=" + allCommentIds + "]";
    }

    public String getSubreddit() {
        return subreddit;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    public void setThumbNail(String thumbNail) {
        this.thumbNail = thumbNail;
    }

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getNumComments() {
        return numComments;
    }

    public void setNumComments(int numComments) {
        this.numComments = numComments;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSelfText() {
        return selfText;
    }

    public void setSelfText(String selfText) {
        this.selfText = selfText;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPermaLink() {
        return permaLink;
    }

    public void setPermaLink(String permaLink) {
        this.permaLink = permaLink;
    }

    public boolean isContestMode() {
        return contestMode;
    }

    public void setContestMode(boolean contestMode) {
        this.contestMode = contestMode;
    }

    public boolean isGilded() {
        return gilded;
    }

    public void setGilded(boolean gilded) {
        this.gilded = gilded;
    }

    public boolean isOver18() {
        return over18;
    }

    public void setOver18(boolean over18) {
        this.over18 = over18;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getDowns() {
        return downs;
    }

    public void setDowns(int downs) {
        this.downs = downs;
    }

    public double getUpvoteRatio() {
        return upvoteRatio;
    }

    public void setUpvoteRatio(double upvoteRatio) {
        this.upvoteRatio = upvoteRatio;
    }

    public long getCreatedUTC() {
        return createdUTC;
    }

    public void setCreatedUTC(long createdUTC) {
        this.createdUTC = createdUTC;
    }

    public ArrayList<String> getAllCommentIds() {
        return allCommentIds;
    }

    public void setAllCommentIds(ArrayList<String> allCommentIds) {
        this.allCommentIds = allCommentIds;
    }
}