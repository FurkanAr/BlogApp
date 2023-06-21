package com.demo.Blog.request;

public class TagRequest {

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
