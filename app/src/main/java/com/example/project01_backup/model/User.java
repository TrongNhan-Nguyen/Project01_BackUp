package com.example.project01_backup.model;

public class User {
    private String id, name, email, uriAvatar, type, password, stringCreated;
    private long longCreated;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getUriAvatar() {
        return uriAvatar;
    }

    public void setUriAvatar(String uriAvatar) {
        this.uriAvatar = uriAvatar;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStringCreated() {
        return stringCreated;
    }

    public void setStringCreated(String stringCreated) {
        this.stringCreated = stringCreated;
    }

    public long getLongCreated() {
        return longCreated;
    }

    public void setLongCreated(long longCreated) {
        this.longCreated = longCreated;
    }
}
