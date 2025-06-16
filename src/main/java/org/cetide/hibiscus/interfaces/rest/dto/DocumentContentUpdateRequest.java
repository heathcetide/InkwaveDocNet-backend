package org.cetide.hibiscus.interfaces.rest.dto;

public class DocumentContentUpdateRequest {
    private String content;
    private String title;

    private String latestOp;

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getLatestOp() {
        return latestOp;
    }

    public void setLatestOp(String latestOp) {
        this.latestOp = latestOp;
    }
}