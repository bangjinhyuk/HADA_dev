package com.hada.pillmanagement;

import com.prolificinteractive.materialcalendarview.CalendarDay;

public class Item {
    private Long id;
    private String name;
    private String date;
    private String day;
    private String lastDate;

    public Item(Long id, String name, String date, String day, String lastDate) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.day = day;
        this.lastDate = lastDate;
    }

    public Item() {
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getName() {
        return name;
    }

    public String getdate() {
        return date;
    }

    public String getDay() {
        return day;
    }
}
