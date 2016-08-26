package com.satan.healthy.presenter.presenterImpl;

import com.satan.healthy.entitiy.RecipeResponse;
import com.satan.healthy.model.IModel.CommonCallback;
import com.satan.healthy.model.IRecipeModel;
import com.satan.healthy.model.modelImpl.RecipeModelImpl;
import com.satan.healthy.presenter.IRecipePresenter;
import com.satan.healthy.view.IRecipeView;

import static com.satan.healthy.R.mipmap.next;

/**
 * Created by Satan on 2016/08/23.
 */

public class RecipePresenterImpl implements IRecipePresenter {
    private IRecipeView view;
    private IRecipeModel model;

    public RecipePresenterImpl(IRecipeView view) {
        this.view = view;
        model = new RecipeModelImpl();
    }

    @Override
    public void getRecipeList(final int nextPage) {
        model.getRecipeList(nextPage, new CommonCallback<RecipeResponse>() {
            @Override
            public void onSuccess(RecipeResponse recipeResponse) {
                if (nextPage == 1) {
                    view.setTotalItem(recipeResponse.getTotal());
                }
                view.addToRecipes(recipeResponse.getTngou());
            }

            @Override
            public void onError(String error) {
                view.showError(error);
            }
        });
    }

    @Override
    public void searchRecipe(String input) {
        model.searcheRecipe(input, new CommonCallback<RecipeResponse>() {
            @Override
            public void onSuccess(RecipeResponse recipeResponse) {
                view.setTotalItem(recipeResponse.getTngou().size());
                view.addToRecipes(recipeResponse.getTngou());
            }

            @Override
            public void onError(String error) {
                view.showError(error);
            }
        });
    }
}
