package com.satan.healthy.presenter;

/**
 * Created by Satan on 2016/08/18.
 */

public interface IHopitalDetailPresenter extends IPresenter {
    /**
     * 获取医院详情
     * @param hospitalId 医院id
     */
    void getDetail(int hospitalId);

    /**
     * 获取图片
     * @param img 图片url后缀
     */
    void getImg(String img);
}
