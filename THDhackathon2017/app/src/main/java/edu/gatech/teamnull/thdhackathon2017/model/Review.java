package edu.gatech.teamnull.thdhackathon2017.model;

/**
 * Created by colby on 9/17/17.
 */

public class Review {

    private String text;
    private Customer author;
    private int stars;

    public Review(String text, Customer author, int stars) {
        this.text = text;
        this.author = author;
        this.stars = stars;
    }

    public Customer getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public int getStars() {
        return stars;
    }

}
