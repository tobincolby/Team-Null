package edu.gatech.teamnull.thdhackathon2017.model;
/**
 * Created by Colby on 9/16/17.
 */

public class Product implements Comparable<Product>{

    private double price;
    private String title;
    private int id;

    /***
     * Don't typically call this constructor
     */
    public Product() {
        this(0, "Test Product", 0.0);
    }

    public Product(int id, String title, double price) {
        this.id = id;
        this.title = title;
        this.price = price;
    }

    public double getPrice() { return  price; }
    public String getTitle() { return title; }
    public int getId() { return id; }


    @Override
    public int compareTo(Product other) {
        return this.id - other.getId();
    }
}
