package com.example.truyengo.commons;

import com.example.truyengo.models.user.UserAccount;
import com.example.truyengo.models.user.UserInfo;
import com.example.truyengo.services.impl.IBookServiceImpl;
import com.example.truyengo.services.impl.IDelAccountServiceImpl;
import com.example.truyengo.services.impl.IEncryptorServiceImpl;
import com.example.truyengo.services.impl.IForgetPasswordServiceImpl;
import com.example.truyengo.services.impl.ILoginServiceImpl;
import com.example.truyengo.services.impl.IRegisterServiceImpl;
import com.example.truyengo.services.impl.IUserServicesImpl;

import java.util.prefs.Preferences;

public class CurrentUser {
    public static ILoginServiceImpl loginService = new ILoginServiceImpl();
    public static IRegisterServiceImpl registerService = new IRegisterServiceImpl();
    public static IUserServicesImpl userServices = new IUserServicesImpl();
    public static IBookServiceImpl bookService = new IBookServiceImpl();
    public static UserAccount userAccount;
    public static UserInfo userInfo = new UserInfo();
    public static IForgetPasswordServiceImpl forgetPasswordService = new IForgetPasswordServiceImpl();
    public static Preferences pref = Preferences.userRoot().node("rememberMe");
    public static IEncryptorServiceImpl encryptorService = new IEncryptorServiceImpl();
    public static IDelAccountServiceImpl delAccountService = new IDelAccountServiceImpl();
}
