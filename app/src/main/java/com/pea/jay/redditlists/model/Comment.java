package com.pea.jay.redditlists.model;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Comment implements Serializable {

    private String body;
    private String name;
    private String parentId;
    private String author;
    private String apiId;
    private String linkId;
    private int score;
    private int downs;
    private int ups;
    private int controversity;
    private long createdDateTime;
    private String kind;
    private RedditPost post;
    private int treeDepth;
    private Link link;
    private ArrayList<Link> links;
    private ArrayList<Link> repliedLinks;
    private ArrayList<String> spoilers = new ArrayList<>();
    private boolean hasLink;
    private static final long serialVersionUID = 1;
    private String colorString = "white";

    public ArrayList<String> getSpoilers() {
        return spoilers;
    }

    public void setSpoilers(ArrayList<String> spoilers) {
        this.spoilers = spoilers;
    }

    public String getColorString() {
        return colorString;
    }

    public void setColorString(String colorString) {
        this.colorString = colorString;
    }

    public Comment() {}

    public Comment(RedditPost redditPost) {
    }

    public ArrayList<Link> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<Link> links) {
        this.links = links;
        if (links.size() > 0) {
            this.setHasLink(true);
        } else {
            this.setHasLink(false);
        }
    }

    public ArrayList<Link> getRepliedLinks() {
        return repliedLinks;
    }

    public void setRepliedLinks(ArrayList<Link> repliedLinks) {
        this.repliedLinks = repliedLinks;
    }

    public boolean isHasLink() {
        return hasLink;
    }

    public void setHasLink(boolean hasLink) {
        this.hasLink = hasLink;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }

    public int getTreeDepth() {
        return treeDepth;
    }

    public void setTreeDepth(int treeDepth) {
        this.treeDepth = treeDepth;
    }

    public String getApiId() {
        return apiId;
    }

    public RedditPost getPost() {
        return post;
    }

    public void setPost(RedditPost post) {
        this.post = post;
    }

    @Override
    public String toString() {
        return "com.pea.jay.redditlists.model.Comment [body=" + body + ", name=" + name + ", parentID=" + parentId + ", author=" + author + ", iD="
                + apiId + ", linkId=" + linkId + ", score=" + score + ", downs=" + downs + ", ups=" + ups
                + ", controversity=" + controversity + ", createdDateTime=" + createdDateTime + ", link=" + link
                + ", kind=" + kind + "]";
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getid() {
        return apiId;
    }

    public void setApiId(String id) {
        this.apiId = apiId;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
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

    public int getUps() {
        return ups;
    }

    public void setUps(int ups) {
        this.ups = ups;
    }

    public int getControversity() {
        return controversity;
    }

    public void setControversity(int controversity) {
        this.controversity = controversity;
    }

    public long getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(long createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

}
