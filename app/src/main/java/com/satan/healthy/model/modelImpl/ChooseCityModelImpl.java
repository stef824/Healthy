package com.satan.healthy.model.modelImpl;


import android.os.AsyncTask;

import com.satan.healthy.HealthyApplication;
import com.satan.healthy.entitiy.City;
import com.satan.healthy.entitiy.Province;
import com.satan.healthy.model.IChooseCityModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Satan on 2016/08/17.
 */

public class ChooseCityModelImpl implements IChooseCityModel {
    private AsyncTask<Void, Void, List<City>> mTask;
    private boolean isTaskFinished;

    @Override
    public void getProvinceAndCities(CommonCallback<List<Province>> callback) {
        HealthyApplication.getApp().getProvinces(callback);
    }

    @Override
    public void getMatchedCities(List<Province> provinces, String input, CommonCallback<List<City>> callback) {
        if (mTask == null) {
            executeTask(provinces, input, callback);
        } else if (isTaskFinished) {
            executeTask(provinces, input, callback);
        } else {
            mTask.cancel(true);
            executeTask(provinces, input, callback);
        }
    }

    private void executeTask(final List<Province> provinces, final String input, final CommonCallback<List<City>> callback) {
        mTask = new AsyncTask<Void, Void, List<City>>() {

            @Override
            protected List<City> doInBackground(Void... voids) {
                List<City> matchedCities = new ArrayList<>();
                if ("".equals(input)) {
                    return matchedCities;
                }
                for (Province p : provinces) {
                    for (City c : p.getCitys()) {
                        if (c.getCity().contains(input)) {
                            matchedCities.add(c);
                        }
                    }
                }
                return matchedCities;
            }

            @Override
            protected void onPostExecute(List<City> cities) {
                callback.onSuccess(cities);
                isTaskFinished = true;
            }
        };
        isTaskFinished = false;
        mTask.execute();
    }
}
