package com.example.googlemap.Classes;

public class Post {
    private String imgString;
    private String mscString;
    private String author;
    private String title;
    private String description;
    private String location;
    private double[] latLng;
    private final String date;

    public Post(String image, String music, String author, String title,
                String description, String date, String location, double[] latLng){
        this.imgString = image;
        this.mscString = music;
        this.author = author;
        this.title = title;
        this.description = description;
        this.date = date;
        this.location = location;
        this.latLng = latLng;
    }

    public void setImgString(String imgString){
        this.imgString = imgString;
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

    public void setLocation(String location) {
        this.location = location;
    }

    public void setLatLng(double[] latLng) {
        this.latLng = latLng;
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

    public String getLocation() {
        return location;
    }

    public double[] getLatLng() {
        return latLng;
    }
}
