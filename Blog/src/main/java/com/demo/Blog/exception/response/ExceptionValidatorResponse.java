package com.demo.Blog.exception.response;

import java.util.List;

public class ExceptionValidatorResponse {

    private String message;
    private int httpStatus;
    private String timestamp;
    private String path;
    private List<String> details;

    public ExceptionValidatorResponse() {
    }

    public ExceptionValidatorResponse(String message, int httpStatus, String timestamp, String path, List<String> details) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
        this.path = path;
        this.details = details;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }
}
