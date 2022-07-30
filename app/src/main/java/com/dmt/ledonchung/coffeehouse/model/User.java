package com.dmt.ledonchung.coffeehouse.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User implements Serializable {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String id;
    private String urlPhoto;
    private Date dayOfBirth;
    private Boolean gender;
    private List<Product> favoriteProduct;

    public User() {

    }

    public User(String firstName, String lastName, String phoneNumber, Boolean gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }

    public User(String firstName, String lastName, String phoneNumber, String idUser, String urlImage, Date birtDay, Boolean gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.id = idUser;
        this.urlPhoto = urlImage;
        this.dayOfBirth = birtDay;
        this.gender = gender;
    }

    public User(String firstName, String lastName, String email, String phoneNumber, String idUser, String urlImage, Date birtDay, Boolean gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.id = idUser;
        this.urlPhoto = urlImage;
        this.dayOfBirth = birtDay;
        this.gender = gender;
    }

    protected User(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        phoneNumber = in.readString();
        id = in.readString();
        urlPhoto = in.readString();
        byte tmpGender = in.readByte();
        gender = tmpGender == 0 ? null : tmpGender == 1;
    }



    public List<Product> getFavoriteProduct() {
        return favoriteProduct;
    }

    public void setFavoriteProduct(List<Product> favoriteProduct) {
        this.favoriteProduct = favoriteProduct;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public Date getDayOfBirth() {
        return dayOfBirth;
    }

    public void setDayOfBirth(Date dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", id='" + id + '\'' +
                ", urlPhoto='" + urlPhoto + '\'' +
                ", dayOfBirth=" + dayOfBirth +
                ", gender=" + gender +
                ", favoriteProduct=" + favoriteProduct +
                '}';
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("firstName", firstName);
        result.put("lastName", lastName);
        result.put("phoneNumber", phoneNumber);
        result.put("dayOfBirth", dayOfBirth);
        result.put("gender", gender);
        return result;
    }
    public Map<String, Object> toMapUpdate() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("firstName", firstName);
        result.put("lastName", lastName);
        result.put("phoneNumber", phoneNumber);
        result.put("gender", gender);
        return result;
    }
}
