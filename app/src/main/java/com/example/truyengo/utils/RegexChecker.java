package com.example.truyengo.utils;


import com.example.truyengo.commons.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexChecker {
    public static boolean checkValidEmail(String email) {
        String EMAIL_PATTERN = Patterns.EMAIL_PATTERN;
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean checkValidPhoneNumber(String phoneNumber) {
        String PHONE_NUMBER_PATTERN = Patterns.PHONE_NUMBER_PATTERN;
        Pattern pattern = Pattern.compile(PHONE_NUMBER_PATTERN);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public static boolean checkValidUserName(String username) {
        String USERNAME_PATTERN = Patterns.USERNAME_PATTERN;
        Pattern pattern = Pattern.compile(USERNAME_PATTERN);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    public static boolean checkValidPassword(String password) {
        String PASSWORD_PATTERN = Patterns.PASSWORD_PATTERN;
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

}
