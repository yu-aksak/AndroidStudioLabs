package com.example.api.Classes;

public class Post {
    private String imgString;
    private String mscString;
    private String author;
    private String title;
    private String description;
    private String date;

    public Post(String image, String music, String author, String title,
                String description, String date){
        this.imgString = image;
        this.mscString = music;
        this.author = author;
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public void setImgString(String imgString){
        this.imgString = imgString;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMscString(String mscString) {
        this.mscString = mscString;
    }

    public String getImgString() {
        return imgString;
    }

    public String getMscString() {
        return mscString;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }
}
