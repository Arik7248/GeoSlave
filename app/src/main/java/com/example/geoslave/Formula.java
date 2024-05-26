package com.example.geoslave;

import java.util.Objects;

public class Formula {
    private String name;
    private int image,fimage;
    private int textSize;
    private String type;
    private String URL;
    private boolean isLiked;

    // Constructor and getters/setters

    public Formula(String name, int image, int fimage, int textSize, String type, String URL) {
        this.name = name;
        this.image = image;
        this.fimage = fimage;
        this.textSize = textSize;
        this.type = type;
        this.URL = URL;
        this.isLiked = false;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }
    public int getFImage() {
        return fimage;
    }

    public int getTextSize() {
        return textSize;
    }

    public String getType() {
        return type;
    }

    public String getURL() {
        return URL;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Formula formula = (Formula) o;
        return Objects.equals(name, formula.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
