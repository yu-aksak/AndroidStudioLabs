package com.example.googlemap.DB;

import com.example.googlemap.Classes.Post;

import java.util.ArrayList;
import java.util.HashSet;

public interface Database {
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
