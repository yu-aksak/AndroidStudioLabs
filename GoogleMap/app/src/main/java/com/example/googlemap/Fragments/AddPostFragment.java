package com.example.googlemap.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.googlemap.R;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;

public class AddPostFragment extends Fragment {
    private ImageButton addImage;
    private Button addMusic;
    private Button add;
    private Button addLocation;
    private EditText description;
    private EditText title;

    String imgString = null;
    String mscString = null;
    String loc = null;
    double[] latLng = {-34, 151};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_add_post, container, false);
        addImage = rootView.findViewById(R.id.addImage);
        addMusic = rootView.findViewById(R.id.addMusic);
        add = rootView.findViewById(R.id.add);
        addLocation = rootView.findViewById(R.id.addLocation);
        description = rootView.findViewById(R.id.description);
        title = rootView.findViewById(R.id.title);
        Button back = rootView.findViewById(R.id.back1);

        Bundle bundle = getArguments();
        if(bundle != null){
            imgString = bundle.getString("imgString");
            String author = bundle.getString("author");
            if (imgString != null)
                if (author.equals("Пост из вк"))
                    Picasso.with(getContext()).load(imgString).into(addImage);
                else
                    addImage.setImageURI(Uri.parse(imgString));
            mscString = bundle.getString("mscString");
            if(mscString!=null)
                addMusic.setText(Uri.parse(mscString).getPath());
            title.setText(bundle.getString("title"));
            description.setText(bundle.getString("description"));
            addLocation.setText(bundle.getString("location"));
            latLng = bundle.getDoubleArray("coordinates");
            add.setText("СОХРАНИТЬ");
            add.setVisibility(View.VISIBLE);
            View.OnClickListener addButtonOnClickListener = view -> Save();
            add.setOnClickListener(addButtonOnClickListener);
        }
        else{
            View.OnClickListener addButtonOnClickListener = view -> Add();
            add.setOnClickListener(addButtonOnClickListener);
        }
        @SuppressLint("IntentReset") View.OnClickListener addImageButtonOnClickListener = view -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT ,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, 1);
        };
        addImage.setOnClickListener(addImageButtonOnClickListener);

        @SuppressLint("IntentReset") View.OnClickListener addMusicButtonOnClickListener = view -> {
            Intent audioPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT,
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
            audioPickerIntent.setType("audio/*");
            startActivityForResult(audioPickerIntent, 2);
        };
        addMusic.setOnClickListener(addMusicButtonOnClickListener);

        View.OnClickListener backButtonOnClickListener = view -> Back();
        back.setOnClickListener(backButtonOnClickListener);

        View.OnClickListener addLocationButtonOnClickListener = view -> AddLocation();
        addLocation.setOnClickListener(addLocationButtonOnClickListener);

        return rootView;
    }

    private void Back(){
        requireActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        ImageMusicFragment imf = (ImageMusicFragment) requireActivity().getSupportFragmentManager().
                findFragmentByTag("imageMusic");
        assert imf != null;
        requireActivity().getSupportFragmentManager().beginTransaction().show(imf).commit();
    }

    private void Add(){
        ImageMusicFragment imf = (ImageMusicFragment) requireActivity().getSupportFragmentManager().
                findFragmentByTag("imageMusic");
        Bundle bundle = new Bundle();
        bundle.putString("imgString", imgString);
        bundle.putString("mscString", mscString);
        bundle.putString("title", title.getText().toString());
        bundle.putString("description", description.getText().toString());
        bundle.putString("location", addLocation.getText().toString());
        bundle.putDoubleArray("coordinates", latLng);
        assert imf != null;
        imf.setArguments(bundle);
        requireActivity().getSupportFragmentManager().beginTransaction().show(imf).commit();
        imf.addPost();
        requireActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    private void Save(){
        PostFragment pf = (PostFragment) requireActivity().getSupportFragmentManager().
                findFragmentByTag("postFragment");
        Bundle bundle = new Bundle();
        bundle.putString("imgString", imgString);
        bundle.putString("mscString", mscString);
        bundle.putString("title", title.getText().toString());
        bundle.putString("description", description.getText().toString());
        bundle.putString("location", loc);
        bundle.putDoubleArray("coordinates", latLng);
        assert pf != null;
        pf.setArguments(bundle);
        requireActivity().getSupportFragmentManager().beginTransaction().show(pf).commit();
        pf.EditPost();
        requireActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    public void addLoc(){
        Bundle bundle = getArguments();
        if (bundle != null) {
            loc = bundle.getString("location");
            latLng = bundle.getDoubleArray("coordinates");
            addLocation.setText(loc);
        }
        else{
            loc = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent ReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, ReturnedIntent);

        if(requestCode == 1)
            if (resultCode == RESULT_OK) {
                imgString = ReturnedIntent.getData().toString();
                addImage.setImageURI(ReturnedIntent.getData());
            }
        if(requestCode == 2)
            if (resultCode == RESULT_OK) {
                mscString = ReturnedIntent.getData().toString();
                addMusic.setText(ReturnedIntent.getData().getPath());
            }
        if(imgString != null && mscString != null ){
            add.setVisibility(View.VISIBLE);
        }
    }

    public void AddLocation(){
        requireActivity().getSupportFragmentManager().beginTransaction().hide(this).commit();
        MapsFragment mf = new MapsFragment();
        Bundle bundle = new Bundle();
        bundle.putDoubleArray("coordinates", latLng);
        mf.setArguments(bundle);
        requireActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, mf).commit();
    }
}