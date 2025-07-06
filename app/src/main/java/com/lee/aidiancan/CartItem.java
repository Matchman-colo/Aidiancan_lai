package com.lee.aidiancan;

public class CartItem {
    private long id;
    private String name;
    private double price;
    private int quantity;
    private int imageResourceId;

    public CartItem(long id, String name, double price, int quantity, int imageResourceId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageResourceId = imageResourceId;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
} 