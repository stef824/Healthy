package com.satan.healthy.model;

import com.satan.healthy.entitiy.RecipeResponse;

/**
 * Created by Satan on 2016/08/23.
 */

public interface IRecipeModel extends IModel {
    /**
     * 异步获取下一页菜谱数据
     *
     * @param nextPage 下一页页码
     * @param callback 回调
     */
    void getRecipeList(int nextPage, CommonCallback<RecipeResponse> callback);

    /**
     * 异步搜索菜谱
     *
     * @param input    用户输入的搜索关键词
     * @param callback 回调
     */
    void searcheRecipe(String input, CommonCallback<RecipeResponse> callback);
}
