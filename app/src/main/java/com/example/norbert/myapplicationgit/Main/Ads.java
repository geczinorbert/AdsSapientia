package com.example.norbert.myapplicationgit.Main;

public class Ads {
    private String title;
    private String shortDescription;
    private String longDescription;
    private String phoneNumber;
    private String locationText;
    private String image;
    private String author;
    public Ads() {
    }

    public Ads(String title, String shortDescription, String longDescription, String phoneNumber,
               String locationText, String image,String author) {
        this.title = title;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.phoneNumber = phoneNumber;
        this.locationText = locationText;
        this.image = image;
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLocationText() {
        return locationText;
    }

    public void setLocationText(String locationText) {
        this.locationText = locationText;
    }
}
