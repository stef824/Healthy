package com.satan.healthy.presenter;

/**
 * Created by Satan on 2016/08/16.
 */

public interface IHospitalPresenter extends IPresenter {
    /**
     * 根据定位位置加载下一页数据
     *
     * @param x        经度
     * @param y        纬度
     * @param nextPage 下一页页码
     */
    void getHospitalsNearby(double x, double y, int nextPage);

    /**
     * 根据选择的城市加载下一页数据
     *
     * @param cityId   城市id
     * @param nextPage 下一页页码
     */
    void getHospitalsOfCity(int cityId, int nextPage);
}
