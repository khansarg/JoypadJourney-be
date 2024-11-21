package com.example.joypadjourney.validation;

import java.util.List;
import java.util.Map;

import lombok.Getter;

@Getter
public class HttpErrorResponse {
    private String message;
    private int status;
    private List<String> generalErrors;
    private Map<String, String> errors;

    public static HttpErrorResponse of(String message, int status, Map<String, String> errors, List<String> generalErrors){
        HttpErrorResponse httpErrorResponse = new HttpErrorResponse();
        httpErrorResponse.message = message;
        httpErrorResponse.status = status;
        httpErrorResponse.errors = errors;
        httpErrorResponse.generalErrors = generalErrors;
        return httpErrorResponse;

    }

    public static HttpErrorResponse of(String message, int status){
        HttpErrorResponse httpErrorResponse = new HttpErrorResponse();
        httpErrorResponse.message = message;
        httpErrorResponse.status = status;
        return httpErrorResponse;
    }
}
