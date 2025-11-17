package com.example.truyengo.services;


import com.example.truyengo.models.user.UserAccount;

public interface IRegisterService {
    boolean register(UserAccount user) throws Exception;
}
