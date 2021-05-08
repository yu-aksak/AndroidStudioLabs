package com.example.googlemap.DB;

import androidx.annotation.NonNull;

import com.example.googlemap.Classes.Post;
import com.example.googlemap.Classes.PostFirebase;
import com.example.googlemap.Classes.UserFirebase;
import com.example.googlemap.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class FirebaseHelper implements Database{

    DataSnapshot userSnapshot;
    DataSnapshot postSnapshot;

    public FirebaseHelper(){
        Init();
    }

    private void Init(){
        DatabaseReference userDatabase = FirebaseDatabase.getInstance().
                getReference(DatabaseHelper.DB_USERS);
        ValueEventListener userValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userSnapshot = dataSnapshot;
            }

            @Override public void onCancelled(@NonNull DatabaseError databaseError) { }
        };
        userDatabase.addValueEventListener(userValueEventListener);
        DatabaseReference PostDatabase = FirebaseDatabase.getInstance().
                getReference(DatabaseHelper.DB_POSTS);
        ValueEventListener postValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postSnapshot = dataSnapshot;
            }

            @Override public void onCancelled(@NonNull DatabaseError databaseError) { }
        };
        PostDatabase.addValueEventListener(postValueEventListener);
    }

    public ArrayList<Post> readPosts() {
        ArrayList<Post> posts = new ArrayList<>();
        for (DataSnapshot dataSnapshot : postSnapshot.getChildren()) {
            PostFirebase postFirebase = dataSnapshot.getValue(PostFirebase.class);
            assert postFirebase != null;
            posts.add(new Post(postFirebase.getImage(), postFirebase.getAudio(),
                    getUser(postFirebase.getForeignKey()), postFirebase.getTitle(),
                    postFirebase.getDescription(), postFirebase.getDate(),
                    postFirebase.getLocation(), new double[]{postFirebase.getCoordinates1(),
                    postFirebase.getCoordinates2()}));
        }
        Init();
        return posts;
    }

    public String readPost(String... args){
        for (DataSnapshot dataSnapshot : postSnapshot.getChildren()) {
            PostFirebase postFirebase = dataSnapshot.getValue(PostFirebase.class);
            assert postFirebase != null;
            if(postFirebase.getImage().equals(args[0]) && postFirebase.getAudio().equals(args[1]) &&
                postFirebase.getTitle().equals(args[2]) && postFirebase.getDescription().
                    equals(args[3]) && postFirebase.getDate().equals(args[4]) &&
                    postFirebase.getLocation().equals(args[5])&&
                    String.valueOf(postFirebase.getCoordinates1()).equals(args[6])&&
                    String.valueOf(postFirebase.getCoordinates2()).equals(args[7]) && postFirebase.getForeignKey().
                    equals(args[8]))
                return dataSnapshot.getKey();
        }
        Init();
        return "-1";
    }

    public String readUser(String author) {
        for (DataSnapshot dataSnapshot : userSnapshot.getChildren()) {
            UserFirebase userFirebase = dataSnapshot.getValue(UserFirebase.class);
            assert userFirebase != null;
            if (userFirebase.getName().equals(author))
                return dataSnapshot.getKey();
        }
        Init();
        return "-1";
    }

    public String readUser(String... user) {
        for (DataSnapshot dataSnapshot : userSnapshot.getChildren()) {
            UserFirebase userFirebase = dataSnapshot.getValue(UserFirebase.class);
            assert userFirebase != null;
            if (userFirebase.getLogin().equals(user[0]) && userFirebase.
                    getPassword().equals(user[1])) {
                return dataSnapshot.getKey();
            }
        }
        Init();
        return "-1";
    }

    public String getUser(String id) {
        for (DataSnapshot dataSnapshot : userSnapshot.getChildren()) {
            UserFirebase userFirebase = dataSnapshot.getValue(UserFirebase.class);
            assert userFirebase != null;
            if (String.valueOf(dataSnapshot.getKey()).equals(id)) {
                return userFirebase.getName();
            }
        }
        Init();
        return "-1";
    }

    public String checkUser(String login) {
        final String[] user_id = {"-1"};
        for (DataSnapshot dataSnapshot : userSnapshot.getChildren()) {
            UserFirebase userFirebase = dataSnapshot.getValue(UserFirebase.class);
            assert userFirebase != null;
            if (userFirebase.getLogin().equals(login)) {
                return dataSnapshot.getKey();
            }
        }
        Init();
        return user_id[0];
    }

    public HashSet<String> readUsers() {
        HashSet<String> authors = new HashSet<>();
        for (DataSnapshot dataSnapshot : userSnapshot.getChildren()) {
            UserFirebase userFirebase = dataSnapshot.getValue(UserFirebase.class);
            assert userFirebase != null;
            authors.add(userFirebase.getName());
        }
        Init();
        return authors;
    }

    public void writePost(Post post) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().
                getReference(DatabaseHelper.DB_POSTS);
        String user_id = readUser(post.getAuthor());
        databaseReference.push().setValue(new PostFirebase(
                post.getImgString(),
                post.getMscString(), post.getTitle(), post.getDescription(),
                post.getDate(), post.getLocation(), post.getLatLng()[0], post.getLatLng()[1], user_id));
        Init();
    }

    public void writeUser(String... args) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().
                getReference(DatabaseHelper.DB_USERS);
        databaseReference.push().setValue(new UserFirebase(
                args[0], args[1], args[2]));
        Init();
    }

    public void deletePost(Post post) {
        String post_id = readPost(post.getImgString(), post.getMscString(), post.getTitle(),
                post.getDescription(), post.getDate(), post.getLocation(),
                String.valueOf(post.getLatLng()[0]), String.valueOf(post.getLatLng()[1]),
                readUser(post.getAuthor()));
        FirebaseDatabase.getInstance().getReference(DatabaseHelper.DB_POSTS).child(post_id).
                removeValue();
        Init();
    }

    public void update(int pos, String... args) {
        Post post = MainActivity.imf.content.get(pos);
        Map<String, Object> map = new HashMap<>();
        map.put("image", args[0]);
        map.put("audio", args[1]);
        map.put("title", args[2]);
        map.put("description", args[3]);
        map.put("location", args[4]);
        map.put("coordinates1", args[5]);
        map.put("coordinates2", args[6]);
        String post_id = readPost(post.getImgString(), post.getMscString(), post.getTitle(),
                post.getDescription(), post.getDate(), post.getLocation(),
                String.valueOf(post.getLatLng()[0]), String.valueOf(post.getLatLng()[1]),
                readUser(post.getAuthor()));
        FirebaseDatabase.getInstance().getReference(DatabaseHelper.DB_POSTS).child(post_id).
                updateChildren(map, (databaseError, databaseReference) -> {});
        Init();
    }
}
