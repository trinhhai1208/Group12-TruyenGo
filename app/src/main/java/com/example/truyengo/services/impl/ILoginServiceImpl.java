package com.example.truyengo.services.impl;


import static com.example.truyengo.commons.CurrentUser.encryptorService;
import static com.example.truyengo.commons.CurrentUser.pref;

import com.example.truyengo.models.user.UserAccount;
import com.example.truyengo.services.ILoginService;

public class ILoginServiceImpl implements ILoginService {
    private final IUserServicesImpl IUserServicesImpl;

    public ILoginServiceImpl() {
        IUserServicesImpl = new IUserServicesImpl();
    }

    public boolean authenticate(String username, String password) throws Exception {
        UserAccount user = null;
        try {
            user = IUserServicesImpl.getUserByUsername(username);
        } catch (Exception e) {
            System.out.println("Không lấy được dữ liệu. Vui lòng thử lại.");
            return false;
        }
        if (user != null && encryptorService.checkPassword(password, user.getPassword())) {
            return true;
        } else {
            return false;
        }
    }

    public void saveUser(String username, String password) {
        pref.put("username", username);
        pref.put("password", password);
    }

}


