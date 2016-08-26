package com.satan.healthy.presenter.presenterImpl;

import com.satan.healthy.entitiy.HospitalListResponse;
import com.satan.healthy.model.IHospitalModel;
import com.satan.healthy.model.IModel;
import com.satan.healthy.model.modelImpl.HospitalModelImpl;
import com.satan.healthy.presenter.IHospitalPresenter;
import com.satan.healthy.view.IHospitalView;

import java.math.BigDecimal;

/**
 * Created by Satan on 2016/08/16.
 */

public class HospitalPresenterImpl implements IHospitalPresenter {
    private IHospitalView view;
    private IHospitalModel model;

    public HospitalPresenterImpl(IHospitalView view) {
        this.view = view;
        model = new HospitalModelImpl();
    }

    @Override
    public void getHospitalsNearby(double x, double y, final int nextPage) {
        model.getHospitalsNearby(x, y, nextPage, new IModel.CommonCallback<HospitalListResponse>() {
            @Override
            public void onSuccess(HospitalListResponse hospitalListResponse) {
                //加载第一页数据时设置总页数
                if(nextPage==1){
                    view.setTotalItem(40);//附近医院按覆盖半径无限扩大，总页数上千，默认设置40条数据
                }
                //显示列表
                view.addToHospitals(hospitalListResponse.getTngou());
            }

            @Override
            public void onError(String error) {
                view.showError(error);
            }
        });
    }

    @Override
    public void getHospitalsOfCity(int cityId, final int nextPage) {
        model.getHospitalsOfCity(cityId, nextPage, new IModel.CommonCallback<HospitalListResponse>() {
            @Override
            public void onSuccess(HospitalListResponse hospitalListResponse) {
                //加载第一页时，设置总页数
                if (nextPage==1){
                    view.setTotalItem(hospitalListResponse.getTotal());
                }
                //显示列表
                view.addToHospitals(hospitalListResponse.getTngou());
            }

            @Override
            public void onError(String error) {
                view.showError(error);
            }
        });
    }
}
