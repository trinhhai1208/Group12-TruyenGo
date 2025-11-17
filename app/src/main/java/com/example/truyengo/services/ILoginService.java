package com.example.truyengo.services;

public interface ILoginService {
    boolean authenticate(String username, String password) throws Exception;

    void saveUser(String username, String password);
}
