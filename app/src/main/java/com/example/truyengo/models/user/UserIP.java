package com.example.truyengo.models.user;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;

public class UserIP {
    private String ip;
    private LocalDateTime time;

    public UserIP() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public UserIP(String ip) {
        this.ip = ip;
        this.time = LocalDateTime.now();
    }

    public UserIP(String ip, LocalDateTime time) {
        this.ip = ip;
        this.time = time;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
