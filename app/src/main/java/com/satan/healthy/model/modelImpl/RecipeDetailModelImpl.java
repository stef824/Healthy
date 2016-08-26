package com.satan.healthy.model.modelImpl;

import android.graphics.Bitmap;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.satan.healthy.HealthyApplication;
import com.satan.healthy.entitiy.CommonResponse;
import com.satan.healthy.entitiy.Recipe;
import com.satan.healthy.entitiy.RecipeResponse;
import com.satan.healthy.model.IRecipeDetailModel;
import com.satan.healthy.utils.ConstantFactory;
import com.satan.healthy.utils.GsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Satan on 2016/08/23.
 */

public class RecipeDetailModelImpl implements IRecipeDetailModel {
    @Override
    public void getImg(String urlSuffix, final CommonCallback<Bitmap> callback) {
        HealthyApplication.getApp().getImageLoader().get(ConstantFactory.getImageUrl(urlSuffix), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                if (imageContainer.getBitmap() != null) {
                    callback.onSuccess(imageContainer.getBitmap());
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String error = volleyError.getMessage();
                if (error != null && !"".equals(error)) {
                    callback.onError(error);
                }
            }
        });
    }

    @Override
    public void getRelatedRecipes(final int recipeId, String recipeName, final CommonCallback<List<Recipe>> callback) {
        ApiStoreSDK.execute(ConstantFactory.getRecipesUrlByName(recipeName), ApiStoreSDK.GET, null, new ApiCallBack() {
            @Override
            public void onSuccess(int i, String s) {
                CommonResponse commonResponse = (CommonResponse) GsonUtil.getEntity(s, CommonResponse.class);
                if (commonResponse.isStatus()) {
                    RecipeResponse recipeResponse = (RecipeResponse) GsonUtil.getEntity(s, RecipeResponse.class);
                    List<Recipe> recipes = null;
                    if (recipeResponse.getTngou() != null && !recipeResponse.getTngou().isEmpty()) {
                        for (int j = 0; j < recipeResponse.getTngou().size(); j++) {
                            if (recipeResponse.getTngou().get(j).getId() == recipeId) {
                                recipeResponse.getTngou().remove(j);
                            }
                        }
                        if (recipeResponse.getTngou().size()>3){
                            recipes = new ArrayList<>();
                            recipes.add(recipeResponse.getTngou().get(0));
                            recipes.add(recipeResponse.getTngou().get(1));
                            recipes.add(recipeResponse.getTngou().get(2));
                        }else {
                            recipes = recipeResponse.getTngou();
                        }
                        callback.onSuccess(recipes);
                    }
                } else {
                    callback.onError("服务器错误，请稍后重试");
                }
            }

            @Override
            public void onError(int i, String s, Exception e) {
                String erorr = e.getMessage();
                if (erorr != null && !"".equals(erorr)) {
                    callback.onError(erorr);
                }
            }
        });
    }

    @Override
    public void getRecipeMethod(int id, final CommonCallback<Recipe> callback) {
        ApiStoreSDK.execute(ConstantFactory.getRecipeUrl(id), ApiStoreSDK.GET, null, new ApiCallBack() {
            @Override
            public void onSuccess(int i, String s) {
                CommonResponse commonResponse = (CommonResponse) GsonUtil.getEntity(s, CommonResponse.class);
                if (commonResponse.isStatus()) {
                    Recipe recipe = (Recipe) GsonUtil.getEntity(s, Recipe.class);
                    callback.onSuccess(recipe);
                } else {
                    callback.onError("服务器错误，请稍后重试");
                }
            }

            @Override
            public void onError(int i, String s, Exception e) {
                String erorr = e.getMessage();
                if (erorr != null && !"".equals(erorr)) {
                    callback.onError(erorr);
                }
            }
        });
    }
}
