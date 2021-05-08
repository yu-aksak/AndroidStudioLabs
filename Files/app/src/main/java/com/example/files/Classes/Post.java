package com.example.files.Classes;


public class Post {
    private String imgString;
    private String mscString;
    private String title;
    private String description;

    public Post(){
        this.imgString = null;
        this.mscString = null;
        this.title = "";
        this.description = "";
    }

    public Post(String image, String music, String title, String description){
        this.imgString = image;
        this.mscString = music;
        this.title = title;
        this.description = description;
    }

    public void setImgString(String imgString) {
        this.imgString = imgString;
    }

    public void setMscString(String mscString) {
        this.mscString = mscString;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgString() {
        return imgString;
    }

    public String getMscString() {
        return mscString;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

}
