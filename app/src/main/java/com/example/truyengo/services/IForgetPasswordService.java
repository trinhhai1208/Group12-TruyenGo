package com.example.truyengo.services;


import com.example.truyengo.models.user.UserAccount;

public interface IForgetPasswordService {
    String getCode(String gmail);

    void ChangePassword(UserAccount user, String newPassword);
}
