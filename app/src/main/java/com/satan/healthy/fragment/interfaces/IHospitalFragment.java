package com.satan.healthy.fragment.interfaces;

import com.satan.healthy.entitiy.Location;

/**
 * HospitalFragment提供的接口，提供一系列方法，供宿主Activity调用
 */

public interface IHospitalFragment extends IFragment{
    /**
     * 设置定位位置
     * @param location 定位位置
     */
    void setLocation(Location location);

    /**
     * 设置选定的城市id
     * @param cityId 城市id
     */
    void setChosenCityId(int cityId);

}
