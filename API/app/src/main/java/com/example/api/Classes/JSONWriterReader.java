package com.example.api.Classes;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Dictionary;

public class JSONWriterReader {
    private static final String FILE_DATA = "data.json";

    public static boolean exportToJSON(Context context, ArrayList<Post> dataList) {

        Gson gson = new Gson();
        DataItems dataItems = new DataItems();
        dataItems.setPosts(dataList);
        String jsonData = gson.toJson(dataItems);
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = context.openFileOutput(FILE_DATA, Context.MODE_PRIVATE);
            fileOutputStream.write(jsonData.getBytes());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    public static ArrayList<Post> importFromJSON(Context context) {

        InputStreamReader streamReader = null;
        FileInputStream fileInputStream = null;
        try{
            fileInputStream = context.openFileInput(FILE_DATA);
            streamReader = new InputStreamReader(fileInputStream);
            Gson gson = new Gson();
            BufferedReader bufferedReader = new BufferedReader(streamReader);

            DataItems dataItems = gson.fromJson(bufferedReader, DataItems.class);

            return  dataItems.getPosts();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        finally {
            if (streamReader != null) {
                try {
                    streamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    private static class DataItems {
        private ArrayList<Post> posts;

        ArrayList<Post> getPosts() {
            return posts;
        }
        void setPosts(ArrayList<Post> posts) {
            this.posts = posts;
        }
    }
}
