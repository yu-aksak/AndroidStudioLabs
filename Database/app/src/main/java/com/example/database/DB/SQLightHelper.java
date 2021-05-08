package com.example.database.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.database.Classes.Post;
import com.example.database.MainActivity;

import java.util.ArrayList;
import java.util.HashSet;

public class SQLightHelper implements DatabaseHelper {
    Database database;

    public SQLightHelper(Context context){
        database = new Database(context);
    }

    @Override
    public ArrayList<Post> readPosts() {
        SQLiteDatabase database = this.database.getReadableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT " +
                 Database.DB_POSTS +
                 "." +
                 Database.COLUMN_IMAGE +
                 " , "+
                 Database.DB_POSTS +
                 "." +
                 Database.COLUMN_AUDIO +
                 " , "+
                 Database.DB_USERS +
                 "." +
                 Database.COLUMN_NAME +
                 " , "+
                 Database.DB_POSTS +
                 "." +
                 Database.COLUMN_TITLE +
                 " , "+
                 Database.DB_POSTS +
                 "." +
                 Database.COLUMN_DESCRIPTION +
                 " , "+
                 Database.DB_POSTS +
                 "." +
                 Database.COLUMN_DATE +
                " FROM " +
                Database.DB_POSTS +
                " INNER JOIN " +
                Database.DB_USERS +
                " ON " +
                Database.DB_POSTS +
                "." +
                Database.COLUMN_FOREIGN_KEY +
                " = " +
                Database.DB_USERS +
                "." +
                Database.COLUMN_ID +
                ";", null);

        ArrayList<Post> posts = new ArrayList<>();

        cursor.moveToFirst();
        while (! cursor.isAfterLast()) {
            posts.add(new Post(cursor.getString(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5)));
            cursor.moveToNext();
        }

        cursor.close();
        return posts;
    }

    @Override
    public String readUser(String author) {
        SQLiteDatabase database = this.database.getReadableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT " +
                        Database.DB_USERS +
                        "." +
                        Database.COLUMN_ID +
                        " FROM " +
                        Database.DB_USERS +
                        " WHERE "+
                        Database.DB_USERS +
                        "." +
                        Database.COLUMN_NAME +
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
        SQLiteDatabase database = this.database.getReadableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT " +
                        Database.DB_USERS +
                        "." +
                        Database.COLUMN_ID +
                        " FROM " +
                        Database.DB_USERS +
                        " WHERE "+
                        Database.DB_USERS +
                        "." +
                        Database.COLUMN_LOGIN + " = ? AND " +
                        Database.DB_USERS +
                        "." +
                        Database.COLUMN_PASSWORD +
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
        SQLiteDatabase database = this.database.getReadableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT " +
                        Database.DB_USERS +
                        "." +
                        Database.COLUMN_ID +
                        " FROM " +
                        Database.DB_USERS +
                        " WHERE "+
                        Database.DB_USERS +
                        "." +
                        Database.COLUMN_LOGIN +
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
        SQLiteDatabase database = this.database.getReadableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT " +
                        Database.DB_USERS +
                        "." +
                        Database.COLUMN_NAME +
                        " FROM " +
                        Database.DB_USERS, null);

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
        SQLiteDatabase database = this.database.getReadableDatabase();
        String[] str = new String[] {post.getImgString(), post.getMscString(), post.getTitle(),
                post.getDescription(), post.getDate(),
                id};
        Cursor cursor = database.rawQuery(
                "SELECT " +
                        Database.COLUMN_ID +
                        " FROM " +
                        Database.DB_POSTS +
                        " WHERE " +
                        Database.COLUMN_IMAGE + " = ? AND " +
                        Database.COLUMN_AUDIO + " = ? AND " +
                        Database.COLUMN_TITLE + " = ? AND " +
                        Database.COLUMN_DESCRIPTION + " = ? AND " +
                        Database.COLUMN_DATE +" = ? AND " +
                        Database.COLUMN_FOREIGN_KEY + " = ?" +
                        ";", str);

        cursor.moveToFirst();
        id = String.valueOf(cursor.getInt(0));
        cursor.close();
        return id;
    }

    @Override
    public void writePost(Post post) {
        int id = Integer.parseInt(readUser(post.getAuthor()));
        SQLiteDatabase database = this.database.getWritableDatabase();
        database.execSQL("INSERT INTO " +
                Database.DB_POSTS +
                " (" +
                Database.COLUMN_IMAGE +
                ", " +
                Database.COLUMN_AUDIO +
                ", " +
                Database.COLUMN_TITLE +
                ", " +
                Database.COLUMN_DESCRIPTION +
                ", " +
                Database.COLUMN_DATE +
                ", " +
                Database.COLUMN_FOREIGN_KEY +
                ") VALUES (?, ?, ?, ?, ?, ?)", new Object[] {post.getImgString(),
                post.getMscString(), post.getTitle(), post.getDescription(), post.getDate(), id});

    }

    @Override
    public void writeUser(String... args) {
        SQLiteDatabase database = this.database.getWritableDatabase();
        database.execSQL("INSERT INTO " +
                Database.DB_USERS +
                " (" +
                Database.COLUMN_NAME +
                ", " +
                Database.COLUMN_LOGIN +
                ", " +
                Database.COLUMN_PASSWORD +
                ") VALUES (?, ?, ?)", new Object[] {args[0], args[1], args[2]});

    }

    @Override
    public void deletePost(Post post) {
        int id = Integer.parseInt(readPost(post));

        SQLiteDatabase database = this.database.getWritableDatabase();

        database.execSQL("DELETE FROM " +
                Database.DB_POSTS +
                " WHERE " +
                Database.COLUMN_ID + " = ? "+
                ";", new String[]{String.valueOf(id)});
    }

    @Override
    public void update(int pos, String... args) {
        Post post = MainActivity.imf.getContent().get(pos);

        int id = Integer.parseInt(readUser(post.getAuthor()));

        SQLiteDatabase database = this.database.getWritableDatabase();
        String s = "UPDATE " +
                Database.DB_POSTS +
                " SET " +
                Database.COLUMN_IMAGE +
                " = '" +
                args[0] +
                "', " +
                Database.COLUMN_AUDIO +
                " = '" +
                args[1] +
                "', " +
                Database.COLUMN_TITLE +
                " = '" +
                args[2] +
                "', " +
                Database.COLUMN_DESCRIPTION +
                " = '" +
                args[3] +
                "'" +
                " WHERE " +
                Database.COLUMN_IMAGE +
                " = '" +
                post.getImgString() +
                "' and " +
                Database.COLUMN_AUDIO +
                " = '" +
                post.getMscString() +
                "' and " +
                Database.COLUMN_TITLE +
                " = '" +
                post.getTitle() +
                "' and " +
                Database.COLUMN_DESCRIPTION +
                " = '" +
                post.getDescription() +
                "' and " +
                Database.COLUMN_FOREIGN_KEY +
                " = " +
                id +
                ";";
        database.execSQL(s, new Object[]{});
    }
}
