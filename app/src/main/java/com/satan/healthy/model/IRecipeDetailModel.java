package com.satan.healthy.model;

import android.graphics.Bitmap;

import com.satan.healthy.entitiy.Recipe;

import java.util.List;

/**
 * Created by Satan on 2016/08/23.
 */

public interface IRecipeDetailModel extends IModel {
    /**
     * 异步获取菜谱图片
     * @param urlSuffix 图片url后缀
     * @param callback 回调
     */
    void getImg(String urlSuffix, CommonCallback<Bitmap> callback);

    /**
     * 异步获取相关菜谱数据
     * @param recipeId 当前菜谱的id
     * @param recipeName 菜谱名称
     * @param callback 回调
     */
    void getRelatedRecipes(int recipeId,String recipeName, CommonCallback<List<Recipe>> callback);

    /**
     * 异步获取菜谱详情
     * @param id 菜谱id
     * @param callback 回调
     */
    void getRecipeMethod(int id, CommonCallback<Recipe> callback);
}
