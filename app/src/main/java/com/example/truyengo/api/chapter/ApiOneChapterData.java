package com.example.truyengo.api.chapter;

import lombok.Getter;

@Getter
public class ApiOneChapterData {
    private ApiOneChapterJson item;

    public void setItems(ApiOneChapterJson books) {
        this.item = books;
    }
}
