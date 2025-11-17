package com.example.truyengo.api;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiGet {
    private static final OkHttpClient client = new OkHttpClient();

    public static String getApi(String urlString) {
        String responseBody = "";

        Request request = new Request.Builder()
                .url(urlString)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                responseBody = response.body().string();
            } else {
                responseBody = "Request failed: " + response.code();
            }
        } catch (IOException e) {
            e.printStackTrace();
            responseBody = "Error: " + e.getMessage();
        }

        return responseBody;
    }
}