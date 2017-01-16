package com.pea.jay.redditlists.utilities;

import android.content.Context;
import android.util.Log;

import com.pea.jay.redditlists.R;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.pea.jay.redditlists.utilities.DialogManager.TAG;

/**
 * Created by Paul Wright on 24/12/2016.
 */
public class StringManager {
    private static Pattern redditLinkRegexPattern = Pattern.compile("\\[(.+?)\\]\\((.+?)\\)");
    private static final Pattern urlPattern = Pattern.compile(
            "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)" + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                    + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    public static String urlStart = "^(https?://www.|https?://|www.)";

    public static String generateHTMLCommentText(String rawCommentString) {
        String taggedString = replaceLinksWithTags(rawCommentString);
        taggedString = taggedString.replace("Spoiler", "<font color=\"#9c27b0\">SPOILER</font>");

        boolean firstBold = true;
        while (taggedString.contains("**")) {
            if (firstBold) {
                taggedString = taggedString.replaceFirst("\\*\\*", "<b>");
                firstBold = false;
            } else {
                taggedString = taggedString.replaceFirst("\\*\\*", "</b>");
                firstBold = true;
            }
        }
        boolean firstItalics = true;
        while (taggedString.contains("*")) {
            if (firstItalics) {
                taggedString = taggedString.replaceFirst("\\*", "<i>");
                firstItalics = false;
            } else {
                taggedString = taggedString.replaceFirst("\\*", "</i>");
                firstItalics = true;
            }
        }

        boolean firstItalics_2 = true;
        while (taggedString.contains("_")) {
            if (firstItalics_2) {
                taggedString = taggedString.replaceFirst("_", "<i>");
                firstItalics_2 = false;
            } else {
                taggedString = taggedString.replaceFirst("_", "</i>");
                firstItalics_2 = true;
            }
        }
        taggedString = removeRedditFormating(taggedString);
        return "<p>" + taggedString + "</p>";
    }

    public static CharSequence noTrailingwhiteLines(CharSequence text) {
        try {
            while (text.charAt(text.length() - 1) == '\n') {
                text = text.subSequence(0, text.length() - 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

    public static String replaceLinksWithTags(String commentText) {
        try {
            commentText = commentText.replaceAll("\n", "<br> ");
            Matcher m = redditLinkRegexPattern.matcher(commentText);
            while (m.find()) {
                if (commentText.contains("#spoiler") || commentText.contains("/spoiler")) {
                    commentText = commentText.replace(m.group(0), "Spoiler");
                } else if (commentText.contains("/s") || commentText.contains("#s")) {
                    commentText = commentText.replace(m.group(0), m.group(1));

                } else {
                    commentText = commentText.replace(m.group(0), "<font color=\"#2196F3\">" + m.group(1) + "</font>");
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        // Now create matcher object.
        Matcher m_URL = urlPattern.matcher(commentText);

        while (m_URL.find()) {
            commentText = commentText.replace(m_URL.group(0), "<font color=\"#2196F3\" href=\"" + m_URL.group(0) + "\">" + m_URL.group(0) + "</font>");
        }
        return commentText;
    }

    public static boolean checkStringIsRedditComment(String string) {
        return string.contains("reddit.com") && string.contains("/comments/") && string.contains("/r/");
    }

    public static String removeRedditFormating(String string) {
        String cleanString = string;
        cleanString = cleanString.replaceAll("\\\\~", "");
        cleanString = cleanString.replaceAll("\\\\*", "");
        cleanString = cleanString.replaceAll("~", "");
        cleanString = cleanString.replaceAll("\\^", "");
        cleanString = cleanString.replaceAll("\\'", "");
        cleanString = cleanString.replaceAll("\\_", "");
        cleanString = cleanString.replaceAll("&nbsp;", "");
        cleanString = cleanString.replaceAll("\\&gt;", "<u>Quote:</u> ");
        return cleanString;
    }

    public static String removeURLstart(String url) {
        return url.replaceFirst(urlStart, "");
    }

    public static String cleanNewSpoilers(String spoilerString) {
        String trimmedLinkText = spoilerString;
        trimmedLinkText.replaceAll("/s", "");
        trimmedLinkText.replaceAll("#s", "");
        trimmedLinkText.replaceAll("&quot;", "");
        return trimmedLinkText;
    }

    public static String getRandomString(Context context) {
        Random r = new Random();
        String[] colorArray = context.getResources().getStringArray(R.array.colors);
        int i = r.nextInt(colorArray.length);
        return colorArray[i];
    }
}
