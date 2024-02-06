package com.example.tablayoutviewpager.Model;

public class Pictures {

    int id;
    private String imageUrl,name;

    private boolean isFavorite;

    public Pictures(int id, String imageUrl, String name, boolean isFavorite) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.isFavorite = isFavorite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
