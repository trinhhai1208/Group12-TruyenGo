package com.example.truyengo.ui.main;

import android.app.Application;

public class MyApplication extends Application {

    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // Tại đây bạn có thể khởi tạo các cấu hình global
        // Ví dụ: Cấu hình Notification channel, Analytics, v.v.
    }

    public static MyApplication getInstance() {
        return instance;
    }
}