package com.satan.healthy.view;

import com.satan.healthy.entitiy.News;

import java.util.List;

/**
 * Created by Satan on 2016/08/24.
 */

public interface INewsView extends IView {
    /**
     * 设置资讯数据源
     * @param newses
     */
    void setNewses(List<News> newses);

    /**
     * 设置资讯总条目数
     * @param totalItems 总条目数
     */
    void setTotalItems(int totalItems);
}
