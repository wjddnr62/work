package com.dev.pdf.work.Scene;

public class JobLog_ListItem {

    private String area;
    private String location;
    private String status;
    private String evaluation;
    private String title;
    private String user_id;

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

    public String getDate() {

        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date; // 지역 / 작업현장/ 근무일/ 입금

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }
}
