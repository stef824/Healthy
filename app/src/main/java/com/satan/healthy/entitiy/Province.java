package com.satan.healthy.entitiy;

import java.util.List;

/**
 * Created by Satan on 2016/08/16.
 */
public class Province {

    private List<City> citys;
    private int id;
    private String province;

    public Province(String province, List<City> citys, int id) {
        this.province = province;
        this.citys = citys;
        this.id = id;
    }

    public Province() {
    }

    public void setCitys(List<City> citys) {
        this.citys = citys;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public List<City> getCitys() {
        return citys;
    }

    public int getId() {
        return id;
    }

    public String getProvince() {
        return province;
    }
}
