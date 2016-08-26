package com.satan.healthy.model.modelImpl;

import android.graphics.Bitmap;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.satan.healthy.HealthyApplication;
import com.satan.healthy.entitiy.Hospital;
import com.satan.healthy.model.IHospitalDetailModel;
import com.satan.healthy.utils.ConstantFactory;
import com.satan.healthy.utils.GsonUtil;

/**
 * Created by Satan on 2016/08/18.
 */

public class HopitalDetailModelImpl implements IHospitalDetailModel {
    @Override
    public void getDetail(int hospitalId, final CommonCallback<Hospital> callback) {
        ApiStoreSDK.execute(ConstantFactory.getHospitalDetailUrl(hospitalId), ApiStoreSDK.GET, null, new ApiCallBack() {
            @Override
            public void onError(int i, String s, Exception e) {
                String error = e.getMessage();
                if (error != null && !"".equals(error)) {
                    callback.onError(error);
                }
            }

            @Override
            public void onSuccess(int i, String s) {
                Hospital hospital = (Hospital) GsonUtil.getEntity(s, Hospital.class);
                if (hospital != null) {
                    callback.onSuccess(hospital);
                }
            }
        });
    }

    @Override
    public void getImg(String img, final CommonCallback<Bitmap> callback) {
        //取消未完成的下载
        HealthyApplication.getApp().getQueue().cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
        //开始新的下载
        HealthyApplication.getApp().getImageLoader().get(ConstantFactory.getImageUrl(img), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                Bitmap bm = imageContainer.getBitmap();
                if (bm != null) {
                    callback.onSuccess(bm);
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callback.onError(volleyError.getMessage());
            }
        });


    }

}
