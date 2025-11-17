package com.example.truyengo.models.chapter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllChapters {
    private String filename;
    private String chapter_name;
    private String chapterData;

    public AllChapters() {
    }

    public AllChapters(String filename, String chapter_name, String chapterData) {
        this.filename = filename;
        this.chapter_name = chapter_name;
        this.chapterData = chapterData;
    }
}
