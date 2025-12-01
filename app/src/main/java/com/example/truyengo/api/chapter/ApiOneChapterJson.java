package com.example.truyengo.api.chapter;

import com.example.truyengo.models.chapter.ChapterImage;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ApiOneChapterJson {
    @SerializedName("_id")
    private String id;
    private String chapter_name;
    private String chapter_path;
    private ArrayList<ChapterImage> chapter_image;

    public ApiOneChapterJson() {
    }

    public ApiOneChapterJson(String id, String chapter_name, String chapter_path, ArrayList<ChapterImage> chapter_image) {
        this.id = id;
        this.chapter_name = chapter_name;
        this.chapter_path = chapter_path;
        this.chapter_image = chapter_image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChapter_name() {
        return chapter_name;
    }

    public void setChapter_name(String chapter_name) {
        this.chapter_name = chapter_name;
    }

    public String getChapter_path() {
        return chapter_path;
    }

    public void setChapter_path(String chapter_path) {
        this.chapter_path = chapter_path;
    }

    public ArrayList<ChapterImage> getChapter_image() {
        return chapter_image;
    }

    public void setChapter_image(ArrayList<ChapterImage> chapter_image) {
        this.chapter_image = chapter_image;
    }
}
