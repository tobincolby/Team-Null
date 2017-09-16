package edu.gatech.teamnull.thdhackathon2017.model;

/**
 * Created by lucas on 9/16/2017.
 */



public class Customer {

    private String name;
    private int ID;
    public Product[] productsPurchased;

    // Constructor with no products
    public Customer(String name, int ID) {
        this.name = name;
        this.ID = ID;
    }


    public void addProduct(Product newProduct) {

    }

    public void removeProduct() {

    }


    public String getName() {return name;}
    public int getID() {return ID;}
    public Product[] getProducts() {return productsPurchased;}



}
