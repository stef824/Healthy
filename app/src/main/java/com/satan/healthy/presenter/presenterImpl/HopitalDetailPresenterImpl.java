package com.satan.healthy.presenter.presenterImpl;

import android.graphics.Bitmap;

import com.satan.healthy.entitiy.Hospital;
import com.satan.healthy.model.IHospitalDetailModel;
import com.satan.healthy.model.IModel;
import com.satan.healthy.model.modelImpl.HopitalDetailModelImpl;
import com.satan.healthy.presenter.IHopitalDetailPresenter;
import com.satan.healthy.view.IHospitalDetailView;

/**
 * Created by Satan on 2016/08/18.
 */

public class HopitalDetailPresenterImpl implements IHopitalDetailPresenter {
    private IHospitalDetailView view;
    private IHospitalDetailModel model;

    public HopitalDetailPresenterImpl(IHospitalDetailView view) {
        this.view = view;
        model = new HopitalDetailModelImpl();
    }

    @Override
    public void getDetail(int hospitalId) {
        model.getDetail(hospitalId, new IModel.CommonCallback<Hospital>() {
            @Override
            public void onSuccess(Hospital hospital) {
                view.setDetail(hospital);
            }

            @Override
            public void onError(String error) {
                view.showError(error);
            }
        });
    }

    @Override
    public void getImg(String img) {
        model.getImg(img, new IModel.CommonCallback<Bitmap>() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                view.setImg(bitmap);
            }

            @Override
            public void onError(String error) {
                view.showError(error);
            }
        });
    }
}
