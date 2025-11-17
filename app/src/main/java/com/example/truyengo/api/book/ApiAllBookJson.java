package com.example.truyengo.api.book;

import com.example.truyengo.models.book.BookCategory;
import com.example.truyengo.models.book.BookChapterLastest;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

}
