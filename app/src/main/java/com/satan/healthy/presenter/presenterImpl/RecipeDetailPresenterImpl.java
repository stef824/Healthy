package com.satan.healthy.presenter.presenterImpl;

import android.graphics.Bitmap;

import com.satan.healthy.entitiy.Recipe;
import com.satan.healthy.model.IModel;
import com.satan.healthy.model.IRecipeDetailModel;
import com.satan.healthy.model.modelImpl.RecipeDetailModelImpl;
import com.satan.healthy.presenter.IRecipeDetatilPresenter;
import com.satan.healthy.view.IRecipeDeatailView;

import java.util.List;

/**
 * Created by Satan on 2016/08/23.
 */

public class RecipeDetailPresenterImpl implements IRecipeDetatilPresenter {
    private IRecipeDeatailView view;
    private IRecipeDetailModel model;

    public RecipeDetailPresenterImpl(IRecipeDeatailView view) {
        this.view = view;
        model = new RecipeDetailModelImpl();
    }

    @Override
    public void getImg(String urlSuffix) {
        model.getImg(urlSuffix, new IModel.CommonCallback<Bitmap>() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                view.showImg(bitmap);
            }

            @Override
            public void onError(String error) {
                view.showError(error);
            }
        });
    }

    @Override
    public void getRelatedRecipes(int recipeId, String recipeName) {
        model.getRelatedRecipes(recipeId,recipeName, new IModel.CommonCallback<List<Recipe>>() {
            @Override
            public void onSuccess(List<Recipe> recipes) {
                view.setRelatedRecipes(recipes);
            }

            @Override
            public void onError(String error) {
                view.showError(error);
            }
        });
    }

    @Override
    public void getRecipeMethod(int id) {
        model.getRecipeMethod(id, new IModel.CommonCallback<Recipe>() {

            @Override
            public void onSuccess(Recipe recipe) {
                view.setRecipeMethod(recipe);
            }

            @Override
            public void onError(String error) {
                view.showError(error);
            }
        });
    }
}
