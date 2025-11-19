package com.example.truyengo.api.search;

import java.util.List;

public class ApiSearchBookData {
    private List<ApiSearchBookJson> items;
    private String titlePage;

    public List<ApiSearchBookJson> getItems() {
        return items;
    }

    public void setItems(List<ApiSearchBookJson> books) {
        this.items = books;
    }

    public String getTitlePage() {
        return titlePage;
    }

    public void setTitlePage(String titlePage) {
        this.titlePage = titlePage;
    }
}
