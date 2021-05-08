package com.example.googlemap.Fragments;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.googlemap.Classes.Post;
import com.example.googlemap.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class PostFragment extends Fragment {
    private ImageButton button;
    private TextView author;
    private TextView title;
    private TextView description;
    private TextView date;
    private boolean role;

    private final Post post;
    private final int position;

    private MediaPlayer mp = new MediaPlayer();

    PostFragment(Post post, int position){
        this.post = post;
        this.position = position;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post, container, false);

        button = rootView.findViewById(R.id.imageMusic);
        author = rootView.findViewById(R.id.author);
        title = rootView.findViewById(R.id.titlePost);
        description = rootView.findViewById(R.id.descriptionPost);
        date = rootView.findViewById(R.id.date);
        description.setMovementMethod(new ScrollingMovementMethod());
        Button back = rootView.findViewById(R.id.back);
        Button location = rootView.findViewById(R.id.location);

        ImageButton edit = rootView.findViewById(R.id.edit);

        Initialize();

        Bundle bundle = getArguments();
        if(bundle != null){
            role = bundle.getBoolean("role");
        }

        if(role) {
            edit.setVisibility(View.VISIBLE);
            View.OnClickListener editButtonOnClickListener = view -> Edit();
            edit.setOnClickListener(editButtonOnClickListener);
        }
        else
            edit.setVisibility(View.INVISIBLE);

        View.OnClickListener backButtonOnClickListener = view -> Back();
        back.setOnClickListener(backButtonOnClickListener);

        View.OnClickListener locationButtonOnClickListener = view -> Location();
        location.setOnClickListener(locationButtonOnClickListener);

        View.OnClickListener musicButtonOnClickListener = view -> Music();
        button.setOnClickListener(musicButtonOnClickListener);

        return rootView;
    }
    //https://vk.com/yulia.aksak

    private void Edit(){
        if (mp.isPlaying()){
            mp.stop();
        }
        if(mp != null)
            mp = new MediaPlayer();
        AddPostFragment apf = new AddPostFragment();
        requireActivity().getSupportFragmentManager().beginTransaction().hide(this).commit();
        Bundle bundle = new Bundle();
        bundle.putString("imgString", post.getImgString());
        bundle.putString("mscString", post.getMscString());
        bundle.putString("title", post.getTitle());
        bundle.putString("description", post.getDescription());
        bundle.putString("author", post.getAuthor());
        bundle.putString("location", post.getLocation());
        bundle.putDoubleArray("coordinates", post.getLatLng());
        apf.setArguments(bundle);
        requireActivity().getSupportFragmentManager().beginTransaction().
                add(R.id.container, apf, "addPostFragment").commit();
    }

    public void EditPost(){
        Bundle bundle = getArguments();
        if(bundle != null){
            ImageMusicFragment imf = (ImageMusicFragment) requireActivity().
                    getSupportFragmentManager().
                    findFragmentByTag("imageMusic");
            assert imf != null;
            imf.Edit(bundle.getString("imgString"),
                    bundle.getString("mscString"),
                    post.getAuthor(),
                    bundle.getString("title"),
                    bundle.getString("description"),
                    post.getDate(), bundle.getString("location"),
                    bundle.getDoubleArray("coordinates"),
                    position);
        }
        assert bundle != null;
        post.setImgString(bundle.getString("imgString"));
        post.setMscString(bundle.getString("mscString"));
        post.setTitle(bundle.getString("title"));
        post.setDescription(bundle.getString("description"));
        post.setLocation(bundle.getString("location"));
        post.setLatLng(bundle.getDoubleArray("coordinates"));
        Initialize();
    }

    private void Back() {
        requireActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        ImageMusicFragment imf = (ImageMusicFragment) requireActivity().getSupportFragmentManager().
                findFragmentByTag("imageMusic");
        assert imf != null;
        requireActivity().getSupportFragmentManager().beginTransaction().show(imf).commit();
        if (mp.isPlaying())
            mp.stop();
    }

    public void Location(){
        MapsFragment mf = new MapsFragment();
        requireActivity().getSupportFragmentManager().beginTransaction().hide(this).commit();
        Bundle bundle = new Bundle();
        bundle.putDoubleArray("coordinates", post.getLatLng());
        mf.setArguments(bundle);
        requireActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, mf).commit();
    }

    @SuppressLint("SetTextI18n")
    public void Initialize() {

        if (post.getImgString() != null)
            if (post.getAuthor().equals("Пост из вк"))
                Picasso.with(getContext()).load(post.getImgString()).into(button);
            else
                button.setImageURI(Uri.parse(post.getImgString()));

        if (post.getMscString() != null) {
            String music = post.getMscString();
            try {
                mp.setDataSource(getContext(), Uri.parse(music));
                mp.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        author.setText(post.getAuthor());
        title.setText(post.getTitle());
        description.setText(post.getDescription() + "\n" + post.getLocation());
        date.setText(post.getDate());

    }

    public void Music() {
        if (mp.isPlaying())
            mp.pause();
        else
            mp.start();
    }
}