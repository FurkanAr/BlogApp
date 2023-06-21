package com.demo.Blog.response;

public class TagResponse {

    private Long id;
    private String name;

    public TagResponse() {
    }

    public TagResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TagResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
