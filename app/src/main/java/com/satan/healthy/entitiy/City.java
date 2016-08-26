package com.satan.healthy.entitiy;

/**
 * Created by Satan on 2016/08/16.
 */
public class City {
    private String city;
    private int id;

    public City(String city, int id) {
        this.city = city;
        this.id = id;
    }

    public City() {
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return city;
    }
}
