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
    private static final Pattern urlPattern = Pattern.compile(
            "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)" + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                    + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
    public static String urlStart = "^(https?://www.|https?://|www.)";
    private static Pattern redditLinkRegexPattern = Pattern.compile("\\[(.+?)\\]\\((.+?)\\)");
    private static Pattern redditLinkRegexPattern_withOptSpace = Pattern.compile("\\[(.+?)\\](\\s+)\\((.+?)\\)");
    private static Pattern redditLinkRegexPattern_RC = Pattern.compile("\\](\\\\s*)<br> (\\\\s*)\\(");

    public static String generateHTMLCommentText(String rawCommentString) {
        Log.d(TAG, " Raw  = " + rawCommentString);
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

        boolean firstStrike = true;
        while (taggedString.contains("~~")) {
            if (firstStrike) {
                taggedString = taggedString.replaceFirst("~~", "<strike>");
                firstStrike = false;
            } else {
                taggedString = taggedString.replaceFirst("~~", "</strike>");
                firstStrike = true;
            }
        }

//  removed due to corrupting links
//        boolean firstItalics_2 = true;
//        while (taggedString.contains("_")) {
//            if (firstItalics_2) {
//                taggedString = taggedString.replaceFirst("_", "<i>");
//                firstItalics_2 = false;
//            } else {
//                taggedString = taggedString.replaceFirst("_", "</i>");
//                firstItalics_2 = true;
//            }
//        }
        //taggedString = removeRedditFormating(taggedString);
        return "<p>" + taggedString + "</p>";
    }

    public static CharSequence noTrailingwhiteLines(CharSequence text) {

        if (text.length() > 0) {
            try {
                while (text.charAt(text.length() - 1) == '\n') {
                    text = text.subSequence(0, text.length() - 1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return text;
    }

    public static String replaceLinksWithTags(String commentText) {
        try {
            Log.d(TAG, "comment textA = " + commentText);
            //commentText = commentText.replace("]\\r?\\n(", "](");
            while (commentText.contains("](\\s*)\\r?\\n(\\s*)(")) {
                commentText = commentText.replace("](\\s*)\r(\\s*)", "]");
                Log.d(TAG, "comment newline = " + commentText);
            }
            commentText = commentText.replaceAll("\n", "<br> ");
            Log.d(TAG, "comment textA2 = " + commentText);

            //test to see if space between the [] and ()
            Matcher m_2 = redditLinkRegexPattern_RC.matcher(commentText);
            while (m_2.find()) {
                String removeRC = m_2.group(0).replace("](\\s*)<br> (\\s*)(", "](");
                Log.d(TAG, "removeRC = " + removeRC);
                commentText = commentText.replace(m_2.group(0), removeRC);
                Log.d(TAG, "comment textA = " + commentText);
            }

            //test to see if space between the [] and ()
            Matcher m_1 = redditLinkRegexPattern_withOptSpace.matcher(commentText);
            while (m_1.find()) {
                String removeSpace = m_1.group(0).replace("] (", "](");
                Log.d(TAG, "removeSpace = " + removeSpace);
                commentText = commentText.replace(m_1.group(0), removeSpace);
                Log.d(TAG, "comment textA = " + commentText);
            }

            Matcher m = redditLinkRegexPattern.matcher(commentText);
            while (m.find()) {
                if (commentText.contains("#spoiler") || commentText.contains("/spoiler")) {
                    commentText = commentText.replace(m.group(0), "Spoiler");

                } else if (commentText.contains("/s") || commentText.contains("#s")) {
                    commentText = commentText.replace(m.group(0), m.group(1));

                } else {
                    //commentText = commentText.replace(m.group(0), "<font color=\"#2196F3\">" + m.group(1) + "</font>");
                    commentText = commentText.replace(m.group(0), " <a href=\"" + m.group(2) + "\">" + m.group(1) + "</a>");
                    Log.d(TAG, "comment text1 = " + commentText);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Now create matcher object.
        Matcher m_URL = urlPattern.matcher(commentText);

        while (m_URL.find()) {
            int startPos = commentText.indexOf(m_URL.group(0));

            if (startPos > 9)
                Log.d(TAG, commentText.substring(startPos - 8, startPos) + "    <a href=");

            if (startPos < 9 || !commentText.substring(startPos - 8, startPos).equals("<a href=")) {
                //commentText = commentText.replace(m_URL.group(0), "<font color=\"#2196F3\" href=\"" + m_URL.group(0) + "\">" + m_URL.group(0) + "</font>");
                String hRef_display = m_URL.group(0);
                if (m_URL.group(0).substring(0, 1).equals(" "))
                    hRef_display = m_URL.group(0).substring(1, hRef_display.length());
                commentText = commentText.replace(m_URL.group(0), " <a href=\"" + m_URL.group(0).replaceAll("\\s+", "") + "\">" + hRef_display + "</a>");
                Log.d(TAG, "comment text2 = " + commentText);
                commentText = commentText.replace("<a href=\"www.", "<a href=\"https://");
                commentText = commentText.replace("<a href=\"http://", "<a href=\"https://");
                Log.d(TAG, "comment text3 = " + commentText);
            }
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
