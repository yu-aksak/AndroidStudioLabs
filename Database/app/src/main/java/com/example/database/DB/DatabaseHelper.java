package com.example.database.DB;

import com.example.database.Classes.Post;

import java.util.ArrayList;
import java.util.HashSet;

public interface DatabaseHelper {
    ArrayList<Post> readPosts();
    String readUser(String author);
    String readUser(String... user);
    String checkUser(String login);
    HashSet<String> readUsers();
    void writePost(Post post);
    void writeUser(String... args);
    void deletePost(Post post);
    void update(int pos, String... args);
}
