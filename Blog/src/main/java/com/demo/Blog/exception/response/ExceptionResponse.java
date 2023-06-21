package com.demo.Blog.exception.response;

import org.springframework.http.HttpStatus;

public class ExceptionResponse {

    private final String message;
    private final int httpStatus;
    private final String timestamp;
    private final String path;


    public ExceptionResponse(String message, int httpStatus, String timestamp, String path) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
        this.path = path;
    }

    public String getMessage() {
        return message;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getPath() {
        return path;
    }
}
