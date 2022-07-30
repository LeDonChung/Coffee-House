package com.dmt.ledonchung.coffeehouse.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Store implements Serializable {
    private String id;
    private String address;
    private String start;
    private String end;
    private String phone;
    private String urlPhoto;

    public Store() {
    }

    public Store(String id, String address, String start, String end, String phone, String urlPhoto) {
        this.id = id;
        this.address = address;
        this.start = start;
        this.end = end;
        this.phone = phone;
        this.urlPhoto = urlPhoto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    @Override
    public String toString() {
        return "Store{" +
                "id='" + id + '\'' +
                ", address='" + address + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", phone='" + phone + '\'' +
                ", urlPhoto='" + urlPhoto + '\'' +
                '}';
    }


    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("address", address);

        map.put("start", start);
        map.put("end", end);
        map.put("phone", phone);
        map.put("urlPhoto", urlPhoto);

        return map;
    }

}
