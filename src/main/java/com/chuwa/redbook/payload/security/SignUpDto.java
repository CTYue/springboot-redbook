package com.chuwa.redbook.payload.security;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author b1go
 * @date 6/26/22 5:25 PM
 */
public class SignUpDto {
    private String name;
    @JsonProperty(value = "account")
    private String account;
    private String email;
    private String password;

    public SignUpDto(String name, String account, String email, String password) {
        this.name = name;
        this.account = account;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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
}
