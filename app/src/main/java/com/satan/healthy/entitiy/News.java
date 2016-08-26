package com.satan.healthy.entitiy;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Satan on 2016/08/24.
 */
public class News extends MultiItemEntity {
    public static int ONE_ITEM_PER_ROW = 2;
    public static int TOW_ITEMS_PER_ROW = 1;

    private String description;
    private int id;
    private String img;
    private String keywords;
    private long time;
    private String title;
    private String message;

    public News(String description, int id, String img, String keywords, long time, String title, String message) {
        this.description = description;
        this.id = id;
        this.img = img;
        this.keywords = keywords;
        this.time = time;
        this.title = title;
        this.message = message;
    }

    public News() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessag(String message) {
        this.message = message;
    }
}
