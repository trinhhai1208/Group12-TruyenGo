package com.example.truyengo.api.search;

import com.example.truyengo.api.chapter.ApiListChapterData;
import com.example.truyengo.models.book.BookCategory;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiSearchBookJson {
    private String name;
    private String slug;
    private String status;
    @SerializedName("thumb_url")
    private String thumbUrl;
    private List<BookCategory> category;
    private String updatedAt;
    private List<ApiListChapterData> chapters;

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

    public List<ApiListChapterData> getChapters() {
        return chapters;
    }

    public void setChapters(List<ApiListChapterData> chapters) {
        this.chapters = chapters;
    }
}
