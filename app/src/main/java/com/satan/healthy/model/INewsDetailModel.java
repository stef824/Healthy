package com.satan.healthy.model;

import com.satan.healthy.entitiy.News;

/**
 * Created by Satan on 2016/08/25.
 */

public interface INewsDetailModel extends IModel {
    /**
     * 异步获取资讯详情
     * @param newsId 资讯id
     * @param callback 回调
     */
    void getNewsContent(int newsId, CommonCallback<News> callback);
}
