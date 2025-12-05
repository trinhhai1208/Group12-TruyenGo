package com.example.truyengo.dto.response;

import com.google.gson.annotations.SerializedName;

public class LastReadHistoryResponseDto {
    @SerializedName("lastReadChapter")
    int lastReadChapter;

    public int getLastReadChapter() {
        return lastReadChapter;
    }

    public void setLastReadChapter(int lastReadChapter) {
        this.lastReadChapter = lastReadChapter;
    }
}
