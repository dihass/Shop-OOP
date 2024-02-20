package com.westminster.shop;

 interface ShoppingManager{ //service requirements specification for the shopping manager
     void addProducts(); //add products

     void removeProducts(); //delete products

     void printListOfProducts(); //print list of products
     void saveProducts(); //save to file
     void loadProducts(); //load from file

}
