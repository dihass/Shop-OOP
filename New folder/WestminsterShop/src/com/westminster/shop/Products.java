/*
 *
 *
 *
 */

package com.westminster.shop;

public abstract class Products {
    // instance variables of products
    private String productID;
    private String productName;
    private int numberOfItems;
    private double price;

    // constructor to initialize the instance variables of the abstract class Products
    public Products(String productID, String productName, int numberOfItems, double price){
        this.productID = productID;
        this.productName = productName;
        this.numberOfItems = numberOfItems;
        this.price = price;
    }
    // abstract method to display the product information

    // getter methods
    public String getProductID(){
        return productID;
    }
    public String getProductName(){
        return productName;
    }
    public int getNumberOfItems(){
        return numberOfItems;
    }
    public double getPrice(){
        return price;
    }
    // setter methods
    public void setProductID(String newProductID){
        this.productID= newProductID;
    }
    public void setProductName(String newProductName){
        this.productName = newProductName;
    }
    public void setNumberOfItems(int newNumberOfItems){
        this.numberOfItems = newNumberOfItems;
    }
    public void setPrice(double newPrice){
        this.price = newPrice;
    }
    public void decreaseNumberOfItems() {
        numberOfItems--;
    }
    public void increaseNumberOfItems(int quantity) {
        this.numberOfItems += quantity;
    }
    public abstract String getInfo();
    public abstract String getProduct();
    public abstract String getCategory();
}





