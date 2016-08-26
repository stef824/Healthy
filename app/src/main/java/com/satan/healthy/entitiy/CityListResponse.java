package com.satan.healthy.entitiy;

import java.util.List;

/**
 * Created by Satan on 2016/08/17.
 */

public class CityListResponse extends CommonResponse{
    private List<Province> tngou;

    public CityListResponse(boolean status, List<Province> tngou) {
        super(status);
        this.tngou = tngou;
    }

    public CityListResponse() {
    }

    public void setTngou(List<Province> tngou) {
        this.tngou = tngou;
    }

    public List<Province> getTngou() {
        return tngou;
    }
}
