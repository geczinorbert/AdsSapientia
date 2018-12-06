package com.example.norbert.myapplicationgit.Main;

public class Ads {
    private String Title;
    private String ShortDesc;

    public Ads(String title, String shortDesc) {
        Title = title;
        ShortDesc = shortDesc;
    }

    public Ads() {
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getShortDesc() {
        return ShortDesc;
    }

    public void setShortDesc(String shortDesc) {
        ShortDesc = shortDesc;
    }
}
