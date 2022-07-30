package com.dmt.ledonchung.coffeehouse.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShoppingCart implements Serializable {
    private String id;
    private ArrayList<Cart> carts = new ArrayList<>();
    private String deliveryAway;
    private String addressDelivery = "";
    private double totalPrice = 0;
    private User user;
    private String publicPay;
    private Store store;
    private Date dateOrder;
    public ShoppingCart() {
    }


    public ShoppingCart(ArrayList<Cart> carts, String deliveryAway, String addressDelivery, double totalPrice, User user, String publicPay, Store store) {

        this.carts = carts;
        this.deliveryAway = deliveryAway;
        this.addressDelivery = addressDelivery;
        this.totalPrice = totalPrice;
        this.user = user;
        this.publicPay = publicPay;
        this.store = store;
    }

    public ShoppingCart(ArrayList<Cart> carts, String deliveryAway, String addressDelivery, double totalPrice, User user, String publicPay) {
        this.carts = carts;
        this.deliveryAway = deliveryAway;
        this.addressDelivery = addressDelivery;
        this.totalPrice = totalPrice;
        this.user = user;
        this.publicPay = publicPay;
    }

    public ShoppingCart(String id, ArrayList<Cart> carts, String deliveryAway, String addressDelivery, double totalPrice, User user, String publicPay, Store store) {
        this.id = id;
        this.carts = carts;
        this.deliveryAway = deliveryAway;
        this.addressDelivery = addressDelivery;
        this.totalPrice = totalPrice;
        this.user = user;
        this.publicPay = publicPay;
        this.store = store;
    }

    public Date getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(Date dateOrder) {
        this.dateOrder = dateOrder;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public ArrayList<Cart> getCarts() {
        return carts;
    }

    public void setCarts(ArrayList<Cart> carts) {
        this.carts = carts;
    }

    public String getDeliveryAway() {
        return deliveryAway;
    }

    public void setDeliveryAway(String deliveryAway) {
        this.deliveryAway = deliveryAway;
    }

    public String getAddressDelivery() {
        return addressDelivery;
    }

    public void setAddressDelivery(String addressDelivery) {
        this.addressDelivery = addressDelivery;
    }


    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPublicPay() {
        return publicPay;
    }

    public void setPublicPay(String publicPay) {
        this.publicPay = publicPay;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();

        map.put("dateOrder", dateOrder.toMap());
        map.put("carts", carts);
        map.put("store", store.toMap());
        map.put("deliveryAway", deliveryAway.trim());
        map.put("addressDelivery", addressDelivery.trim());
        map.put("totalPrice", totalPrice);
        map.put("publicPay", publicPay.trim());

        return map;
    }
}
