package edu.gatech.teamnull.thdhackathon2017.model;

import java.util.Random;

/**
 * Created by colby on 9/17/17.
 */

public class Review {

    private String text;
    private int id;
    private String author;
    private int rating;
    private String sku;
    private String productTitle;

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Review(String text, String author, int stars, String sku, String productTitle) {
        this.text = text;
        this.author = author;
        this.rating = stars;
        this.sku = sku;
        this.productTitle = productTitle;
        Random rand = new Random();
        this.id = rand.nextInt(100);
    }

    public String getAuthor() {
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
