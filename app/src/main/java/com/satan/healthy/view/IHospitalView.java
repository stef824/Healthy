package com.satan.healthy.view;

import com.satan.healthy.entitiy.Hospital;

import java.util.List;

/**
 * Created by Satan on 2016/08/16.
 */

public interface IHospitalView extends IView {

    /**
     * 返回数据加到列表数据集合中
     *
     * @param hospitals
     */
    void addToHospitals(List<Hospital> hospitals);

    /**
     * 设置当前列表的总条目数
     *
     * @param totalItem 总条目数
     */
    void setTotalItem(int totalItem);
}
