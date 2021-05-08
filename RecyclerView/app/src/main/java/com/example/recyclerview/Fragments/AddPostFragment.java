package com.example.recyclerview.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.recyclerview.R;


import static android.app.Activity.RESULT_OK;

public class AddPostFragment extends Fragment {
    private ImageButton addImage;
    private Button addMusic;
    private Button add;
    private EditText description;
    private EditText title;

    Uri uriImage = null;
    Uri uriMusic = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(R.layout.fragment_add_post, container, false);
        addImage = (ImageButton) rootView.findViewById(R.id.addImage);
        addMusic = (Button) rootView.findViewById(R.id.addMusic);
        add = (Button) rootView.findViewById(R.id.add);
        description = (EditText) rootView.findViewById(R.id.description);
        title = (EditText) rootView.findViewById(R.id.title);

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
                Add();
            }
        };
        add.setOnClickListener(addButtonOnClickListener);
        return rootView;
    }

    private void Add(){
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        ImageMusicFragment imf = (ImageMusicFragment) getActivity().getSupportFragmentManager().findFragmentByTag("imageMusic");
        Bundle bundle = new Bundle();
        bundle.putParcelable("uriImage", uriImage);
        bundle.putParcelable("uriMusic", uriMusic);
        bundle.putString("title", title.getText().toString());
        bundle.putString("description", description.getText().toString());
        imf.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().show(imf).commit();
        imf.addPost();
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
            add.setVisibility(View.VISIBLE);
        }
    }
}