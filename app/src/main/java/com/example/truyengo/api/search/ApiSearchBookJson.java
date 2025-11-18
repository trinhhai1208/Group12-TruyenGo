package com.example.truyengo.api.search;

import com.example.truyengo.api.chapter.ApiListChapterData;
import com.example.truyengo.models.book.BookCategory;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiSearchBookJson {
    private String name;
    private String slug;
    private String status;
    @SerializedName("thumb_url")
    private String thumbUrl;
    private List<BookCategory> category;
    private String updatedAt;
    private List<ApiListChapterData> chapters;

}
