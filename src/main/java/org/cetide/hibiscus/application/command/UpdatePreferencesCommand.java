package org.cetide.hibiscus.application.command;

public class UpdatePreferencesCommand {
    private Boolean themeDark;
    private Boolean emailNotifications;

    public Boolean getThemeDark() {
        return themeDark;
    }

    public void setThemeDark(Boolean themeDark) {
        this.themeDark = themeDark;
    }

    public Boolean getEmailNotifications() {
        return emailNotifications;
    }

    public void setEmailNotifications(Boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }
}
