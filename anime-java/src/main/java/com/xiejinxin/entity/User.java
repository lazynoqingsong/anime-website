package com.xiejinxin.entity;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String username;
    private String password;

    // 临时添加 getter/setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
