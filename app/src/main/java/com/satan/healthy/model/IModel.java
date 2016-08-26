package com.satan.healthy.model;

/**
 * Created by Satan on 2016/08/16.
 */

public interface IModel {
    interface CommonCallback<T>{
        void onSuccess(T t);
        void onError(String error);
    }
}
