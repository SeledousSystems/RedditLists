package com.pea.jay.redditlists.model;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkFactory {

    private static Pattern redditLinkRegexPattern = Pattern.compile("\\[(.+?)\\]\\((.+?)\\)");
    private static final Pattern urlPattern = Pattern.compile(
            "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)" + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                    + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);


    public static ArrayList<Link> buildLinkArray(String commentText) {
        String trimmedComment = commentText;
        ArrayList<Link> linkAL = new ArrayList<>();
        try {
            Matcher m = redditLinkRegexPattern.matcher(commentText);

            while (m.find()) {
                System.out.println(" group count" + m.groupCount());
                if (m.groupCount() > 1) {
                    Link link = new Link();
                    trimmedComment = trimmedComment.replace(m.group(0), "");
                    link.setLinkOriginal(m.group(0));
                    link.setLinkText(m.group(1));
                    link.setLinkURL(m.group(2));
                    linkAL.add(link);
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }

        // Now create matcher object.
        Matcher m_URL = urlPattern.matcher(trimmedComment);

        while (m_URL.find()) {
            Link link = new Link();
            link.setLinkOriginal("");
            link.setLinkText("");
            link.setLinkURL(m_URL.group(0));
            linkAL.add(link);
        }
        return linkAL;
    }
}
