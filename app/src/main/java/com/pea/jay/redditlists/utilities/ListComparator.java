package com.pea.jay.redditlists.utilities;

import com.pea.jay.redditlists.model.RedditList;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Paul Wright on 30/01/2017.
 */
public class ListComparator {


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
