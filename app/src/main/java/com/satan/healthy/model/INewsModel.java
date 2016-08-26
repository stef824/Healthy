package com.satan.healthy.model;

import com.satan.healthy.entitiy.NewsResponse;

/**
 * Created by Satan on 2016/08/24.
 */

public interface INewsModel extends IModel {
    /**
     * 异步获取资讯列表数据源
     * @param page 下一页页码
     * @param callback 回调
     */
    void getNewses(int page,CommonCallback<NewsResponse> callback);
}
