package com.dmt.ledonchung.coffeehouse.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Product implements Serializable {
    private String id;
    private String name;
    private String describe;
    private String size;
    private Double price;
    private String type;
    private String urlPhoto;

    public Product() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id.equals(product.id) && name.equals(product.name) && describe.equals(product.describe) && size.equals(product.size) && price.equals(product.price) && type.equals(product.type) && urlPhoto.equals(product.urlPhoto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, describe, size, price, type, urlPhoto);
    }

    public Product(String id, String name, String describe, String size, Double price, String type, String urlPhoto) {
        this.id = id;
        this.name = name;
        this.describe = describe;
        this.size = size;
        this.price = price;
        this.type = type;
        this.urlPhoto = urlPhoto;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", describe='" + describe + '\'' +
                ", size='" + size + '\'' +
                ", price=" + price +
                ", type='" + type + '\'' +
                ", urlPhoto='" + urlPhoto + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();

        map.put("id", id);
        map.put("name", name);
        map.put("describe", describe);
        map.put("size", size);
        map.put("price", price);
        map.put("type", type);
        map.put("urlPhoto", urlPhoto);

        return map;
    }
}
