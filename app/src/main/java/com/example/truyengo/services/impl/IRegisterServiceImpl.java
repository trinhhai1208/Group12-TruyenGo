package com.example.truyengo.services.impl;

import static com.example.truyengo.dao.ConnectDB.collection;

import com.example.truyengo.models.user.UserAccount;
import com.example.truyengo.services.IRegisterService;

public class IRegisterServiceImpl implements IRegisterService {
    private IUserServicesImpl IUserServicesImpl;

    public IRegisterServiceImpl() {
        IUserServicesImpl = new IUserServicesImpl();
    }

    public boolean register(UserAccount user) throws Exception {
        try {
            if (!IUserServicesImpl.isUsernameExists(user.getUsername())) {
                collection.insertOne(user);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println("Không lấy được dữ liệu. Vui lòng thử lại.");
            return false;
        }
    }
}
