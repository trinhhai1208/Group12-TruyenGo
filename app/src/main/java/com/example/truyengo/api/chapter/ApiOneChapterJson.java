package com.example.truyengo.api.chapter;

import com.example.truyengo.models.chapter.ChapterImage;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

}
