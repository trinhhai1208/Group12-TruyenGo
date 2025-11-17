package com.example.truyengo.models.chapter;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Chapter {
    @SerializedName("_id")
    private String id;
    private String chapter_name;
    private String chapter_path;
    private ArrayList<ChapterImage> chapter_image;

    public Chapter() {
    }

    public Chapter(String id, String chapter_name, String chapter_path, ArrayList<ChapterImage> chapter_image) {
        this.id = id;
        this.chapter_name = chapter_name;
        this.chapter_path = chapter_path;
        this.chapter_image = chapter_image;
    }
}

