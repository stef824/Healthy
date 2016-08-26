package com.satan.healthy.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.overlay.BusRouteOverlay;
import com.amap.api.maps2d.overlay.DrivingRouteOverlay;
import com.amap.api.maps2d.overlay.WalkRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteResult;
import com.amap.api.services.route.WalkRouteResult;
import com.satan.healthy.R;
import com.satan.healthy.service.APSRouteService;
import com.satan.healthy.utils.ToastUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.satan.healthy.utils.ConstantFactory.DATA_CURRENT_LATLONPOINT;
import static com.satan.healthy.utils.ConstantFactory.DATA_HOSPITAL_NAME;
import static com.satan.healthy.utils.ConstantFactory.NAVI_CURRENT_CITY;
import static com.satan.healthy.utils.ConstantFactory.NAVI_HOSPITAL_ADDRESS;

@ContentView(R.layout.activity_navi)
public class NaviActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    //注解控件
    @ViewInject(R.id.toolbar_navi)
    private Toolbar mToolbar;
    @ViewInject(R.id.map)
    private MapView mMapView;
    @ViewInject(R.id.rg_navi)
    private RadioGroup mRadioGroup;
    @ViewInject(R.id.navi_bus_line)
    private CardView mCVBusLine;
    @ViewInject(R.id.tv_bus_path_index)
    private TextView mTVBusPath;
    @ViewInject(R.id.tv_total_distance)
    private TextView mTVTotalDistance;
    @ViewInject(R.id.tv_walk_distance)
    private TextView mTVWalkDistance;
    @ViewInject(R.id.tv_price)
    private TextView mTVPrice;

    private ToastUtil mToastUtil;

    private AMap mAMap;
    private UiSettings mUiSettings;

    private LatLonPoint mFromPoint;
    private String mTo;
    private String mCity;

    private ServiceConnection mConn;
    private APSRouteService mRouteService;
    private DrivingRouteOverlay mDrivingRouteOverlay;
    private WalkRouteOverlay mWalkRouteOverlay;

    private List<BusPath> mBusPaths;
    private List<BusRouteOverlay> mBusRouteOverlays;

    private int mBusPathIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToastUtil = new ToastUtil(this);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mMapView.onCreate(savedInstanceState);
        mAMap = mMapView.getMap();
        mUiSettings = mAMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(false);
        //获取医院详情界面传入的数据
        Intent intent = getIntent();
        mFromPoint = intent.getParcelableExtra(DATA_CURRENT_LATLONPOINT);
        mTo = intent.getStringExtra(NAVI_HOSPITAL_ADDRESS);
        mCity = intent.getStringExtra(NAVI_CURRENT_CITY);
        mToolbar.setTitle("目的地:"+intent.getStringExtra(DATA_HOSPITAL_NAME));
        //绑定路线规划服务
        Intent service = new Intent(this, APSRouteService.class);
        service.putExtra(DATA_CURRENT_LATLONPOINT, mFromPoint);
        service.putExtra(NAVI_HOSPITAL_ADDRESS, mTo);
        service.putExtra(NAVI_CURRENT_CITY, mCity);
        mConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                //获得路径规划服务对象
                mRouteService = ((APSRouteService.RouteBinder) service).getService();
                Log.d("APSRouteService", ((APSRouteService.RouteBinder) service).getService().toString());
                //默认为公交查询
                ((RadioButton) mRadioGroup.getChildAt(0)).setChecked(true);
                //为路径规划服务设置路径规划成功的监听器
                mRouteService.setOnRouteResultListner(new APSRouteService.OnRouteResultListner() {
                    @Override
                    public void onRouteResult(RouteResult routeResult) {
                        if (routeResult instanceof BusRouteResult) {
                            BusRouteResult busRouteResult = (BusRouteResult) routeResult;
                            mBusPaths = busRouteResult.getPaths();
                            mBusRouteOverlays = new ArrayList<>();
                            for (BusPath bp : mBusPaths){
                                mBusRouteOverlays.add(new BusRouteOverlay(NaviActivity.this, mAMap, bp, busRouteResult.getStartPos(), busRouteResult.getTargetPos()));
                            }
                            addBusRouteToMap(mBusPathIndex);
                            showBusInfo(mBusPathIndex);
                        } else if (routeResult instanceof DriveRouteResult) {
                            DriveRouteResult driveRouteResult = (DriveRouteResult) routeResult;
                            mDrivingRouteOverlay = new DrivingRouteOverlay(NaviActivity.this, mAMap, driveRouteResult.getPaths().get(0), driveRouteResult.getStartPos(), driveRouteResult.getTargetPos());
                            addCarRouteToMap();
                        } else if (routeResult instanceof WalkRouteResult) {
                            WalkRouteResult walkRouteResult = (WalkRouteResult) routeResult;
                            mWalkRouteOverlay = new WalkRouteOverlay(NaviActivity.this, mAMap, walkRouteResult.getPaths().get(0), walkRouteResult.getStartPos(), walkRouteResult.getTargetPos());
                            addWalkRouteToMap();
                        }
                    }
                });
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(service, mConn, BIND_AUTO_CREATE);
        mRadioGroup.setOnCheckedChangeListener(this);

    }

    private void addWalkRouteToMap() {
        mWalkRouteOverlay.addToMap();
        mWalkRouteOverlay.zoomToSpan();
    }

    private void addCarRouteToMap() {
        mDrivingRouteOverlay.addToMap();
        mDrivingRouteOverlay.zoomToSpan();
    }

    private void addBusRouteToMap(int busPathIndex) {
        for (int i=0;i<mBusRouteOverlays.size();i++){
            if (i==busPathIndex){
                mBusRouteOverlays.get(i).addToMap();
                mBusRouteOverlays.get(i).zoomToSpan();
            }else {
                mBusRouteOverlays.get(i).removeFromMap();
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        //解绑服务
        unbindService(mConn);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.rb_bus:
                if (mDrivingRouteOverlay != null) {
                    mDrivingRouteOverlay.removeFromMap();
                }
                if (mWalkRouteOverlay != null) {
                    mWalkRouteOverlay.removeFromMap();
                }
                if (mBusRouteOverlays != null) {
                    addBusRouteToMap(mBusPathIndex);
                } else {
                    mRouteService.getRoute(APSRouteService.ROUTE_TYPE_BUS);
                }
                if (mBusPaths != null) {
                    showBusInfo(mBusPathIndex);
                }
                break;
            case R.id.rb_car:
                if (mBusRouteOverlays != null) {
                    removeAllBusRouteOverlays();
                }
                if (mWalkRouteOverlay != null) {
                    mWalkRouteOverlay.removeFromMap();
                }
                if (mDrivingRouteOverlay != null) {
                    addCarRouteToMap();
                } else {
                    mRouteService.getRoute(APSRouteService.ROUTE_TYPE_CAR);
                }
                hideBusInfo();
                break;
            case R.id.rb_walk:
                if (mBusRouteOverlays != null) {
                    removeAllBusRouteOverlays();
                }
                if (mDrivingRouteOverlay != null) {
                    mDrivingRouteOverlay.removeFromMap();
                }
                if (mWalkRouteOverlay != null) {
                    addWalkRouteToMap();
                } else {
                    mRouteService.getRoute(APSRouteService.ROUTE_TYPE_WALK);
                }
                hideBusInfo();
        }
    }

    private void removeAllBusRouteOverlays() {
        for (BusRouteOverlay busRouteOverlay : mBusRouteOverlays){
            busRouteOverlay.removeFromMap();
        }
    }


    private void hideBusInfo() {
        mCVBusLine.setVisibility(View.GONE);
    }

    private void showBusInfo(int busPathIndex) {
        if (mBusPaths.isEmpty()){
            mToastUtil.showToast("距离太近，请选择其它出行方式");
            return;
        }
        mCVBusLine.setVisibility(View.VISIBLE);
        mTVBusPath.setText("公交方案" + (mBusPathIndex + 1));
        String totalDistance = mBusPaths.get(busPathIndex).getDistance() / 1000 + "";
        mTVTotalDistance.setText("总距离：" + totalDistance + "公里");
        mTVWalkDistance.setText("步行距离：" + mBusPaths.get(busPathIndex).getWalkDistance() + "米");
        mTVPrice.setText("票价：" + mBusPaths.get(busPathIndex).getCost());
    }

    @Event(R.id.iv_previous_bus_path)
    private void onPreviousClick(View v) {
        if (mBusPathIndex == 0) {
            mBusPathIndex = mBusPaths.size() - 1;
        } else {
            mBusPathIndex--;
        }
        showBusInfo(mBusPathIndex);
        addBusRouteToMap(mBusPathIndex);
    }

    @Event(R.id.iv_next_bus_path)
    private void onNextClick(View v) {
        if (mBusPathIndex == mBusPaths.size() - 1) {
            mBusPathIndex = 0;
        } else {
            mBusPathIndex++;
        }
        showBusInfo(mBusPathIndex);
        addBusRouteToMap(mBusPathIndex);
    }

}
