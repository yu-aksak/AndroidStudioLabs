package com.example.googlemap.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.googlemap.Classes.Post;
import com.example.googlemap.MainActivity;

import java.util.ArrayList;
import java.util.HashSet;

public class SQLightHelper implements Database{
    DatabaseHelper databaseHelper;

    public SQLightHelper(Context context){
        databaseHelper = new DatabaseHelper(context);
    }

    @Override
    public ArrayList<Post> readPosts() {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT " +
                 DatabaseHelper.DB_POSTS +
                 "." +
                 DatabaseHelper.COLUMN_IMAGE +
                 " , "+
                 DatabaseHelper.DB_POSTS +
                 "." +
                 DatabaseHelper.COLUMN_AUDIO +
                 " , "+
                 DatabaseHelper.DB_USERS +
                 "." +
                 DatabaseHelper.COLUMN_NAME +
                 " , "+
                 DatabaseHelper.DB_POSTS +
                 "." +
                 DatabaseHelper.COLUMN_TITLE +
                 " , "+
                 DatabaseHelper.DB_POSTS +
                 "." +
                 DatabaseHelper.COLUMN_DESCRIPTION +
                 " , "+
                 DatabaseHelper.DB_POSTS +
                 "." +
                 DatabaseHelper.COLUMN_DATE +
                 " , "+
                 DatabaseHelper.DB_POSTS +
                 "." +
                 DatabaseHelper.COLUMN_LOCATION +
                 " , "+
                 DatabaseHelper.DB_POSTS +
                 "." +
                 DatabaseHelper.COLUMN_COORDINATES1 +
                 " , "+
                 DatabaseHelper.DB_POSTS +
                 "." +
                 DatabaseHelper.COLUMN_COORDINATES2 +
                " FROM " +
                DatabaseHelper.DB_POSTS +
                " INNER JOIN " +
                DatabaseHelper.DB_USERS +
                " ON " +
                DatabaseHelper.DB_POSTS +
                "." +
                DatabaseHelper.COLUMN_FOREIGN_KEY +
                " = " +
                DatabaseHelper.DB_USERS +
                "." +
                DatabaseHelper.COLUMN_ID +
                ";", null);

        ArrayList<Post> posts = new ArrayList<>();

        cursor.moveToFirst();
        while (! cursor.isAfterLast()) {
            posts.add(new Post(cursor.getString(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5),
                    cursor.getString(6),new double[]{Double.parseDouble(cursor.
                    getString(7)), Double.parseDouble(cursor.getString(8))}));
            cursor.moveToNext();
        }

        cursor.close();
        return posts;
    }

    @Override
    public String readUser(String author) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT " +
                        DatabaseHelper.DB_USERS +
                        "." +
                        DatabaseHelper.COLUMN_ID +
                        " FROM " +
                        DatabaseHelper.DB_USERS +
                        " WHERE "+
                        DatabaseHelper.DB_USERS +
                        "." +
                        DatabaseHelper.COLUMN_NAME +
                        " = ?;", new String[]{author});

        if(!cursor.moveToFirst())
            return  "-1";

        cursor.moveToFirst();

        String id = String.valueOf(cursor.getInt(0));

        cursor.close();
        return id;
    }

    @Override
    public String readUser(String... user) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT " +
                        DatabaseHelper.DB_USERS +
                        "." +
                        DatabaseHelper.COLUMN_ID +
                        " FROM " +
                        DatabaseHelper.DB_USERS +
                        " WHERE "+
                        DatabaseHelper.DB_USERS +
                        "." +
                        DatabaseHelper.COLUMN_LOGIN + " = ? AND " +
                        DatabaseHelper.DB_USERS +
                        "." +
                        DatabaseHelper.COLUMN_PASSWORD +
                        " = ?;", new String[]{user[0], user[1]});

        if(!cursor.moveToFirst())
            return  "-1";

        cursor.moveToFirst();

        String id = String.valueOf(cursor.getInt(0));

        cursor.close();
        return id;
    }

    @Override
    public String checkUser(String login) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT " +
                        DatabaseHelper.DB_USERS +
                        "." +
                        DatabaseHelper.COLUMN_ID +
                        " FROM " +
                        DatabaseHelper.DB_USERS +
                        " WHERE "+
                        DatabaseHelper.DB_USERS +
                        "." +
                        DatabaseHelper.COLUMN_LOGIN +
                        " = ?;", new String[]{login});

        if(!cursor.moveToFirst())
            return "-1";

        cursor.moveToFirst();

        String id = String.valueOf(cursor.getInt(0));

        cursor.close();
        return id;
    }

    @Override
    public HashSet<String> readUsers() {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT " +
                        DatabaseHelper.DB_USERS +
                        "." +
                        DatabaseHelper.COLUMN_NAME +
                        " FROM " +
                        DatabaseHelper.DB_USERS, null);

        HashSet<String> authors = new HashSet<>();

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            authors.add(cursor.getString(0));
            cursor.moveToNext();
        }

        cursor.close();

        return authors;
    }

    public String readPost(Post post){
        String id = readUser(post.getAuthor());
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        String[] str = new String[] {post.getImgString(), post.getMscString(), post.getTitle(),
                post.getDescription(), post.getDate(), post.getLocation(),
                String.valueOf(post.getLatLng()[0]), String.valueOf(post.getLatLng()[1]),
                id};
        Cursor cursor = database.rawQuery(
                "SELECT " +
                        DatabaseHelper.COLUMN_ID +
                        " FROM " +
                        DatabaseHelper.DB_POSTS +
                        " WHERE " +
                        DatabaseHelper.COLUMN_IMAGE + " = ? AND " +
                        DatabaseHelper.COLUMN_AUDIO + " = ? AND " +
                        DatabaseHelper.COLUMN_TITLE + " = ? AND " +
                        DatabaseHelper.COLUMN_DESCRIPTION + " = ? AND " +
                        DatabaseHelper.COLUMN_DATE +" = ? AND " +
                        DatabaseHelper.COLUMN_LOCATION +" = ? AND " +
                        DatabaseHelper.COLUMN_COORDINATES1 +" = ? AND " +
                        DatabaseHelper.COLUMN_COORDINATES2 +" = ? AND " +
                        DatabaseHelper.COLUMN_FOREIGN_KEY + " = ?" +
                        ";", str);

        cursor.moveToFirst();
        id = String.valueOf(cursor.getInt(0));
        cursor.close();
        return id;
    }

    @Override
    public void writePost(Post post) {
        int id = Integer.parseInt(readUser(post.getAuthor()));
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        database.execSQL("INSERT INTO " +
                DatabaseHelper.DB_POSTS +
                " (" +
                DatabaseHelper.COLUMN_IMAGE +
                ", " +
                DatabaseHelper.COLUMN_AUDIO +
                ", " +
                DatabaseHelper.COLUMN_TITLE +
                ", " +
                DatabaseHelper.COLUMN_DESCRIPTION +
                ", " +
                DatabaseHelper.COLUMN_DATE +
                ", " +
                DatabaseHelper.COLUMN_LOCATION +
                ", " +
                DatabaseHelper.COLUMN_COORDINATES1 +
                ", " +
                DatabaseHelper.COLUMN_COORDINATES2 +
                ", " +
                DatabaseHelper.COLUMN_FOREIGN_KEY +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", new Object[] {post.getImgString(),
                post.getMscString(), post.getTitle(), post.getDescription(), post.getDate(),
                post.getLocation(), post.getLatLng()[0], post.getLatLng()[1], id});

    }

    @Override
    public void writeUser(String... args) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        database.execSQL("INSERT INTO " +
                DatabaseHelper.DB_USERS +
                " (" +
                DatabaseHelper.COLUMN_NAME +
                ", " +
                DatabaseHelper.COLUMN_LOGIN +
                ", " +
                DatabaseHelper.COLUMN_PASSWORD +
                ") VALUES (?, ?, ?)", new Object[] {args[0], args[1], args[2]});

    }

    @Override
    public void deletePost(Post post) {
        int id = Integer.parseInt(readPost(post));

        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        database.execSQL("DELETE FROM " +
                DatabaseHelper.DB_POSTS +
                " WHERE " +
                DatabaseHelper.COLUMN_ID + " = ? "+
                ";", new String[]{String.valueOf(id)});
    }

    @Override
    public void update(int pos, String... args) {
        Post post = MainActivity.imf.getContent().get(pos);

        int id = Integer.parseInt(readUser(post.getAuthor()));

        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        String s = "UPDATE " +
                DatabaseHelper.DB_POSTS +
                " SET " +
                DatabaseHelper.COLUMN_IMAGE +
                " = '" +
                args[0] +
                "', " +
                DatabaseHelper.COLUMN_AUDIO +
                " = '" +
                args[1] +
                "', " +
                DatabaseHelper.COLUMN_TITLE +
                " = '" +
                args[2] +
                "', " +
                DatabaseHelper.COLUMN_DESCRIPTION +
                " = '" +
                args[3] +
                "', " +
                DatabaseHelper.COLUMN_LOCATION +
                " = '" +
                args[4] +
                "', " +
                DatabaseHelper.COLUMN_COORDINATES1 +
                " = '" +
                args[5] +
                "', " +
                DatabaseHelper.COLUMN_COORDINATES2 +
                " = '" +
                args[6] +
                "'" +
                " WHERE " +
                DatabaseHelper.COLUMN_IMAGE +
                " = '" +
                post.getImgString() +
                "' and " +
                DatabaseHelper.COLUMN_AUDIO +
                " = '" +
                post.getMscString() +
                "' and " +
                DatabaseHelper.COLUMN_TITLE +
                " = '" +
                post.getTitle() +
                "' and " +
                DatabaseHelper.COLUMN_DESCRIPTION +
                " = '" +
                post.getDescription() +
                "' and " +
                DatabaseHelper.COLUMN_LOCATION +
                " = '" +
                post.getLocation() +
                "' and " +
                DatabaseHelper.COLUMN_COORDINATES1 +
                " = " +
                post.getLatLng()[0] +
                " and " +
                DatabaseHelper.COLUMN_COORDINATES1 +
                " = " +
                post.getLatLng()[1] +
                " and " +
                DatabaseHelper.COLUMN_FOREIGN_KEY +
                " = " +
                id +
                ";";
        database.execSQL(s, new Object[]{});
    }
}
