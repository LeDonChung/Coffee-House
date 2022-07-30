package com.dmt.ledonchung.coffeehouse.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Cart implements Serializable {
    private Product product;
    private String size;
    private double totalPrice;
    private int totalCount;

    public Cart(Product product, String size, double totalPrice, int totalCount) {
        this.product = product;
        this.size = size;
        this.totalPrice = totalPrice;
        this.totalCount = totalCount;
    }

    public Cart() {
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "product=" + product +
                ", size='" + size + '\'' +
                ", totalPrice=" + totalPrice +
                ", totalCount=" + totalCount +
                '}';
    }
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("size", size);
        map.put("product", product.toMap());
        map.put("totalCount", totalCount);
        map.put("totalPrice", totalPrice);
        return map;
    }
}
