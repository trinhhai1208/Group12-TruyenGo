package com.example.truyengo.models.chapter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChapterImage {
    private String image_page;
    private String image_file;

    public ChapterImage(String image_page, String image_file) {
        this.image_page = image_page;
        this.image_file = image_file;
    }
}

