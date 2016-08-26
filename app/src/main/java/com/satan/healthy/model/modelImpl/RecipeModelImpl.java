package com.satan.healthy.model.modelImpl;

import android.util.Log;

import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.satan.healthy.entitiy.CommonResponse;
import com.satan.healthy.entitiy.RecipeResponse;
import com.satan.healthy.model.IRecipeModel;
import com.satan.healthy.utils.ConstantFactory;
import com.satan.healthy.utils.GsonUtil;

/**
 * Created by Satan on 2016/08/23.
 */
public class RecipeModelImpl implements IRecipeModel {

    @Override
    public void getRecipeList(int nextPage, CommonCallback<RecipeResponse> callback) {
        gerRecipies(ConstantFactory.getRecipesUrl(nextPage), callback);
    }


    @Override
    public void searcheRecipe(String input, final CommonCallback<RecipeResponse> callback) {
        gerRecipies(ConstantFactory.getRecipesUrlByName(input), callback);

    }

    /**
     * 根据url异步获取菜谱列表数据
     *
     * @param url      菜谱列表url
     * @param callback 回调
     */
    private void gerRecipies(String url, final CommonCallback<RecipeResponse> callback) {
        ApiStoreSDK.execute(url, ApiStoreSDK.GET, null, new ApiCallBack() {
            @Override
            public void onSuccess(int i, String s) {
                CommonResponse commonResponse = (CommonResponse) GsonUtil.getEntity(s, CommonResponse.class);
                if (commonResponse.isStatus()) {
                    RecipeResponse recipeResponse = (RecipeResponse) GsonUtil.getEntity(s, RecipeResponse.class);
                    if (recipeResponse.getTngou() != null && !recipeResponse.getTngou().isEmpty() && recipeResponse.getTngou().size() <= 20) {
                        callback.onSuccess(recipeResponse);
                    } else if (recipeResponse.getTngou().isEmpty()) {
                        callback.onError("暂未收录此菜谱，敬请期待");
                    } else if (recipeResponse.getTngou().size() > 20) {
                        for (int j = 0; j < recipeResponse.getTngou().size(); j++) {
                            if (j > 19) {
                                recipeResponse.getTngou().remove(j);
                            }
                        }
                        callback.onSuccess(recipeResponse);
                    }
                } else {
                    callback.onError("服务器错误，请稍后再试");
                }
            }

            @Override
            public void onError(int i, String s, Exception e) {
                String error = e.getMessage();
                if (error != null && !"".equals(error)) {
                    callback.onError(e.getMessage());
                }else {
                    callback.onError("服务器错误，请稍后再试");
                }
            }
        });
    }
}
