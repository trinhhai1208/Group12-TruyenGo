package com.example.truyengo.models.user;

import org.bson.types.ObjectId;

public class UserAccount {
    private ObjectId id;
    private String username;
    private String password;
    private Role role;
    private String status;

    public UserAccount() {
        id = null;
        username = "";
        password = "";
        role = Role.USER;
        status = "ACTIVE";

    }

    public UserAccount(String username, String password) {
        this.username = username;
        this.password = password;
        this.role = Role.USER;
        status = "ACTIVE";

    }

    public UserAccount(String username, String password, String fullName, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
        status = "ACTIVE";

    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
