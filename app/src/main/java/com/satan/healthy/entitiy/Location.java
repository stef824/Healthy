package com.satan.healthy.entitiy;

import com.amap.api.services.core.LatLonPoint;

/**
 * Created by Satan on 2016/08/16.
 */

public class Location {
    private String city;
    private String district;
    private String street;
    private String aio;
    private LatLonPoint mLatLonPoint;

    public Location(String city, String district, String street, String aio,LatLonPoint latLonPoint) {
        mLatLonPoint = latLonPoint;
        this.city = city;
        this.district = district;
        this.street = street;
        this.aio = aio;
    }

    public Location() {
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setAio(String aio) {
        this.aio = aio;
    }

    public void setLatLonPoint(LatLonPoint latLonPoint) {
        mLatLonPoint = latLonPoint;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public String getStreet() {
        return street;
    }

    public String getAio() {
        return aio;
    }

    public LatLonPoint getLatLonPoint() {
        return mLatLonPoint;
    }
}
