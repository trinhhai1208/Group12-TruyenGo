package com.example.truyengo.models.chapter;

public class ChapterImage {
    private String image_page;
    private String image_file;

    public ChapterImage(String image_page, String image_file) {
        this.image_page = image_page;
        this.image_file = image_file;
    }

    public String getImage_page() {
        return image_page;
    }

    public void setImage_page(String image_page) {
        this.image_page = image_page;
    }

    public String getImage_file() {
        return image_file;
    }

    public void setImage_file(String image_file) {
        this.image_file = image_file;
    }
}

