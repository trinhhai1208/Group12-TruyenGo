package com.example.truyengo.models.user;

import android.os.Build;

import androidx.annotation.RequiresApi;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
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
}
