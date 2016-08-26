package com.satan.healthy.presenter;

/**
 * Created by Satan on 2016/08/23.
 */

public interface IRecipeDetatilPresenter extends IPresenter {
    /**
     * 获取图片
     * @param urlSuffix 图片url后缀
     */
    void getImg(String urlSuffix);

    /**
     * 获取相关菜谱
     * @param recipeId 当前菜谱id
     * @param recipeName 菜谱名称
     */
    void getRelatedRecipes(int recipeId,String recipeName);

    /**
     * 根据菜谱id获取菜谱制作方法
     * @param id 菜谱id
     */
    void getRecipeMethod(int id);
}
