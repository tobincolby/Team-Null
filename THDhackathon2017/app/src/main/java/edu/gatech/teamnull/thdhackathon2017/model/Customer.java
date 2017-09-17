package edu.gatech.teamnull.thdhackathon2017.model;

import java.util.ArrayList;

/**
 * Created by lucas on 9/16/2017.
 */

public class Customer {

    private String name;
    private int ID;
    private ArrayList<Product> productsPurchased = new ArrayList<>();

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


    public String getName() {return name;}
    public int getID() {return ID;}
    public ArrayList<Product> getProducts() {return productsPurchased;}


}
