package com.example.database.DB;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "data.db";

    private static final int SCHEMA = 2;
    public static final String DB_POSTS = "post";
    public static final String DB_USERS = "user";

    public static final String COLUMN_ID = "_id";

    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_AUDIO = "audio";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DATE = "_date";
    public static final String COLUMN_FOREIGN_KEY = "id_user";


    public static final String COLUMN_NAME = "_name";
    public static final String COLUMN_LOGIN = "_login";
    public static final String COLUMN_PASSWORD = "_password";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DB_USERS + " (" +
                COLUMN_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME +
                " TEXT NOT NULL, " +
                COLUMN_LOGIN +
                " TEXT," +
                COLUMN_PASSWORD +
                " TEXT " +
                ");");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DB_POSTS + " (" +
                COLUMN_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_IMAGE +
                " TEXT, " +
                COLUMN_AUDIO +
                " TEXT," +
                COLUMN_TITLE +
                " TEXT, " +
                COLUMN_DESCRIPTION +
                " TEXT, " +
                COLUMN_DATE +
                " TEXT, " +
                COLUMN_FOREIGN_KEY +
                " INTEGER NOT NULL, " +
                "FOREIGN KEY (" +
                COLUMN_FOREIGN_KEY +
                ") REFERENCES " +
                DB_USERS +
                "(" +
                COLUMN_ID +
                "));");

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery("SELECT " +
                Database.DB_USERS +
                "." +
                Database.COLUMN_ID +
                " FROM " +
                Database.DB_USERS +
                " WHERE "+
                Database.DB_USERS +
                "." +
                Database.COLUMN_NAME +
                " = ?;", new String[]{"admin"});

        if(!cursor.moveToFirst()) {
            db.execSQL("INSERT INTO " +
                    Database.DB_USERS +
                    " (" +
                    Database.COLUMN_NAME +
                    ", " +
                    Database.COLUMN_LOGIN +
                    ", " +
                    Database.COLUMN_PASSWORD +
                    ") VALUES (?, ?, ?)", new Object[]{"admin", "admin", "admin"});
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ DB_POSTS);
        db.execSQL("DROP TABLE IF EXISTS "+ DB_USERS);
        onCreate(db);
    }

}
