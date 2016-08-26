package com.satan.healthy.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.satan.healthy.utils.ConstantFactory.ACTION_LOCATION_FAIL;
import static com.satan.healthy.utils.ConstantFactory.ACTION_LOCATION_SUCCESS;

/**
 * Created by Satan on 2016/08/09.
 */

public class APSLocationService extends Service {
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {


        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
//                    aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
//                    aMapLocation.getLatitude();//获取纬度
//                    aMapLocation.getLongitude();//获取经度
//                    aMapLocation.getAccuracy();//获取精度信息
//                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    Date date = new Date(aMapLocation.getTime());
//                    df.format(date);//定位时间
//                    aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
//                    aMapLocation.getCountry();//国家信息
//                    aMapLocation.getProvince();//省信息
//                    aMapLocation.getCity();//城市信息
//                    Log.d("定位", aMapLocation.getCity());
//                    aMapLocation.getDistrict();//城区信息
//                    aMapLocation.getStreet();//街道信息
//                    aMapLocation.getStreetNum();//街道门牌号信息
//                    aMapLocation.getCityCode();//城市编码
//                    aMapLocation.getAdCode();//地区编码
//                    aMapLocation.getAoiName();//获取当前定位点的AOI信息
                    LatLonPoint latLonPoint = new LatLonPoint(aMapLocation.getLatitude(),aMapLocation.getLongitude());
                    Intent intent = new Intent(ACTION_LOCATION_SUCCESS);
                    intent.putExtra("city",aMapLocation.getCity());
                    intent.putExtra("district",aMapLocation.getDistrict());
                    intent.putExtra("street",aMapLocation.getStreet());
                    intent.putExtra("aoi",aMapLocation.getAoiName());
                    intent.putExtra("latLonPoint",latLonPoint);
                    sendBroadcast(intent);
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                    //发消息通知定位失败
                    Intent intent = new Intent(ACTION_LOCATION_FAIL);
                    sendBroadcast(intent);
                }
            }
        }
    };
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);

        if (mLocationOption.isOnceLocationLatest()) {
            mLocationOption.setOnceLocationLatest(true);
            //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
            //如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会。
        }

        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //启动定位
        mLocationClient.startLocation();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mLocationClient.stopLocation();
        mLocationClient.onDestroy();
        super.onDestroy();
    }
}
