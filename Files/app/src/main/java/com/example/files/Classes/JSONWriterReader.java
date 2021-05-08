package com.example.files.Classes;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class JSONWriterReader {
    private static final String FILE_NAME = "data.json";

    private static final String ALGORITHM_AES = "AES";
    private static final String ALGORITHM_BLOWFISH = "Blowfish";

    public static boolean exportToJSON(Context context, ArrayList<Post> dataList) {

        Gson gson = new Gson();
        DataItems dataItems = new DataItems();
        dataItems.setPosts(dataList);
        String jsonString = gson.toJson(dataItems);

        try {
            Crypt.GenerateKeyPair(context, ALGORITHM_AES);
        } catch (Exception e) {
            e.printStackTrace();
        }

        jsonString = Crypt.Encrypt(context,3, jsonString);

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fileOutputStream.write(jsonString.getBytes());
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
            fileInputStream = context.openFileInput(FILE_NAME);
            streamReader = new InputStreamReader(fileInputStream);
            Gson gson = new Gson();
           BufferedReader bufferedReader = new BufferedReader(streamReader);
           StringBuilder jsonString = new StringBuilder();
           String line;
            while ((line = bufferedReader.readLine()) != null)
                jsonString.append(line);
            jsonString = new StringBuilder(Crypt.Decrypt(context, 3, jsonString.toString()));
            DataItems dataItems = gson.fromJson(jsonString.toString(), DataItems.class);
            System.out.println(gson.fromJson(jsonString.toString(), DataItems.class));
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
