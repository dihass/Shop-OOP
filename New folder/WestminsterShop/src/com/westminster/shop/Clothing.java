package com.westminster.shop;

class Clothing extends Products{
    // instance variables unique to clothing
    private String size;
    private String color;
    // constructor to create a new clothing product (instantiates a new object of the class Clothing)
    public Clothing(String productID, String productName, int numberOfItems , double price ,String size,String color){
        super(productID, productName, numberOfItems,price); // calls the constructor of the superclass Products and passess the parameters
        this.size = size;
        this.color = color;
    }
    // getter methods
    public String getSize(){
        return size;
    }
    public String getColor(){
        return color;
    }

    @Override
    public String getInfo() {
        return  this.getSize() + ", " + this.getColor();
    }
    @Override
    public String getProduct() {
        return this.getProductID() + ",\n" + this.getProductName() + ",\n" +this.getInfo();
    }
    @Override
    public String getCategory() {
        return "Clothing";
    }

    // setter methods
    public void setSize(String newSize){
        this.size = newSize;
    }
    public void setColor(String newColor){
        this.color = newColor;
    }
}

