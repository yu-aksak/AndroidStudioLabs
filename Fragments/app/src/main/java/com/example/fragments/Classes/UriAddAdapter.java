package com.example.fragments.Classes;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.fragments.R;

import java.util.List;

public class UriAddAdapter extends ArrayAdapter<UriAdd> {
    private LayoutInflater inflater;
    private int layout;
    private List<UriAdd> uris;

    public UriAddAdapter(Context context, int resource, List<UriAdd> uris) {
        super(context, resource, uris);
        this.uris = uris;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        UriAdd uri = uris.get(position);
        imageView.setImageURI(uri.getImageUri());
        return view;
    }

    public Uri getUriMusic(int position){
        return uris.get(position).getAudioUri();
    }

    public Uri getImage(int position){
        return uris.get(position).getImageUri();
    }
}
