package com.example.recyclerview.Classes;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Post implements Parcelable {
    private Uri imageUri;
    private Uri audioUri;
    private String title;
    private String description;

    public Post(){
        this.imageUri = null;
        this.audioUri = null;
        this.title = "";
        this.description = "";
    }

    public Post(Uri image, Uri music, String title, String description){
        this.imageUri = image;
        this.audioUri = music;
        this.title = title;
        this.description = description;
    }

    protected Post(Parcel in) {
        imageUri = in.readParcelable(Uri.class.getClassLoader());
        audioUri = in.readParcelable(Uri.class.getClassLoader());
        title = in.readString();
        description = in.readString();
    }

    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public void setAudioUri(Uri audioUri) {
        this.audioUri = audioUri;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public Uri getAudioUri() {
        return audioUri;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(imageUri, flags);
        dest.writeParcelable(audioUri, flags);
    }
}
