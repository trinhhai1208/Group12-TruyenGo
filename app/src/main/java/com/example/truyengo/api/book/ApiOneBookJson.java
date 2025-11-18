package com.example.truyengo.api.book;

import com.example.truyengo.api.chapter.ApiListChapterData;
import com.example.truyengo.models.book.BookCategory;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ApiOneBookJson {
    @SerializedName("_id")
    private String id;
    private String name;
    private String slug;
    private String content;
    private String status;
    @SerializedName("thumb_url")
    private String thumbUrl;
    private List<BookCategory> category;
    private List<ApiListChapterData> chapters;
    private String updatedAt;

}
