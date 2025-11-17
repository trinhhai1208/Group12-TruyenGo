package com.example.truyengo.models.user;

import lombok.Getter;
import lombok.Setter;

import org.bson.types.ObjectId;

@Getter
@Setter
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
}
