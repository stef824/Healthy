package com.satan.healthy.entitiy;

import java.io.Serializable;

/**
 * Created by Satan on 2016/08/23.
 */
public class Recipe implements Serializable{
    private String name;
    private String img;
    private int id;
    private String food;
    private String keywords;
    private String message;

    public Recipe(String name, String img, int id, String food, String keywords, String message) {
        this.name = name;
        this.img = img;
        this.id = id;
        this.food = food;
        this.keywords = keywords;
        this.message = message;
    }

    public Recipe() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
