package org.cetide.hibiscus.interfaces.rest.dto;

public class UserVO{

    private String username;

    private String email;

    private String avatarUrl;

    private String status;

    public UserVO() {
    }

    public UserVO(String username, String email, String avatarUrl, String status) {
        this.username = username;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserVO{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
