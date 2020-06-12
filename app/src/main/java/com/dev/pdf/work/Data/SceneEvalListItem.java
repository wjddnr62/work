package com.dev.pdf.work.Data;

public class SceneEvalListItem {

    private String name;
    private String job_content;
    private String eval;
    private String address;
    private String title;
    private String location;
    private String user_id;
    private String user_key;

    public String getUser_key() {
        return user_key;
    }

    public void setUser_key(String user_key) {
        this.user_key = user_key;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    private String tel;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob_content() {
        return job_content;
    }

    public void setJob_content(String job_content) {
        this.job_content = job_content;
    }

    public String getEval() {
        return eval;
    }

    public void setEval(String eval) {
        this.eval = eval;
    }
}
