package com.satan.healthy.adapter;

import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.satan.healthy.HealthyApplication;
import com.satan.healthy.R;
import com.satan.healthy.entitiy.CommonResponse;
import com.satan.healthy.entitiy.Hospitlal;
import com.satan.healthy.utils.ApiStoreGetRequest;
import com.satan.healthy.utils.ConstantFactory;
import com.satan.healthy.utils.GsonUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Satan on 2016/09/12.
 */

public class RecipeCollectionAdapter extends BaseItemDraggableAdapter<Integer> {
    public RecipeCollectionAdapter(int layoutResId, List<Integer> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, Integer integer) {
        ApiStoreGetRequest request = new ApiStoreGetRequest(ConstantFactory.getHospitalDetailUrl(integer), new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                CommonResponse commonResponse = (CommonResponse) GsonUtil.getEntity(s, CommonResponse.class);
                if (commonResponse.isStatus()) {
                    Hospitlal hospitlal = (Hospitlal) GsonUtil.getEntity(s, Hospitlal.class);
                    Picasso.with(mContext).load(ConstantFactory.getImageUrl(hospitlal.getImg())).into((ImageView) baseViewHolder.getView(R.id.iv_collection_fall_item));
                } else {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        HealthyApplication.getApp().getQueue().add(request);
    }

}
