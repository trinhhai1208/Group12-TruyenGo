package com.example.truyengo.services.impl;

import com.example.truyengo.services.IEncryptorService;

import org.mindrot.jbcrypt.BCrypt;

public class IEncryptorServiceImpl implements IEncryptorService {
    public String hashing(String plainText) {
        return BCrypt.hashpw(plainText, BCrypt.gensalt());
    }

    public boolean checkPassword(String plainText, String hashedPassword) {
        try {
            return BCrypt.checkpw(plainText, hashedPassword);
        } catch (Exception e) {
            System.out.println("Không lấy được dữ liệu. Vui lòng thử lại.");
            return false;
        }
    }
}
