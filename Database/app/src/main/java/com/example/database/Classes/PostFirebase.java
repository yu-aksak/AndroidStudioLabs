package com.example.database.Classes;

public class PostFirebase {
    private String image;
    private String audio;
    private String title;
    private String description;
    private String date;
    private String foreignKey;

    public PostFirebase(String image, String audio, String title, String description,
                        String date, String foreignKey) {
        this.image = image;
        this.audio = audio;
        this.title = title;
        this.description = description;
        this.date = date;
        this.foreignKey = foreignKey;
    }

    public PostFirebase(){ }

    public String getImage() {
        return image;
    }

    public String getAudio() {
        return audio;
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

    public String getForeignKey() {
        return foreignKey;
    }
}
