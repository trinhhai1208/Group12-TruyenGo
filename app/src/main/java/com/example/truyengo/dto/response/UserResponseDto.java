package com.example.truyengo.dto.response;

import com.google.gson.annotations.SerializedName;

public class UserResponseDto {
    @SerializedName("id")
    private String id;

    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("birth")
    private String birth;

    @SerializedName("phone")
    private String phone;

    @SerializedName("nationality")
    private String nationality;

    @SerializedName("linkAvatar")
    private String linkAvatar;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("role")
    private String role;

    @SerializedName("address")
    private String address;

    // Getter methods
    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getFullName() {
        return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
    }
    public String getPhone() { return phone; }
    public String getLinkAvatar() { return linkAvatar; }
    public String getRole() { return role; }
    public String getBirth() { return birth; }
    public String getNationality() { return nationality; }
    public String getCreatedAt() { return createdAt; }
    public String getAddress() { return address; }
}