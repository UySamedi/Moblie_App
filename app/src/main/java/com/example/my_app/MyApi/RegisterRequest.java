package com.example.my_app.MyApi;

public class RegisterRequest {
    private String name;

    private String email;

    private String password;

    private String confirm_password;

    public RegisterRequest(String name, String email, String password, String confirm_password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.confirm_password = confirm_password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirm_password() {
        return confirm_password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }
}
