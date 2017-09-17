package edu.gatech.teamnull.thdhackathon2017.model;

import java.util.Random;

/**
 * Created by colby on 9/17/17.
 */

public class Review {

    private String text;
    private int id;
    private Customer author;
    private int rating;
    private String sku;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Review(String text, Customer author, int stars, String sku) {
        this.text = text;
        this.author = author;
        this.rating = stars;
        this.sku = sku;
        Random rand = new Random();
        this.id = rand.nextInt(100);
    }

    public Customer getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public int getRating() {
        return rating;
    }

    public int getId() { return id; }

}
