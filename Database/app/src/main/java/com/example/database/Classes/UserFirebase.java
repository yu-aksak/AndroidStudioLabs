package com.example.database.Classes;

public class UserFirebase {
    private String name;
    private String login;
    private String password;

    public UserFirebase(String name, String login, String password) {
        this.name = name;
        this.login = login;
        this.password = password;
    }

    public UserFirebase(){ }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
