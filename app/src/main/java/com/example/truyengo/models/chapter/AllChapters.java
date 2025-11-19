package com.example.truyengo.models.chapter;

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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getChapter_name() {
        return chapter_name;
    }

    public void setChapter_name(String chapter_name) {
        this.chapter_name = chapter_name;
    }

    public String getChapterData() {
        return chapterData;
    }

    public void setChapterData(String chapterData) {
        this.chapterData = chapterData;
    }
}
