package com.cws.cwslife.model;

public class ChangePassword {

    private Long id;

    private String currentPassword;

    private String newPassword;

    private String confirmPassword;

    // EMPTY CONSTRUCTOR
    public ChangePassword() {
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}