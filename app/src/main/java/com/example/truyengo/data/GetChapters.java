package com.example.truyengo.data;

import static com.example.truyengo.api.ApiGet.getApi;

import com.example.truyengo.api.chapter.ApiListChapterData;
import com.example.truyengo.api.chapter.ApiListChapterJson;
import com.example.truyengo.api.chapter.ApiOneChapterJson;
import com.example.truyengo.api.chapter.ApiOneChapterResponse;
import com.example.truyengo.models.book.Book;
import com.example.truyengo.models.chapter.AllChapters;
import com.example.truyengo.models.chapter.Chapter;
import com.google.gson.Gson;

import java.util.ArrayList;

public class GetChapters {
    public ArrayList<AllChapters> getListChapters(Book book) {
        ArrayList<AllChapters> chapters = new ArrayList<>();
        for (ApiListChapterData a : book.getChapters()) {
            for (ApiListChapterJson b : a.getServer_data()) {
                chapters.add(new AllChapters(b.getFilename(), b.getChapter_name(), b.getChapter_api_data()));
            }
        }
        return chapters;
    }

    public Chapter getChapter(AllChapters chapters) {
        Chapter chapter = new Chapter();
        try {
            String apiUrl = chapters.getChapterData();
            String jsonData = getApi(apiUrl);
            if (jsonData != null && !jsonData.isEmpty()) {
                Gson gson = new Gson();
                ApiOneChapterResponse apiResponse = gson.fromJson(jsonData, ApiOneChapterResponse.class);
                if (apiResponse != null && apiResponse.getData() != null) {
                    ApiOneChapterJson a = apiResponse.getData().getItem();
                    chapter = new Chapter(a.getId(), a.getChapter_name(), a.getChapter_path(), a.getChapter_image());
                }
            }
        } catch (Exception e) {
            System.out.println("Không lấy được dữ liệu. Vui lòng thử lại.");
        }
        return chapter;
    }
}
