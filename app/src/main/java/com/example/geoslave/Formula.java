package com.example.geoslave;


import android.widget.ImageButton;

public class Formula {
    String name;
    int image, textSize;
    boolean isLiked=false;
    ImageButton likeBT;

    public Formula(String name, int image, int textSize) {
        this.name = name;
        this.image = image;
        this.textSize = textSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textsize) {
        this.textSize = textsize;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        this.isLiked = liked;
    }

}
