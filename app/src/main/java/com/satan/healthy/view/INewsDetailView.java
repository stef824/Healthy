package com.satan.healthy.view;

import com.satan.healthy.entitiy.News;

/**
 * Created by Satan on 2016/08/25.
 */

public interface INewsDetailView extends IView {
    /**
     * 设置资讯内容
     * @param news 包含内容的资讯对象
     */
    void setNewsContent(News news);
}
