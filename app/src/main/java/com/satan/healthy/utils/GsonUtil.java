package com.satan.healthy.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Satan on 2016/08/03.
 */

public class GsonUtil {
    /**
     * 从json字符串返回实体对象
     * @param json json字符串
     * @param aClass 实体类
     * @return 实体类对象
     */
    public static Object getEntity(String json,Class aClass){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Object obj = gson.fromJson(json,aClass);
        return obj;
    }
}
