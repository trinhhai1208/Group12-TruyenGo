package com.example.truyengo.models.user;

import lombok.Getter;
import lombok.Setter;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

@Getter
@Setter
public class UserInfo {
    @BsonProperty("_id")
    private ObjectId id;

    @BsonProperty("fullName")
    private String fullName;

    @BsonProperty("email")
    private String email;

    @BsonProperty("phoneNumber")
    private String phoneNumber;

    @BsonProperty("birthday")
    private int birthday;

    @BsonProperty("sex")
    private String sex;

    @BsonProperty("colorFix")
    private String colorFix;

    @BsonProperty("colorMain1")
    private String colorMain1;

    @BsonProperty("colorMain2")
    private String colorMain2;

    @BsonProperty("colorMain3")
    private String colorMain3;

    @BsonProperty("notification")
    private boolean notification;

    // Constructor mặc định (bắt buộc)
    public UserInfo() {
        this.notification = false;
    }

    public UserInfo(ObjectId id) {
        this.id = id;
        this.notification = false;
    }

    // Constructor đầy đủ
    public UserInfo(ObjectId id, String fullName, String email, String phoneNumber, int birthday, String sex, String colorFix, String colorMain1, String colorMain2, String colorMain3) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.sex = sex;
        this.colorFix = colorFix;
        this.colorMain1 = colorMain1;
        this.colorMain2 = colorMain2;
        this.colorMain3 = colorMain3;
        this.notification = false;
    }


    public UserInfo(String fullname, int i, String email, String phoneNumber, String gender, UserAccount user) {
        this.id = user.getId();
        this.fullName = fullname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthday = i;
        this.sex = gender;
        this.notification = false;
    }

    public boolean getNotification() {
        return notification;
    }
}