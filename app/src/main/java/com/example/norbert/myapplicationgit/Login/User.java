package com.example.norbert.myapplicationgit.Login;

import java.util.ArrayList;

public class    User {

    private String phonenumber;
    private String lastname;
    private String firstname;
    private String email;
    private String adress;
    private String image;

    public User(){
    }

    public User(String phonenumber){
        this.phonenumber = phonenumber;
    }


    public User(String phonenumber, String lastname,String firstname,String email,String adress,String image) {
        this.phonenumber = phonenumber;
        this.lastname = lastname;
        this.firstname = firstname;
        this.email = email;
        this.adress = adress;
        this.image = image;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getLastname() {
        return lastname ;
    }

    public String getFirstname(){return firstname;}

    public String getImage() {
        return image;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }
}
