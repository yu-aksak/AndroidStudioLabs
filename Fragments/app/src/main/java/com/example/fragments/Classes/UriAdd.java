package com.example.fragments.Classes;

import android.net.Uri;

public class UriAdd {
    private Uri imageUri;
    private Uri audioUri;

    public UriAdd(){
        this.imageUri = null;
        this.audioUri = null;
    }

    public UriAdd(Uri image, Uri music){
        this.imageUri = image;
        this.audioUri = music;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public void setAudioUri(Uri audioUri) {
        this.audioUri = audioUri;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public Uri getAudioUri() {
        return audioUri;
    }
}
