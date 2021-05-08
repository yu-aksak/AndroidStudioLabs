package com.example.files.Fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.files.Classes.JSONWriterReader;
import com.example.files.Classes.Post;
import com.example.files.Classes.PostAdapter;
import com.example.files.R;

import java.util.ArrayList;

public class ImageMusicFragment extends Fragment {
    private ImageButton addPost;
    private RecyclerView list;

    private String imgString = null;
    private String mscString = null;
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

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(list);

        open();

        return rootView;
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            switch (direction){
                case ItemTouchHelper.RIGHT:
                    content.remove(position);
                    Add();
                    save();
                    break;
            }
        }
    };

    public void Hide(Fragment f){
        getActivity().getSupportFragmentManager().beginTransaction().hide(this).commit();
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, f).commit();
    }

    public void addPost(){
        Bundle bundle = new Bundle();
        bundle = getArguments();
        if (bundle != null) {
            imgString = bundle.getString("imgString");
            mscString = bundle.getString("mscString");
            title = bundle.getString("title");
            description = bundle.getString("description");
            AddContent();
            Add();
        }
        else{
            mscString = null;
            imgString = null;
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
        temp.add(new Post(imgString, mscString, title, description));
        temp.addAll(content);
        content.clear();
        content.addAll(temp);
        save();
    }
    //ImageMusicActivity
    public void save(){

        boolean result = JSONWriterReader.exportToJSON(getActivity().getApplicationContext(), content);
        if(result){
            Toast.makeText(getActivity(), "Данные сохранены", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getActivity(), "Не удалось сохранить данные", Toast.LENGTH_LONG).show();
        }
    }

    public void open(){
        content = JSONWriterReader.importFromJSON(getActivity().getApplicationContext());
        if(content!=null){
            Add();
            Toast.makeText(getActivity().getApplicationContext(), "Данные восстановлены", Toast.LENGTH_LONG).show();
        }
        else{
            content = new ArrayList<>();
            Toast.makeText(getActivity().getApplicationContext(), "Не удалось открыть данные", Toast.LENGTH_LONG).show();
        }
    }
}