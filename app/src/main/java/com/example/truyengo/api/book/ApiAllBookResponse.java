package com.example.truyengo.api.book;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiAllBookResponse {
    private String status;
    private String message;
    private ApiAllBookData data;

}
