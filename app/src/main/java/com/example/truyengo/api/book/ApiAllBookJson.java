package com.example.truyengo.api.book;

import com.example.truyengo.models.book.BookCategory;
import com.example.truyengo.models.book.BookChapterLastest;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiAllBookJson {
    @SerializedName("_id")
    private String id;
    private String name;
    private String slug;
    private String status;
    @SerializedName("thumb_url")
    private String thumbUrl;
    private List<BookCategory> category;
    private String updatedAt;
    private List<BookChapterLastest> chaptersLatest;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public List<BookCategory> getCategory() {
        return category;
    }

    public void setCategory(List<BookCategory> category) {
        this.category = category;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<BookChapterLastest> getChaptersLatest() {
        return chaptersLatest;
    }

    public void setChaptersLatest(List<BookChapterLastest> chaptersLatest) {
        this.chaptersLatest = chaptersLatest;
    }
}
