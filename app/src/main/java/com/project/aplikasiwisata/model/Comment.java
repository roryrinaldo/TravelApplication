package com.project.aplikasiwisata.model;

public class Comment {
    private int id;
    private int userId;
    private int wisataId;
    private String content;
    private int rating;

    public Comment(int id, int userId, int wisataId, String content, int rating) {
        this.id = id;
        this.userId = userId;
        this.wisataId = wisataId;
        this.content = content;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getWisataId() {
        return wisataId;
    }

    public String getContent() {
        return content;
    }

    public int getRating() {
        return rating;
    }
}
