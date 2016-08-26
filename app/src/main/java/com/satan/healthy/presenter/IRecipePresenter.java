package com.satan.healthy.presenter;

/**
 * Created by Satan on 2016/08/23.
 */

public interface IRecipePresenter extends IPresenter {
    /**
     * 获取菜谱列表
     * @param nextPage 下一页页码
     */
    void getRecipeList(int nextPage);
    /**
     * 搜索菜谱
     * @param input 用户输入
     */
    void searchRecipe(String input);

}
