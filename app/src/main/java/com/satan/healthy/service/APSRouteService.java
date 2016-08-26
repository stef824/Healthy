package com.satan.healthy.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;

import static com.satan.healthy.utils.ConstantFactory.DATA_CURRENT_LATLONPOINT;
import static com.satan.healthy.utils.ConstantFactory.NAVI_CURRENT_CITY;
import static com.satan.healthy.utils.ConstantFactory.NAVI_HOSPITAL_ADDRESS;

/**
 * Created by Satan on 2016/08/20.
 */

public class APSRouteService extends Service {
    public static final int ROUTE_TYPE_BUS = 0;
    public static final int ROUTE_TYPE_CAR = 1;
    public static final int ROUTE_TYPE_WALK = 2;

    private int mRouteType;

    private RouteSearch mRouteSearch;
    private String mCity;
    private LatLonPoint mFromPoint;
    private LatLonPoint mToPoint;
    private String mTo;

    private OnRouteResultListner mOnRouteResultListner;
    private RouteSearch.FromAndTo mFromAndTo;
    private GeocodeSearch mGeocodeSearch;
    private RouteSearch.BusRouteQuery mBusRouteQuery;
    private RouteSearch.DriveRouteQuery mDriveRouteQuery;
    private RouteSearch.WalkRouteQuery mWalkRouteQuery;

    public void setOnRouteResultListner(OnRouteResultListner onRouteResultListner) {
        mOnRouteResultListner = onRouteResultListner;
    }

    @Override
    public void onCreate() {
        mGeocodeSearch = new GeocodeSearch(this);
        mRouteSearch = new RouteSearch(this);
        mGeocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                //目的地坐标获取,设置路径规划起始点信息
                mToPoint = geocodeResult.getGeocodeAddressList().get(0).getLatLonPoint();
                mFromAndTo = new RouteSearch.FromAndTo(mFromPoint, mToPoint);
                queryRoute(mRouteType);
            }
        });
        mRouteSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
            @Override
            public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
                mOnRouteResultListner.onRouteResult(busRouteResult);
            }

            @Override
            public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
                mOnRouteResultListner.onRouteResult(driveRouteResult);
            }

            @Override
            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
                mOnRouteResultListner.onRouteResult(walkRouteResult);
            }
        });
    }

    public void getRoute(int routeType) {
        mRouteType = routeType;
        if (mToPoint == null) {
            getToLatLonPoint();
        } else {
            queryRoute(mRouteType);
        }

    }

    private void queryRoute(int routeType) {
        switch (routeType) {
            case ROUTE_TYPE_BUS:
                if (mBusRouteQuery == null) {
                    mBusRouteQuery = new RouteSearch.BusRouteQuery(mFromAndTo, 0, mCity, 0);
                }
                mRouteSearch.calculateBusRouteAsyn(mBusRouteQuery);
                break;
            case ROUTE_TYPE_CAR:
                if (mDriveRouteQuery == null) {
                    mDriveRouteQuery = new RouteSearch.DriveRouteQuery(mFromAndTo, 0, null, null, null);
                }
                mRouteSearch.calculateDriveRouteAsyn(mDriveRouteQuery);
                break;
            case ROUTE_TYPE_WALK:
                if (mWalkRouteQuery == null) {
                    mWalkRouteQuery = new RouteSearch.WalkRouteQuery(mFromAndTo, 0);
                }
                mRouteSearch.calculateWalkRouteAsyn(mWalkRouteQuery);
                break;
            default:
                break;
        }
    }

    private void getToLatLonPoint() {
        Log.d("mTo", mTo);
        Log.d("mCity", mCity);
        GeocodeQuery query = new GeocodeQuery(mTo, mCity);
        mGeocodeSearch.getFromLocationNameAsyn(query);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mFromPoint = intent.getParcelableExtra(DATA_CURRENT_LATLONPOINT);
        mTo = intent.getStringExtra(NAVI_HOSPITAL_ADDRESS);
        mCity = intent.getStringExtra(NAVI_CURRENT_CITY);
        return new RouteBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {

    }

    /**
     * 路径规划结果回调
     *
     * @param <T> 路径规划结果类型，共三种：BusRouteResult，DriveRouteResult，WalkRouteResult
     */
    public interface OnRouteResultListner<T extends RouteResult> {
        void onRouteResult(T routeResult);
    }

    public class RouteBinder extends Binder {
        public APSRouteService getService() {
            return APSRouteService.this;
        }
    }
}
