package com.example.tablayoutviewpager.Model;

public class Category {

    int catId;
    private String name,imageUrl;

    public Category(int catId, String name, String imageUrl) {
        this.catId = catId;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
