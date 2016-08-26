package com.satan.healthy.view;

import android.graphics.Bitmap;

import com.satan.healthy.entitiy.Recipe;

import java.util.List;

/**
 * Created by Satan on 2016/08/23.
 */

public interface IRecipeDeatailView extends IView {
    /**
     * 显示菜谱图片
     * @param bitmap 图片
     */
    void showImg(Bitmap bitmap);

    /**
     * 设置相关菜谱数据源
     * @param recipes 菜谱数据源
     */
    void setRelatedRecipes(List<Recipe> recipes);

    /**
     * 设置详菜谱制作方法
     * @param recipe 包含制作方法的菜谱对象
     */
    void setRecipeMethod(Recipe recipe);
}
