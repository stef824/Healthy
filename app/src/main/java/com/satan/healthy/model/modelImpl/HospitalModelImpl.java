package com.satan.healthy.model.modelImpl;

import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.satan.healthy.entitiy.Hospital;
import com.satan.healthy.entitiy.HospitalListResponse;
import com.satan.healthy.model.IHospitalModel;
import com.satan.healthy.utils.ConstantFactory;
import com.satan.healthy.utils.GsonUtil;

import java.util.List;

/**
 * Created by Satan on 2016/08/16.
 */

public class HospitalModelImpl implements IHospitalModel {

    @Override
    public void getHospitalsNearby(double x, double y, int nextPage, CommonCallback<HospitalListResponse> callback) {
        getHospitals(ConstantFactory.getHospitalNearbyUrl(x, y, nextPage), callback);
    }

    @Override
    public void getHospitalsOfCity(int cityId, int nextPage, CommonCallback<HospitalListResponse> callback) {
        getHospitals(ConstantFactory.getHospitalOfCityUrl(cityId, nextPage), callback);
    }

    /**
     * 获取医院列表数据
     *
     * @param url      医院列表url
     * @param callback 回调
     */
    private void getHospitals(String url, final CommonCallback<HospitalListResponse> callback) {
        ApiStoreSDK.execute(url, ApiStoreSDK.GET, null, new ApiCallBack() {
            @Override
            public void onSuccess(int i, String s) {
                HospitalListResponse response = (HospitalListResponse) GsonUtil.getEntity(s, HospitalListResponse.class);
                if (response.isStatus()) {
                    if (response.getTngou() != null && !response.getTngou().isEmpty()) {
                        setLevel(response.getTngou());
                        callback.onSuccess(response);
                    }
                } else {
                    callback.onError("服务器错误，请稍后重试");
                }

            }

            @Override
            public void onError(int i, String s, Exception e) {
                String error = e.getMessage();
                if (error != null && !"".equals(error)) {
                    callback.onError(e.getMessage());
                }
            }
        });
    }

    private void setLevel(List<Hospital> hospitals) {
        for (Hospital hospital: hospitals) {
            if (hospital.getLevel()==null||"其他".equals(hospital.getLevel())){
                hospital.setLevel("");
            }
        }
    }
}
