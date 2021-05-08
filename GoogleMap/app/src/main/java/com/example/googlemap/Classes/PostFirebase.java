package com.example.googlemap.Classes;

public class PostFirebase {
    private String image;
    private String audio;
    private String title;
    private String description;
    private String date;
    private String location;
    private double coordinates1;
    private double coordinates2;
    private String foreignKey;

    public PostFirebase(String image, String audio, String title, String description,
                        String date, String location, double coordinates1, double coordinates2, String foreignKey) {
        this.image = image;
        this.audio = audio;
        this.title = title;
        this.description = description;
        this.date = date;
        this.location = location;
        this.coordinates1 = coordinates1;
        this.coordinates2 = coordinates2;
        this.foreignKey = foreignKey;
    }

    public PostFirebase(){ }

    public String getLocation() {
        return location;
    }

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

    public double getCoordinates1() {
        return coordinates1;
    }

    public double getCoordinates2() {
        return coordinates2;
    }

    public String getForeignKey() {
        return foreignKey;
    }
}
