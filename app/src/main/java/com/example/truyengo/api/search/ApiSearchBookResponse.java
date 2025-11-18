package com.example.truyengo.api.search;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiSearchBookResponse {
    private String status;
    private String message;
    private ApiSearchBookData data;

}
