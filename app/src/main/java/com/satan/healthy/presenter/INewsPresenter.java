package com.satan.healthy.presenter;

/**
 * Created by Satan on 2016/08/24.
 */

public interface INewsPresenter extends IPresenter {
    /**
     * 获取资讯列表源数据
     * @param page 页码
     */
    void getNewses(int page);
}
