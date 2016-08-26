package com.satan.healthy.entitiy;

/**
 * Created by Satan on 2016/08/16.
 */
public class Hospital {
    private String address;
    private int id;
    private String img;
    private String message;
    private String name;
    private String tel;
    private String level;
    private boolean isMarked;

    public Hospital(String address, int id, String img, String message, String name, String tel) {
        this(address, id, img, name, tel);
        this.message = message;
    }

    public Hospital(String address, int id, String img, String name, String tel) {
        this.address = address;
        this.id = id;
        this.img = img;
        this.name = name;
        this.tel = tel;
    }

    public Hospital() {
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public int getId() {
        return id;
    }

    public String getImg() {
        return img;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public String getTel() {
        return tel;
    }

    public boolean isMarked() {
        return isMarked;
    }

    public void setMarked(boolean marked) {
        isMarked = marked;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
