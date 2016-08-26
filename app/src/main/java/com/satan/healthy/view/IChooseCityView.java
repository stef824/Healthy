package com.satan.healthy.view;

import com.satan.healthy.entitiy.City;
import com.satan.healthy.entitiy.Province;

import java.util.List;

/**
 * Created by Satan on 2016/08/17.
 */

public interface IChooseCityView extends IView {
    /**
     * 设置省份集合
     *
     * @param provinces 省份集合
     */
    void setProvinces(List<Province> provinces);

    /**
     * 设置匹配城市集合
     *
     * @param cities 匹配城市集合
     */
    void setMatchedCities(List<City> cities);
}
