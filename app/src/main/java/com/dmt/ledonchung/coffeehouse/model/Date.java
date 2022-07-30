package com.dmt.ledonchung.coffeehouse.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Date implements Serializable {
    private int day, month, year;
    private String hour;

    public Date(String hour) {
        this.hour = hour;
    }

    public Date(int day, int month, int year, String hour) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
    }

    public Date() {
    }

    @Override
    public String toString() {
        return day + "/" + month + "/" + year;
    }

    public Date(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("day", day);
        map.put("month", month);
        map.put("year", year);
        map.put("hour", hour);

        return map;
    }
}
