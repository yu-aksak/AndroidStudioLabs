package com.example.googlemap.Classes;

import android.os.AsyncTask;

import com.example.googlemap.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class PostVK extends AsyncTask<Void, Void, JSONObject> {
    private final boolean isEmpty;

    public PostVK(boolean isEmpty){
        this.isEmpty = isEmpty;
    }

    @Override
    protected JSONObject doInBackground(Void... integers) {
        JSONObject response = null;
        try {
            String METHOD = "newsfeed.getRecommended";
            String VERSION = "5.130";
            String MAX_PHOTOS = "1";
            String COUNT = "10";
            if(isEmpty) {
                URL request = new URL("https://api.vk.com/method/" + METHOD + "?access_token="
                        + MainActivity.vkToken + "&v=" + VERSION + "&max_photos=" + MAX_PHOTOS +
                        "&count=" + COUNT);
                Scanner scanner = new Scanner(request.openStream());
                response = new JSONObject(scanner.useDelimiter("\\Z").next());
                MainActivity.databaseHelper.writeUser("Пост из вк", null, null);
            }
            URL re = new URL("https://api.vk.com/method/" + "account.getProfileInfo" +
                    "?access_token=" + MainActivity.vkToken + "&v=" + VERSION);
            Scanner scanner1 = new Scanner(re.openStream());
            JSONObject res = new JSONObject(scanner1.useDelimiter("\\Z").next());
            MainActivity.imf.author = res.getJSONObject("response").getString("first_name") +
                    " " + res.getJSONObject("response").getString("last_name");
            if(MainActivity.databaseHelper.readUser(MainActivity.imf.author).equals("-1"))
                MainActivity.databaseHelper.writeUser(MainActivity.imf.author, "", "");
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return response;
    }



    @Override
    protected void onPostExecute(JSONObject result) {
        if (result != null) {
            try {
                JSONObject response = result.getJSONObject("response");
                JSONArray items = response.getJSONArray("items");
                for (int i = 0; i < items.length(); i++) {
                    JSONObject obj = items.getJSONObject(i);
                    String image = null;
                    String text = obj.getString("text");
                    try {
                        JSONArray attachments = obj.getJSONArray("attachments");
                        for (int j = 0; j < attachments.length(); j++) {
                            if (attachments.getJSONObject(j).getString("type").
                                    equals("photo")) {
                                JSONArray sizes = attachments.getJSONObject(j).
                                        getJSONObject("photo").getJSONArray("sizes");
                                image = sizes.getJSONObject(sizes.length() - 1).
                                        getString("url");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    MainActivity.imf.AddContent(new Post(image, "", "Пост из вк",
                            "", text, MainActivity.imf.getDate(), "Австралия, Сидней", new double[]{-34,151}));
                }
                MainActivity.imf.Add(MainActivity.imf.getContent());
            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println(result);
            }
        }
    }
}