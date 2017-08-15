package com.example.timmy.sockettest;

/**
 * Created by Timmy on 2017/8/15.
 */
public class LoginUser {
    public String username ;
    public String password  ;
    public LoginUser(String username, String password) {
        this.username=username;
        this.password=password;
    }
    public String toString()
    {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
