package com.example.norbert.myapplicationgit.Login;

import java.util.ArrayList;

public class    User {

    private String phonenumber;
    private String lastname;
    private String firstname;
    private String image;
    private ArrayList<String> intrests;
    private Boolean admin;

    public User(){
    }

    public User(String phonenumber){
        this.phonenumber = phonenumber;
    }

    public User(String phonenumber, String lastname,String firstname, String image, ArrayList<String> intrests, Boolean admin) {
        this.phonenumber = phonenumber;
        this.lastname = lastname;
        this.firstname = firstname;
        this.image = image;
        this.intrests = intrests;
        this.admin = admin;
    }

    public User(String phonenumber, String lastname,String firstname) {
        this.phonenumber = phonenumber;
        this.lastname = lastname;
        this.firstname = firstname;
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

    public ArrayList<String> getIntrests() {
        return intrests;
    }

    public Boolean getAdmin() {
        return admin;
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

    public void setIntrests(ArrayList<String> intrests) {
        this.intrests = intrests;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

}
