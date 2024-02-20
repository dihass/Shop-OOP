package com.westminster.shop;

class Electronics extends Products{
    //instance variables unique to electronics
    private String brand;
    private int warrantyPeriod;
    // constructor to create a new electronics product (instantiates a new object of the class Electronics)
    public Electronics(String productID, String productName, int numberOfItems,double price, String brand, int warrantyPeriod){
        super(productID, productName, numberOfItems,price); // calls the constructor of the superclass Products and passess the parameters
        this.brand = brand;
        this.warrantyPeriod = warrantyPeriod;
    }
    // getter methods
    public String getBrand(){
        return brand;
    }
    public int getWarrantyPeriod(){
        return warrantyPeriod;
    }
    @Override
    public String getInfo() {
        return this.getBrand() + ", " + this.getWarrantyPeriod();
    }
    @Override
    public String getProduct() {
        return this.getProductID() + ",\n" + this.getProductName() + ",\n" +this.getInfo();
    }
    @Override
    public String getCategory() {
        return "Electronics";
    }
    // setter methods
    public void setBrand(String newBrand){
        this.brand = newBrand;
    }
    public void setWarrantyPeriod(int newWarrantyPeriod){
        this.warrantyPeriod = newWarrantyPeriod;
    }
}
