package com.example.truyengo.api.search;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiSearchBookData {
    private List<ApiSearchBookJson> items;
    private String titlePage;

}
