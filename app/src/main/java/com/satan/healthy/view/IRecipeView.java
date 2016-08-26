package com.satan.healthy.view;

import com.satan.healthy.entitiy.Recipe;

import java.util.List;

/**
 * Created by Satan on 2016/08/23.
 */

public interface IRecipeView extends IView {
    /**
     * 添加到菜谱列表数据源
     *
     * @param recipes 单次请求获得的菜谱列表
     */
    void addToRecipes(List<Recipe> recipes);

    /**
     * 设置菜谱数据源总条目数
     *
     * @param total 总条目数
     */
    void setTotalItem(int total);
}
