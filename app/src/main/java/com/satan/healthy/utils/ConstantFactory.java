package com.satan.healthy.utils;

import static android.R.id.input;

/**
 * Created by Satan on 2016/08/03.
 */

public class ConstantFactory {
    /**
     * 医院列表页面启动城市选择界面时发送的请求码
     */
    public static final int REQUEST_CODE_CHOOSE_CITY = 0;
    /**
     * 城市选择界面设置的响应码
     */
    public static final int RESULT_CODE_CHOOSE_CITY = 1;
    /**
     * 城市选择界面设置城市id数据的键名
     */
    public static final String DATA_CITY_ID = "DATA_CITY_ID";
    /**
     * 城市选择界面设置城市名称数据的键名
     */
    public static final String DATA_CITY_NAME = "DATA_CITY_NAME";
    /**
     * 启动医院详情界面传递医院id的键名
     */
    public static final String DATA_HOSPITAL_ID = "DATA_HOSPITAL_ID";
    /**
     * 启动医院详情界面传递医院名称的键名
     */
    public static final String DATA_HOSPITAL_NAME = "DATA_HOSPITAL_NAME";
    /**
     * 启动医院详情界面传递的定位模式的键名
     */
    public static final String DATA_IS_AUTO_LOCATION = "DATA_IS_AUTO_LOCATION";
    /**
     * 启动医院详情界面传递的定位经纬度点的键名
     */
    public static final String DATA_CURRENT_LATLONPOINT = "DATA_CURRENT_LATLONPOINT";
    /**
     * 启动医院详情界面传递的当前城市名称的键名
     */
    public static final String DATA_CURRENT_CITY = "DATA_CURRENT_CITY";

    /**
     * 获取省市数据的url
     */
    public static final String URL_PROVINCES_AND_CITIES = "http://apis.baidu.com/tngou/hospital/city?type=all";

    /**
     * 获取附近医院的url
     *
     * @param x    经度
     * @param y    纬度
     * @param page 页码
     * @return 附近医院列表url
     */
    public static final String getHospitalNearbyUrl(double x, double y, int page) {
        return "http://apis.baidu.com/tngou/hospital/location?x=" + x + "&y=" + y + "&page=" + page + "&rows=10";
    }

    /**
     * 获取城市医院的url
     *
     * @param cityId 城市id
     * @param page   纬度
     * @return 城市医院列表url
     */
    public static final String getHospitalOfCityUrl(int cityId, int page) {
        return " http://apis.baidu.com/tngou/hospital/list?id=" + cityId + "&page=" + page + "&rows=10";
    }

    /**
     * 下载图片的url
     *
     * @param img 图片url后缀
     * @return
     */
    public static String getImageUrl(String img) {
        return "http://tnfs.tngou.net/image" + img;
    }

    public static String getHospitalDetailUrl(int hospitalId) {
        return "http://apis.baidu.com/tngou/hospital/show?id=" + hospitalId;
    }

    /**
     * 导航医院所在城市
     */
    public static final String NAVI_CURRENT_CITY = "NAVI_CURRENT_CITY";
    /**
     * 导航医院地址
     */
    public static final String NAVI_HOSPITAL_ADDRESS = "NAVI_HOSPITAL_ADDRESS";

    /**
     * 定位成功发广播的ACTION
     */
    public static final String ACTION_LOCATION_SUCCESS = "ACTION_LOCATION_SUCCESS";
    /**
     * 定位失败发广播的ACTION
     */
    public static final String ACTION_LOCATION_FAIL = "ACTION_LOCATION_FAIL";

    /**
     * 获取菜谱列表数据url
     * @param nextPage 页码
     * @return url
     */
    public static String getRecipesUrl(int nextPage) {
        return "http://apis.baidu.com/tngou/cook/list?id=15&page="+nextPage+"&rows=20";
    }

    /**
     * 获取菜谱详情的url
     * @param recipeId 菜谱id
     */
    public static String getRecipeUrl(int recipeId) {
        return "http://apis.baidu.com/tngou/cook/show?id="+recipeId;
    }

    /**
     * 根据菜谱名称搜索菜谱的url
     * @param input 用户输入关键字
     * @return url
     */
    public static String getRecipesUrlByName(String input) {
        return "http://apis.baidu.com/tngou/cook/name?name="+input;
    }

    public static String DATA_RECIPE = "DATA_RECIPE";
    public static String DATA_NEWS = "DATA_NEWS";

    /**
     * 获取资讯列表的url
     * @param page 页码
     * @return url
     */
    public static String getNewsesUrl(int page) {
        return "http://apis.baidu.com/tngou/info/list?id=0&page="+page+"&rows=18";
    }

    /**
     * 获取资讯详情的url
     * @param newsId 资讯id
     * @return url
     */
    public static String getNewsUrl(int newsId) {
        return " http://apis.baidu.com/tngou/info/show?id="+newsId;
    }
}
