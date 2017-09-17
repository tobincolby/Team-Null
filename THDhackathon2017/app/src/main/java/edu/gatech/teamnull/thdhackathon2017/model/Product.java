package edu.gatech.teamnull.thdhackathon2017.model;

import com.google.api.services.youtube.model.SearchResult;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Colby on 9/16/17.
 */

public class Product implements Comparable<Product>, Serializable{

    private double price;
    private String title;
    private static int idCounter;
    private int id;
    private String sku;

    /***
     * Don't typically call this constructor
     */
    public Product() {
        this("Test Product", 0.0, "ASSDF3");
    }

    public Product(String title, double price, String sku) {
        this.title = title;
        this.price = price;
        this.sku = sku;
        this.id = idCounter++;
    }

    public Product(int id, String title, double price, String sku) {
        this.title = title;
        this.price = price;
        this.sku = sku;
        this.id = id;
    }

    public double getPrice() { return  price; }
    public String getTitle() { return title; }
    public int getId() { return id; }
    public String getSku() { return sku; }

    @Override
    public String toString() {
        return "{title : '" + title + "', price : " + price +", sku : '" + sku + "'}";
    }


    public static String toGSON(Product product) {
        Gson gson = new Gson();
        return gson.toJson(product);
    }

    public static Product fromGSON(String json) {
        Gson gson = new Gson();
        return (Product) gson.fromJson(json, Product.class);
    }


    @Override
    public int compareTo(Product other) {
        return this.id - other.getId();
    }

//    public List<SearchResult> searchForVideos() {
//        Search mySearch = new Search(title + " tutorial");
//        long startTime = System.nanoTime();
//        long fiveSeconds = 4 * (int) Math.pow(10, 9);
//        // definitely not sure this is the best way to do this but i'm not sure how to
//        // make sure the result comes back before its sent
//        while (!mySearch.isQueryDone() && System.nanoTime() - startTime < fiveSeconds) {
//
//        }
//        return mySearch.getResults();
//    }
}
