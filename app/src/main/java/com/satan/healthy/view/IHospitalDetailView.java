package com.satan.healthy.view;

import android.graphics.Bitmap;

import com.satan.healthy.entitiy.Hospital;

/**
 * Created by Satan on 2016/08/18.
 */

public interface IHospitalDetailView extends IView {
    /**
     * 设置详情
     * @param hospital 包含详情的医院对象
     */
    void setDetail(Hospital hospital);

    /**
     * 设置图片
     * @param img 图片
     */
    void setImg(Bitmap img);
}
