package org.personalblogapi.dto;

import jakarta.validation.constraints.NotEmpty;

public class BlogRequest {

    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    public BlogRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public BlogRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
