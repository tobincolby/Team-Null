package edu.gatech.teamnull.thdhackathon2017.model;

import java.util.ArrayList;

/**
 * Created by lucas on 9/16/2017.
 */

public class Customer {
    public static final String customerName = "THDuser";
    private String name;
    private int ID;
    private ArrayList<Product> productsPurchased = new ArrayList<>();

    public static ArrayList<Video> getMySavedVideos() {
        return mySavedVideos;
    }

    public static void addSavedVideo(Video v) {
        mySavedVideos.add(v);
    }

    public static void deleteSavedVideo(Video v) {
        mySavedVideos.remove(v);
    }

    private static ArrayList<Video> mySavedVideos = new ArrayList<>();

    // Constructor with no products
    public Customer(String name, int ID) {
        this.name = name;
        this.ID = ID;
    }

    public Customer(String name, int ID, ArrayList<Product> products) {
        this(name, ID);
        this.productsPurchased = products;
    }


    public void addProduct(Product newProduct) {
        productsPurchased.add(newProduct);
    }

    public void removeProduct(Product removedProduct) {
        productsPurchased.remove(removedProduct);
    }


    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }

    public ArrayList<Product> getProducts() {
        return productsPurchased;
    }

    public String[] getProductsByName() {
        String[] names = new String[productsPurchased.size()];
        for (int i = 0; i < names.length; i++) {
            names[i] = productsPurchased.get(i).getTitle();
        }
        return names;
    }


}
