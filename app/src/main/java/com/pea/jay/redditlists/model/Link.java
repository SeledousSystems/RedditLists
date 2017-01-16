package com.pea.jay.redditlists.model;



import java.io.Serializable;

public class Link implements Serializable{

    private String linkText = "";
    private String linkURL = "";
    private String urlHomePage = "";
    private String linkOriginal = "";
    private String thumbNail = "";
    private static final long serialVersionUID = 1;
    private int type = 0; //0 = link in reply to post, 1 = link in a reply to comment
    private Comment comment;
    RedditPost redditPost;

    public String getThumbNail() {
        return thumbNail;
    }

    public void setThumbNail(String thumbNail) {
        this.thumbNail = thumbNail;
    }

    public String getLinkOriginal() {
        return linkOriginal;
    }

    public void setLinkOriginal(String linkOriginal) {
        this.linkOriginal = linkOriginal;
    }

    public Link() {
    }

    public String getLinkText() {
        String trimmedLinkText = linkText;
        trimmedLinkText.replaceAll("/s","");
        trimmedLinkText.replaceAll("#s","");
        trimmedLinkText.replaceAll("&quot;","");
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    public String getLinkURL() {
        return linkURL;
    }

    public void setLinkURL(String linkURL) {
        this.linkURL = linkURL;
    }

    public String getUrlHomePage() {
        return urlHomePage;
    }

    public void setUrlHomePage(String urlHomePage) {
        this.urlHomePage = urlHomePage;
    }

    public Link(Comment comment) {
        this.comment = comment;
    }

    public Link(RedditPost redditPost) {
        this.redditPost = redditPost;

    }
}
