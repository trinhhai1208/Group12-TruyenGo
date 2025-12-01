package com.example.truyengo.services.impl;

import com.example.truyengo.services.IChatBotService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit; // Thêm import này

import okhttp3.*;

public class IChatBotServiceImpl implements IChatBotService {

    // 1. Thay API Key MỚI (Sau khi bạn đã xóa key cũ bị lộ)
    private static final String API_KEY = "AIzaSyBbkOPUAhW32ym8OfeR4YzX_SxSWBwOQ-s".trim();

    // 2. Đổi sang model 'gemini-1.5-flash' (Chạy ổn định hơn gemini-pro)
    private static final String BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent";

    public static String askGemini(String prompt) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        // Xây dựng JSON Body
        JSONObject textPart = new JSONObject();
        textPart.put("text", prompt);
        JSONArray partsArray = new JSONArray();
        partsArray.put(textPart);
        JSONObject contentObject = new JSONObject();
        contentObject.put("parts", partsArray);
        JSONArray contentsArray = new JSONArray();
        contentsArray.put(contentObject);
        JSONObject rootObject = new JSONObject();
        rootObject.put("contents", contentsArray);

        String requestBodyString = rootObject.toString();

        // 3. Xây dựng URL an toàn (Tránh lỗi 404 do thừa khoảng trắng)
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
        urlBuilder.addQueryParameter("key", API_KEY); // Tự động thêm ?key=... chuẩn xác
        String finalUrl = urlBuilder.build().toString();

        // In ra để debug xem URL đúng chưa
        System.out.println("DEBUG_URL: " + finalUrl);

        Request request = new Request.Builder()
                .url(finalUrl)
                .post(RequestBody.create(requestBodyString, MediaType.get("application/json")))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "Unknown error";
                System.out.println("GEMINI_ERROR: " + errorBody);
                return "Lỗi API: " + response.code();
            }

            // Xử lý JSON trả về...
            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);
            try {
                return jsonResponse.getJSONArray("candidates")
                        .getJSONObject(0)
                        .getJSONObject("content")
                        .getJSONArray("parts")
                        .getJSONObject(0)
                        .getString("text");
            } catch (Exception e) {
                return "AI không phản hồi đúng định dạng.";
            }
        }
    }
}