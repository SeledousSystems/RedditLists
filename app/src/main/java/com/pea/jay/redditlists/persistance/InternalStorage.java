package com.pea.jay.redditlists.persistance;

import android.content.Context;

import com.pea.jay.redditlists.model.RedditList;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public final class InternalStorage {

    private static ArrayList<String> redditListIDS = new ArrayList<>();
    private static String IDs = "redditListIDS";
    private static String ArrayListKey = "RedditListKey";
    private static ArrayList<RedditList> redditLists = new ArrayList<>();
    private static String TAG = "InternalStorage";

    private InternalStorage() {
    }

    public static boolean testObjectExists(Context context, String key) {
        boolean exists = true;
        try {
            readObject(context, key);
        } catch (IOException e) {
            e.printStackTrace();
            exists = false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            exists = false;
        }
        return exists;
    }

    public static boolean setupArrayList(Context context) {
        if (!testObjectExists(context, IDs)) {
            try {
                writeObject(context, IDs, redditListIDS);
            } catch (IOException ioe) {
                ioe.printStackTrace();
                //failure return false
                return false;
            }
        }
        //arraylist exists return true
        return true;
    }


    public static boolean writeList(Context context, ArrayList<RedditList> redditListArray) {

//        boolean writing = false;
//
//        while(writing){
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        writing = true;

        try {
            //write the object to storage, overwriting the old version the string value of the millis of created date is the Key
            writeObject(context, ArrayListKey, redditListArray);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            //failure return false
            return false;
        }
        // unblock for next thread
        //writing = false;
        //success return true
        return true;
    }


    public static ArrayList<RedditList> getAllLists(Context context) {
        redditLists = new ArrayList<>();

        testObjectExists(context, ArrayListKey);
        final Context finContext = context;

        try {
            redditLists = (ArrayList<RedditList>) readObject(finContext, ArrayListKey);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        //return the arrayList of objects
        return redditLists;
    }

    private static void writeObject(Context context, String key, Object object) throws IOException {
        FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(object);
        oos.close();
        fos.close();
    }

    private static Object readObject(Context context, String key) throws IOException,
            ClassNotFoundException {
        FileInputStream fis = context.openFileInput(key);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object object = ois.readObject();
        return object;
    }

    private static boolean deleteListInternal(Context context, String key) throws IOException,
            ClassNotFoundException {
        return context.deleteFile(key);
    }


}