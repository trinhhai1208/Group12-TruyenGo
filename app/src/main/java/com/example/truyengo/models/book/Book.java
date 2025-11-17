package com.example.truyengo.models.book;

import com.example.truyengo.api.chapter.ApiListChapterData;

import lombok.Getter;
import lombok.Setter;

import org.bson.types.ObjectId;

import java.util.List;

@Getter
@Setter
public class Book {
    private ObjectId id;
    private String name;
    private String slug;
    private String status;
    private String updatedAt;
    private String thumbnail;
    private List<BookCategory> category;
    private List<BookChapterLastest> chapterLastests;

    private String content;
    private List<ApiListChapterData> chapters;

    public Book() {
    }

    public Book(String name, String slug) {
        this.name = name;
        this.slug = slug;
    }

    public Book(String id, String name, String slug, String status, String updatedAt, String thumbnail, List<BookCategory> category, List<BookChapterLastest> chapterLastests) {
        this.id = new ObjectId(id);
        this.name = name;
        this.slug = slug;
        this.status = status;
        this.updatedAt = updatedAt;
        this.thumbnail = thumbnail;
        this.category = category;
        this.chapterLastests = chapterLastests;
    }

    public Book(String id, String name, String slug, String content, String status, String thumbnail, List<BookCategory> category, List<ApiListChapterData> chapters, String updatedAt) {
        this.id = new ObjectId(id);
        this.name = name;
        this.slug = slug;
        this.content = content;
        this.status = status;
        this.thumbnail = thumbnail;
        this.category = category;
        this.chapters = chapters;
        this.updatedAt = updatedAt;
    }

    public Book(String name, String slug, String status, String updatedAt, String thumbnail, List<BookCategory> category, List<ApiListChapterData> chapters) {
        this.name = name;
        this.slug = slug;
        this.status = status;
        this.updatedAt = updatedAt;
        this.thumbnail = thumbnail;
        this.category = category;
        this.chapters = chapters;
    }
}
