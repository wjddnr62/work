package com.dev.pdf.work.Data;

public class Worklist_Data {

    String title;
    String location;
    String id;
    int status;
    int job_eval_status;
    String during_work;
    String name;
    String user_address;
    String user_tel;
    String job_kind;
    String career;

    public String getJob_kind() {
        return job_kind;
    }

    public void setJob_kind(String job_kind) {
        this.job_kind = job_kind;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUser_tel() {
        return user_tel;
    }

    public void setUser_tel(String user_tel) {
        this.user_tel = user_tel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuring_work() {
        return during_work;
    }

    public void setDuring_work(String during_work) {
        this.during_work = during_work;
    }

    public int getJob_eval_status() {
        return job_eval_status;
    }

    public void setJob_eval_status(int job_eval_status) {
        this.job_eval_status = job_eval_status;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
