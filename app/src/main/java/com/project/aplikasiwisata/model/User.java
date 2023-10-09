package com.project.aplikasiwisata.model;

public class User {
    private int id;
    private String email;
    private String password;
    private String name;
    private String photo;

    public User(int id, String email, String password, String name, String photo) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }
}
