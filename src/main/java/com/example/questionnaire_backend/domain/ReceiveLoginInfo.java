package com.example.questionnaire_backend.domain;

public class ReceiveLoginInfo {
    private String username;
    private String password;
    private Boolean remember;


    public String getUsername() {
        return username;
    }

    public void setUsername(String s) {
        this.username = s;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String s) {
        this.password = s;
    }

    public Boolean getRemember() {
        return remember;
    }

    public void setRemember(Boolean r) {
        this.remember = r;
    }
}
