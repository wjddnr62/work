package com.dev.pdf.work.Scene;

public class WorkListItem {

    private String area;
    private String location;
    private String workDay;
    private String money;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title; // 지역 / 작업현장/ 근무일/ 입금

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

    public String getWorkDay() {
        return workDay;
    }

    public void setWorkDay(String workDay) {
        this.workDay = workDay;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
