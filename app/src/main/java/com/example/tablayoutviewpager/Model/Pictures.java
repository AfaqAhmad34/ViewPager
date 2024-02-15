package com.example.tablayoutviewpager.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Pictures implements Parcelable {

    int id;
    private String imageUrl,name;

    private boolean isFavorite;
    int catId;

    public Pictures(int id, String imageUrl, String name, boolean isFavorite, int catId) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.isFavorite = isFavorite;
        this.catId = catId;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
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

    protected Pictures(Parcel in) {
        id = in.readInt();
        imageUrl = in.readString();
        name = in.readString();
        isFavorite = in.readByte() != 0;
    }

    public static final Creator<Pictures> CREATOR = new Creator<Pictures>() {
        @Override
        public Pictures createFromParcel(Parcel in) {
            return new Pictures(in);
        }

        @Override
        public Pictures[] newArray(int size) {
            return new Pictures[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(imageUrl);
        dest.writeString(name);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
    }
}
