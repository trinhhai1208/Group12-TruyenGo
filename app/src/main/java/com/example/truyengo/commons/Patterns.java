package com.example.truyengo.commons;

public class Patterns {
    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9]+[@]+[a-zA-Z0-9]+[.]+[a-zA-Z0-9]+$";
    public static final String PHONE_NUMBER_PATTERN = "^[0-9]*$";
    public static final String USERNAME_PATTERN = "^^(?=.{8,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$";
    public static final String PASSWORD_PATTERN = "^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$";
}
