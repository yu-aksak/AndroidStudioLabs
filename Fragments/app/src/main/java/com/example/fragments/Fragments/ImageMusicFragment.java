package com.example.fragments.Fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.fragments.Classes.UriAdd;
import com.example.fragments.Classes.UriAddAdapter;
import com.example.fragments.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import static android.app.Activity.RESULT_OK;

public class ImageMusicFragment extends Fragment {
    private ImageButton addImage;
    private Button addMusic;
    private Button apply;
    private ListView list;

    Uri uriImage = null;
    Uri uriMusic = null;
    ArrayList<UriAdd> content = new ArrayList<>();
    UriAddAdapter adapter;

    MediaPlayer mp = new MediaPlayer();
    UriAdd _uriAdd = new UriAdd();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =
                inflater.inflate(R.layout.fragment_image_music, container, false);

        addImage = (ImageButton) rootView.findViewById(R.id.addImage);
        addMusic = (Button) rootView.findViewById(R.id.addMusic);
        apply = (Button) rootView.findViewById(R.id.apply);
        list = (ListView) rootView.findViewById(R.id.list);

        View.OnClickListener addImageButtonOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        };
        addImage.setOnClickListener(addImageButtonOnClickListener);

        View.OnClickListener addMusicButtonOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent audioPickerIntent = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                audioPickerIntent.setType("audio/*");
                startActivityForResult(audioPickerIntent, 2);
            }
        };
        addMusic.setOnClickListener(addMusicButtonOnClickListener);

        View.OnClickListener addButtonOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Apply();
            }
        };
        apply.setOnClickListener(addButtonOnClickListener);

        return rootView;
    }

    private void Apply() {
        adapter = new UriAddAdapter(getActivity(), R.layout.list_item, content);
        list.setAdapter(adapter);

        apply.setVisibility(View.INVISIBLE);
        addImage.setImageResource(R.drawable.ic_plus);
        addMusic.setText("Добавить музыку");
        uriImage = null;
        uriMusic = null;

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // получаем нажатый элемент
                UriAdd uriAdd = adapter.getItem(position);
                if(_uriAdd.getAudioUri() != null)
                    if (_uriAdd.getAudioUri().equals(uriAdd.getAudioUri()) && _uriAdd.getImageUri().equals(uriAdd.getImageUri()))
                        if(mp.isPlaying())
                            mp.pause();
                        else
                            mp.start();
                    else {
                        _uriAdd = adapter.getItem(position);
                        if (mp.isPlaying()) {
                            mp.stop();
                        }
                        try {
                            mp.reset();
                            mp.setDataSource(getContext(), _uriAdd.getAudioUri());
                            mp.prepare();
                            mp.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                else {
                    _uriAdd = adapter.getItem(position);
                    try {
                        mp.reset();
                        mp.setDataSource(getContext(), _uriAdd.getAudioUri());
                        mp.prepare();
                        mp.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent ReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, ReturnedIntent);

        if(requestCode == 1)
            if (resultCode == RESULT_OK) {
                uriImage = ReturnedIntent.getData();
                addImage.setImageURI(uriImage);
            }
        if(requestCode == 2)
            if (resultCode == RESULT_OK) {
                uriMusic = ReturnedIntent.getData();
                addMusic.setText(uriMusic.getLastPathSegment());
            }
        if(uriImage != null && uriMusic != null){
            UriAdd uriAdd = new UriAdd(uriImage, uriMusic);
            ArrayList<UriAdd> temp = new ArrayList<>();
            temp.add(uriAdd);
            temp.addAll(content);
            content.clear();
            content.addAll(temp);
            apply.setVisibility(View.VISIBLE);
        }
    }
}