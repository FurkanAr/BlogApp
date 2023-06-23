package com.demo.Blog.request;

import javax.validation.constraints.NotEmpty;

public class TagRequest {
    @NotEmpty(message = "Please enter tag name")
    private String name;

    public TagRequest() {
    }

    public TagRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TagRequest{" +
                "name='" + name + '\'' +
                '}';
    }


}
