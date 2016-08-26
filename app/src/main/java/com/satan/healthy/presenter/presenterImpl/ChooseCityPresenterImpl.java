package com.satan.healthy.presenter.presenterImpl;

import com.satan.healthy.entitiy.City;
import com.satan.healthy.entitiy.Province;
import com.satan.healthy.model.IChooseCityModel;
import com.satan.healthy.model.IModel;
import com.satan.healthy.model.modelImpl.ChooseCityModelImpl;
import com.satan.healthy.presenter.IChooseCityPresenter;
import com.satan.healthy.view.IChooseCityView;

import java.util.List;

/**
 * Created by Satan on 2016/08/17.
 */

public class ChooseCityPresenterImpl implements IChooseCityPresenter {
    private IChooseCityView view;
    private IChooseCityModel model;

    public ChooseCityPresenterImpl(IChooseCityView view) {
        this.view = view;
        model = new ChooseCityModelImpl();
    }

    @Override
    public void getProvincesAndCities() {
        model.getProvinceAndCities(new IModel.CommonCallback<List<Province>>() {
            @Override
            public void onSuccess(List<Province> provinces) {
                view.setProvinces(provinces);
            }

            @Override
            public void onError(String error) {
                if (error!=null&&!"".equals(error)){
                    view.showError(error);
                }
            }
        });
    }

    @Override
    public void getMatchedCities(List<Province> provinces, String input) {
        if (provinces==null){
            return;
        }
        model.getMatchedCities(provinces, input, new IModel.CommonCallback<List<City>>() {
            @Override
            public void onSuccess(List<City> cities) {
                view.setMatchedCities(cities);
            }

            @Override
            public void onError(String error) {
                if (error!=null&&!"".equals(error)){
                    view.showError(error);
                }
            }
        });
    }
}
