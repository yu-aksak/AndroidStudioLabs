package com.example.files.Fragments;

import android.graphics.BitmapFactory;
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

import com.example.files.Classes.Post;
import com.example.files.R;

import java.io.IOException;

public class PostFragment extends Fragment {
    private ImageButton button;
    private TextView title;
    private TextView description;
    private Button back;

    private String music;
    private Post post;

    private MediaPlayer mp = new MediaPlayer();

    PostFragment(Post post){
        this.post = post;
    }

    /*Spannable spannable;
    URLSpan[] spans;*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post, container, false);

        button = (ImageButton) rootView.findViewById(R.id.imageMusic);
        title = (TextView) rootView.findViewById(R.id.titlePost);
        description = (TextView) rootView.findViewById(R.id.descriptionPost);
        description.setMovementMethod(new ScrollingMovementMethod());
        back = (Button) rootView.findViewById(R.id.back);

        Initialize();

        View.OnClickListener backButtonOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Back();
            }
        };
        back.setOnClickListener(backButtonOnClickListener);

        View.OnClickListener musicButtonOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Music();
            }
        };
        button.setOnClickListener(musicButtonOnClickListener);

        return rootView;
    }
    //https://vk.com/yulia.aksak
    private void Back() {
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        ImageMusicFragment imf = (ImageMusicFragment) getActivity().getSupportFragmentManager().findFragmentByTag("imageMusic");
        getActivity().getSupportFragmentManager().beginTransaction().show(imf).commit();
        if (mp.isPlaying())
            mp.stop();
    }

    public void Initialize(){
        button.setImageURI(Uri.parse(post.getImgString()));
        music = post.getMscString();
        try {
            mp.setDataSource(getContext(), Uri.parse(music));
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        title.setText(post.getTitle());
        description.setText(post.getDescription());

    }

    public void Music() {
        if (mp.isPlaying())
            mp.pause();
        else
            mp.start();
    }
}