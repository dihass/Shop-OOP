package com.westminster.shop;

import java.io.Serializable;

class User implements Serializable {
    //instance variables of user
    private String userName;
    private String password;
    private String email;

    private boolean FirstTimeDiscount;
    // constructor to create a new user account (instantiates a new object of the class User)
    public User(String userName,String password,String email){
        this.userName=userName;
        this.password=password;
        this.email=email;
    }
    public User(String userName, String password){
        this.userName=userName;
        this.password=password;

    }
    // getter methods
    public String getUserName(){
        return userName;
    }
    public String getPassword(){
        return password;
    }
    public String getEmail(){
        return email;
    }
    //getUser method

    // setter methods
    public void setUserName(String newUserName){
        this.userName=newUserName;
    }
    public void setPassword(String newPassword){
        this.password=newPassword;
    }
    public boolean isFirstTimeDiscount(){
        return FirstTimeDiscount;
    }
   public void setFirstTimeDiscount(boolean FirstTimeDiscount){
        this.FirstTimeDiscount=FirstTimeDiscount;
   }
}

