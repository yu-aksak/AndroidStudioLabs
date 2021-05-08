package com.example.api.Fragments;

import android.annotation.SuppressLint;
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

import com.example.api.Classes.JSONWriterReader;
import com.example.api.Classes.LoadPostFromWall;
import com.example.api.Classes.Post;
import com.example.api.Classes.PostAdapter;
import com.example.api.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

public class ImageMusicFragment extends Fragment {
    private RecyclerView list;

    private String imgString = null;
    private String mscString = null;
    private boolean role;
    public String author;
    private String title;
    private String description;
    private String date;

    private ArrayList<Post> content = new ArrayList<>();
    private ArrayList<Post> filtered = new ArrayList<>();
    private HashSet<String> authors = new HashSet<>();

    private boolean isEmpty = true;
    private boolean fromVK = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =
                inflater.inflate(R.layout.fragment_image_music, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            author = bundle.getString("name");
            role = bundle.getBoolean("role");
        }
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("dd/MM/yyyy");
        Date d = new Date();
        date = simpleDateFormat.format(d);

        ImageButton addPost = rootView.findViewById(R.id.addPost);
        ImageButton filter = rootView.findViewById(R.id.filter);
        list = rootView.findViewById(R.id.list);

        View.OnClickListener addPostButtonOnClickListener = view -> Hide(new AddPostFragment(), "addPostFragment");
        addPost.setOnClickListener(addPostButtonOnClickListener);

        View.OnClickListener filterPostButtonOnClickListener = view -> {
            FilterFragment ff = new FilterFragment();
            Bundle bundle1 = new Bundle();
            ArrayList<String> a = new ArrayList<>(authors);
            bundle1.putStringArrayList("authors", a);
            ff.setArguments(bundle1);
            Hide(ff, "filterFragment");
        };
        filter.setOnClickListener(filterPostButtonOnClickListener);

        if (role) {
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
            itemTouchHelper.attachToRecyclerView(list);
        }

        open();

        if (fromVK) {
            if (!content.isEmpty())
                isEmpty = false;
            LoadPostFromWall asyncTask = new LoadPostFromWall(isEmpty);
            asyncTask.execute();
            authors.add("Пост из вк");
        }

        return rootView;
    }

    ItemTouchHelper.SimpleCallback simpleCallback =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView,
                              @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            if (direction == ItemTouchHelper.RIGHT) {
                content.remove(position);
                Add(content);
                save();
            }
        }
    };

    public void Hide(Fragment f, String tag){

        getActivity().getSupportFragmentManager().beginTransaction().hide(this).commit();
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, f, tag).
                commit();
    }

    public void addPost(){
        Bundle bundle = getArguments();
        if (bundle != null) {
            imgString = bundle.getString("imgString");
            mscString = bundle.getString("mscString");
            title = bundle.getString("title");
            description = bundle.getString("description");
            AddContent();
            Add(content);
        }
        else{
            mscString = null;
            imgString = null;
        }
    }

    public void Filter(){
        filtered.clear();
        Bundle bundle = getArguments();
        if(bundle != null) {
            if(bundle.getString("author") == null) {
                filtered.addAll(content);
            }
            String au = bundle.getString("author");
            if(au != null){
                for(int i = 0; i < content.size(); i++){
                    if(content.get(i).getAuthor().equals(au))
                        filtered.add(content.get(i));
                }
            }
            String d = bundle.getString("date");
            if(d != null){
                if(d.equals("up")) {
                    if (filtered.size() > 1)
                        Collections.sort(filtered, (p1, p2) -> p1.getDate().compareTo(p2.getDate()));
                }
                else {
                    if (filtered.size() > 1)
                        Collections.sort(filtered, (p1, p2) -> p2.getDate().compareTo(p1.getDate()));
                }
            }
        }
        else
            filtered.addAll(content);
        Add(filtered);
    }

    public void Edit(String imgString, String mscString, String author,  String title, String description, String date, int position){
        Post post = new Post(imgString, mscString, author, title, description, date);
        content.set(position, post);
        save();
        Add(content);
    }

    public void Add(ArrayList<Post> content) {

        PostAdapter.OnStateClickListener stateClickListener = (post, position) -> {
            PostFragment postFragment = new PostFragment(post, position);
            Bundle bundle = new Bundle();
            bundle.putBoolean("role", role);
            postFragment.setArguments(bundle);
            Hide(postFragment, "postFragment");
        };

        PostAdapter adapter = new PostAdapter(getActivity(), content, stateClickListener);
        list.setAdapter(adapter);
    }

    public void AddContent(){
        ArrayList<Post> temp = new ArrayList<>();
        authors.add(author);
        temp.add(new Post(imgString, mscString, author, title, description, date));
        temp.addAll(content);
        content.clear();
        content.addAll(temp);
        save();
    }

    private void AddAuthors(){
        for(int i = 0; i < content.size(); i++)
            authors.add(content.get(i).getAuthor());
    }
    //ImageMusicActivity
    public void save(){

        boolean result = JSONWriterReader.
                exportToJSON(getActivity().getApplicationContext(), content);
        if(result){
            Toast.makeText(getActivity(), "Данные сохранены", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getActivity(), "Не удалось сохранить данные",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void open(){
        content = JSONWriterReader.importFromJSON(getActivity().getApplicationContext());
        if(content!=null){
            Add(content);
            AddAuthors();
            Toast.makeText(getActivity().getApplicationContext(), "Данные восстановлены",
                    Toast.LENGTH_LONG).show();
        }
        else{
            content = new ArrayList<>();
            Toast.makeText(getActivity().getApplicationContext(), "Не удалось открыть данные",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void AddContent(Post post){
        content.add(post);
    }

    public void setFromVK(boolean fromVK) {
        this.fromVK = fromVK;
    }

    public ArrayList<Post> getContent() {
        return content;
    }
}