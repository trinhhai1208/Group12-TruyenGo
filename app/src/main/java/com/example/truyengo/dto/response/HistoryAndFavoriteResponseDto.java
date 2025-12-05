package com.example.truyengo.dto.response;

import com.google.gson.annotations.SerializedName;

public class HistoryAndFavoriteResponseDto {
    @SerializedName("slug")
    String slug;

    @SerializedName("id")
    String id;

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
