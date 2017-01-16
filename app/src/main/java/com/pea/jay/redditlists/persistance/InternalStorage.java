package com.pea.jay.redditlists.persistance;

import android.content.Context;
import android.util.Log;

import com.pea.jay.redditlists.model.RedditList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public final class InternalStorage {

    private static ArrayList<String> redditListIDS = new ArrayList<>();
    private static String IDs = "redditListIDS";
    private static String ArrayListKey = "RedditListKey";
    private static ArrayList<RedditList> redditLists = new ArrayList<>();

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
        Log.d(TAG, "csetUp arraylist");
        if (!testObjectExists(context, IDs)) {
            try {
                Log.d(TAG, "creating list IDs");
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

//    public void updateList(final Context context, final RedditList redditList) {
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                updateListInternal(context, redditList);
//
//            }
//        }).run();
//
//    }
//
//    public void getAllLists(final Context context) {
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                deleteList(context);
//
//            }
//        }).run();
//
//    }

//    public void deleteList(final Context context, final RedditList redditList) {
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                getAllListsInternal(context);
//
//            }
//        }).run();
//
//    }

//    public static boolean updateListInternal(Context context, RedditList redditList) {
//        try {
//            //write the object to storage, overrighting the old version the string value of the millis of created date is the Key
//            writeObject(context, String.valueOf(redditList.getCreated().getMillis()), redditList);
//        } catch (IOException ioe) {
//            ioe.printStackTrace();
//            //failure return false
//            return false;
//        }
//        //success return true
//        return true;
//    }
//
//    public static boolean writeList(Context context, RedditList redditList) {
//        try {
//            //get the id Array for holding the ids of the reddit list objects
//            redditListIDS = (ArrayList<String>) readObject(context, IDs);
//            //write the object to storage the string value of the millis of created date is the Key
//            deleteListInternal(context, String.valueOf(redditList.getCreated().getMillis()) );
//            writeObject(context, String.valueOf(redditList.getCreated().getMillis()), redditList);
//            //write the key to the arraylist and store it if it doesn exist, else just overwriting the object e.g. update
//            if(!redditListIDS.contains(redditList.getCreated().getMillis())){
//                Log.d(TAG, redditList.getCreated().getMillis() + " XXXXXXXXXXXXXXXXX");
//            redditListIDS.add(String.valueOf(redditList.getCreated().getMillis())); }
//
//            deleteListInternal(context, IDs);
//            writeObject(context, IDs, redditListIDS);
//        } catch (IOException ioe) {
//            ioe.printStackTrace();
//            //failure return false
//            return false;
//        } catch (ClassNotFoundException cnfe) {
//            cnfe.printStackTrace();
//            //failure return false
//            return false;
//        }
//        //success return true
//        return true;
//    }
//
//    public static boolean deleteList(Context context, RedditList list) {
//        redditLists = new ArrayList<>();
//        String key = String.valueOf(list.getCreated().getMillis());
//        try {
//            //get the id Array for holding the ids of the reddit list objects
//            redditListIDS = (ArrayList<String>) readObject(context, IDs);
//            //remove the subject key
//            redditListIDS.remove(key);
//            //store the key ArrayList
//            writeObject(context, IDs, redditListIDS);
//            //delete the list from storage
//            deleteListInternal(context, key);
//        } catch (IOException ioe) {
//            ioe.printStackTrace();
//            //failure return false
//            return false;
//        } catch (ClassNotFoundException cnfe) {
//            cnfe.printStackTrace();
//            //failure return false
//            return false;
//        }
//        //retunr true, sucess
//        return true;
//    }
//
//    public static ArrayList<RedditList> getAllLists(Context context) {
//        redditLists = new ArrayList<>();
//
//        try {
//            //get the id Array for holding the ids of the reddit list objects
//            redditListIDS = (ArrayList<String>) readObject(context, IDs);
//            //iterate through the list getting all the objects using the keys
//            for (String s : redditListIDS) {
//                Log.d(TAG, s);
//                if (testObjectExists(context, s)) {
//                    Log.d(TAG, s + " exists ");
//                    redditLists.add((RedditList) readObject(context, s));
//                }
//            }
//        } catch (IOException ioe) {
//            ioe.printStackTrace();
//            //failure return false
//            return null;
//        } catch (ClassNotFoundException cnfe) {
//            cnfe.printStackTrace();
//            //failure return false
//            return null;
//        }
//        Log.d(TAG, redditListIDS.size() + "   " + redditLists.size());
//        //return the arrayList of objects
//        return redditLists;
//    }


    public static boolean writeList(Context context, ArrayList<RedditList> redditListArray) {

        boolean writing = false;

        while(writing){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        writing = true;

        try {
            //write the object to storage, overwriting the old version the string value of the millis of created date is the Key
            writeObject(context, ArrayListKey, redditListArray);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            //failure return false
            return false;
        }
        // unblock for next thread
        writing = false;
        //success return true
        return true;
    }


    public static ArrayList<RedditList> getAllLists(Context context) {
        redditLists = new ArrayList<>();
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
        File dir = context.getFilesDir();
        return context.deleteFile(key);
    }


}