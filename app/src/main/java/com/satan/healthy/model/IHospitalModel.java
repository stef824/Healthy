package com.satan.healthy.model;

import com.satan.healthy.entitiy.HospitalListResponse;

/**
 * Created by Satan on 2016/08/16.
 */

public interface IHospitalModel extends IModel {

    /**
     * 根据定位位置获取下一页数据
     *
     * @param x
     * @param y
     * @param nextPage 下一页页码
     */
    void getHospitalsNearby(double x, double y, int nextPage, CommonCallback<HospitalListResponse> callback);

    /**
     * 根据选择的城市获取下一页数据
     *
     * @param cityId   城市id
     * @param nextPage 下一页页码
     */
    void getHospitalsOfCity(int cityId, int nextPage, CommonCallback<HospitalListResponse> callback);

}
