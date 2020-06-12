package com.dev.pdf.work.Data;

public class UserData {
    private static UserData instance;
    private String id;

    public static UserData getInstance() {
        if (instance == null) instance = new UserData();
        return instance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
