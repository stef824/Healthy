package com.satan.healthy.model;

import android.graphics.Bitmap;

import com.satan.healthy.entitiy.Hospital;

/**
 * Created by Satan on 2016/08/18.
 */

public interface IHospitalDetailModel extends IModel {
    /**
     * 异步获取医院详情
     * @param hospitalId 医院id
     * @param callback 回调
     */
    void getDetail(int hospitalId, CommonCallback<Hospital> callback);

    /**
     * 异步获取图片
     * @param img 图片url后缀
     * @param callback 回调
     */
    void getImg(String img,CommonCallback<Bitmap> callback);

}
