package com.satan.healthy.model;

import com.satan.healthy.entitiy.City;
import com.satan.healthy.entitiy.Province;

import java.util.List;

/**
 * Created by Satan on 2016/08/17.
 */

public interface IChooseCityModel extends IModel {
    /**
     * 异步获取省市数据集合
     * @param callback 回调
     */
    void getProvinceAndCities(CommonCallback<List<Province>> callback);

    /**
     * 获取匹配城市数据集合
     * @param provinces 省市数据集合
     * @param input 用户输入
     * @param callback 回调
     */
    void getMatchedCities(List<Province> provinces,String input,CommonCallback<List<City>> callback);
}
