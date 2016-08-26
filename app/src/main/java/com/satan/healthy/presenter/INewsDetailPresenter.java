package com.satan.healthy.presenter;

/**
 * Created by Satan on 2016/08/25.
 */

public interface INewsDetailPresenter extends IPresenter {
    /**
     * 获取包资讯详情
     * @param newsId 资讯id
     */
    void getNewsContent(int newsId);
}
