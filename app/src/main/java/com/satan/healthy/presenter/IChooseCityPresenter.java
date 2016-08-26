package com.satan.healthy.presenter;

import com.satan.healthy.entitiy.Province;

import java.util.List;

/**
 * Created by Satan on 2016/08/17.
 */

public interface IChooseCityPresenter extends IPresenter{
    /**
     * 获取省市数据
     */
    void getProvincesAndCities();
    /**
     * 根据用户输入匹配城市
     */
    void getMatchedCities(List<Province> provinces,String input);
}
