package com.example.recyclerview.Fragments;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;

import com.example.recyclerview.Classes.Post;
import com.example.recyclerview.Classes.PostAdapter;
import com.example.recyclerview.R;

import java.io.IOException;
import java.util.ArrayList;

public class ImageMusicFragment extends Fragment {
    private ImageButton addPost;
    private RecyclerView list;

    private Uri uriImage = null;
    private Uri uriMusic = null;
    private String title;
    private String description;

    private ArrayList<Post> content = new ArrayList<>();
    private PostAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =
                inflater.inflate(R.layout.fragment_image_music, container, false);

        addPost = (ImageButton) rootView.findViewById(R.id.addPost);
        list = (RecyclerView) rootView.findViewById(R.id.list);

        View.OnClickListener addPostButtonOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hide(new AddPostFragment());
            }
        };
        addPost.setOnClickListener(addPostButtonOnClickListener);


        return rootView;
    }

    public void Hide(Fragment f){

        getActivity().getSupportFragmentManager().beginTransaction().hide(this).commit();
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, f).commit();
    }

    public void addPost(){
        Bundle bundle = new Bundle();
        bundle = getArguments();
        if (bundle != null) {
            uriImage = (Uri) bundle.getParcelable("uriImage");
            uriMusic = (Uri) bundle.getParcelable("uriMusic");
            title = bundle.getString("title");
            description = bundle.getString("description");
            AddContent();
            Add();
        }
        else{
            uriMusic = null;
            uriImage = null;
        }
    }

    public void Add() {

        PostAdapter.OnStateClickListener stateClickListener = new PostAdapter.OnStateClickListener() {
            @Override
            public void onStateClick(Post post, int position) {
                PostFragment postFragment = new PostFragment(post);
                Hide(postFragment);
            }
        };

        adapter = new PostAdapter(getActivity(), content, stateClickListener);
        list.setAdapter(adapter);
    }

    public void AddContent(){
        ArrayList<Post> temp = new ArrayList<>();
        temp.add(new Post(uriImage, uriMusic, title, description));
        temp.addAll(content);
        content.clear();
        content.addAll(temp);
    }
}