package com.satan.healthy.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.satan.healthy.R;
import com.satan.healthy.entitiy.Recipe;
import com.satan.healthy.utils.ConstantFactory;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Satan on 2016/08/23.
 */
public class RelatedRecipeAdapter extends BaseQuickAdapter<Recipe> {
    public RelatedRecipeAdapter(int layoutResId, List<Recipe> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Recipe recipe) {
        baseViewHolder.setText(R.id.tv_related_recipe_item, recipe.getName());
        Picasso.with(mContext).load(ConstantFactory.getImageUrl(recipe.getImg())).into((ImageView) baseViewHolder.getView(R.id.iv_related_recipe_item));
    }
}
