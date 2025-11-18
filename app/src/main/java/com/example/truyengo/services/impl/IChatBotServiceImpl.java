package com.example.truyengo.services.impl;

import com.example.truyengo.services.IChatBotService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.*;

public class IChatBotServiceImpl implements IChatBotService {
    private static final String API_KEY = "AIzaSyC53a7Hl-WqvIX2nJQr77JIA5ZeLtTwAZ4";
    private static final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + API_KEY;

    public static String askGemini(String prompt) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();

        String requestBody = "{ \"contents\": [{ \"parts\": [{ \"text\": \"" + prompt + "\" }] }] }";

        Request request = new Request.Builder()
                .url(GEMINI_URL)
                .post(RequestBody.create(requestBody, MediaType.get("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return "Request failed: " + response;
            }

            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);

            return jsonResponse
                    .getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text")
                    .replace("*", "");
        }
    }
}