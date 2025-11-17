package com.example.truyengo.api.chapter;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiOneChapterResponse {
    private String status;
    private String message;
    private ApiOneChapterData data;

}
