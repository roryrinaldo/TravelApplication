package com.project.aplikasiwisata.model;

public class Wisata {
    private int id;
    private String name;
    private String description;
    private String image;
    private String location;

    public Wisata(int id, String name, String description, String image, String location) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getLocation() {
        return location;
    }
}

