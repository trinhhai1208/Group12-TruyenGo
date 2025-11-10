package com.example.truyengo;
public class Book {
    private String name;
    private String author;
    private String state;
    private String lastUpdated;
    private String newChapter;
    private String genre;
    private int imageResId;

    public Book(String name, String author, String state, String lastUpdated, String newChapter, String genre, int imageResId) {
        this.name = name;
        this.author = author;
        this.state = state;
        this.lastUpdated = lastUpdated;
        this.newChapter = newChapter;
        this.genre = genre;
        this.imageResId = imageResId;
    }

    public String getName() { return name; }
    public String getAuthor() { return author; }
    public String getState() { return state; }
    public String getLastUpdated() { return lastUpdated; }
    public String getNewChapter() { return newChapter; }
    public String getGenre() { return genre; }
    public int getImageResId() { return imageResId; }
}