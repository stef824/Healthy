package com.satan.healthy.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.satan.healthy.HealthyApplication;
import com.satan.healthy.R;
import com.satan.healthy.entitiy.Hospital;
import com.satan.healthy.presenter.IHopitalDetailPresenter;
import com.satan.healthy.presenter.presenterImpl.HopitalDetailPresenterImpl;
import com.satan.healthy.utils.ConstantFactory;
import com.satan.healthy.utils.TelNumUtil;
import com.satan.healthy.utils.ToastUtil;
import com.satan.healthy.view.IHospitalDetailView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import static com.satan.healthy.utils.ConstantFactory.DATA_CURRENT_CITY;
import static com.satan.healthy.utils.ConstantFactory.DATA_CURRENT_LATLONPOINT;
import static com.satan.healthy.utils.ConstantFactory.DATA_HOSPITAL_ID;
import static com.satan.healthy.utils.ConstantFactory.DATA_HOSPITAL_NAME;
import static com.satan.healthy.utils.ConstantFactory.DATA_IS_AUTO_LOCATION;
import static com.satan.healthy.utils.ConstantFactory.NAVI_HOSPITAL_ADDRESS;

@ContentView(R.layout.activity_hospital_detail)
public class HospitalDetailActivity extends AppCompatActivity implements IHospitalDetailView {
    //注解控件
    @ViewInject(R.id.toolbar_hospital_detail)
    private Toolbar mToolbar;
    @ViewInject(R.id.fab_mark)
    private FloatingActionButton mFab;
    @ViewInject(R.id.toolbar_layout_hospital_detail)
    private CollapsingToolbarLayout mToolbarLayout;
    @ViewInject(R.id.tv_hospital_detail_msg)
    private TextView mTVMsg;
    @ViewInject(R.id.tv_hospital_detail_tel)
    private TextView mTVTel;
    @ViewInject(R.id.tv_hospital_detail_address)
    private TextView mTVAddress;
    @ViewInject(R.id.ll_call)
    private LinearLayout mLLCall;
    @ViewInject(R.id.ll_start_navi)
    private LinearLayout mLLNavi;


    private IHopitalDetailPresenter mPresenter;
    private ToastUtil mToastUtil;

    private Hospital mHospital;

    private int hopitalId;
    private String hospitalName;
    private String[] mTels;

    private AlertDialog mAlertDialog;
    private AlertDialog.Builder mBuilder;
    /**
     * 定位位置的坐标点
     */
    private LatLonPoint mLatLonPoint;
    /**
     * 医院所在城市，也是定位城市
     */
    private String mCity;
    /**
     * 医院地址
     */
    private String mAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new HopitalDetailPresenterImpl(this);
        mToastUtil = new ToastUtil(this);
        //设置状态栏透明
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        //注解布局
        x.view().inject(this);
        //设置控件不可点
        setUnclickable();
        //设置Toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2016/08/19 收藏和取消收藏操作
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });
        Intent intent = getIntent();
        hopitalId = intent.getIntExtra(DATA_HOSPITAL_ID, 0);
        hospitalName = intent.getStringExtra(DATA_HOSPITAL_NAME);
        //根据医院列表页面的定位模式来确定是否提供路径规划功能
        if (intent.getBooleanExtra(DATA_IS_AUTO_LOCATION,false)) {
            mLLNavi.setVisibility(View.VISIBLE);
            //获取路径规划需要的数据
            mLatLonPoint = intent.getParcelableExtra(DATA_CURRENT_LATLONPOINT);
            mCity = intent.getStringExtra(DATA_CURRENT_CITY);
            mAddress = intent.getStringExtra(NAVI_HOSPITAL_ADDRESS);
        }else {
            mLLNavi.setVisibility(View.GONE);

        }
        mPresenter.getDetail(hopitalId);
        mToolbarLayout.setTitle(hospitalName);
        mBuilder = new AlertDialog.Builder(this);
    }

    /**
     * 设置控件不可点击
     */
    private void setUnclickable() {
        mFab.setClickable(false);
        mLLCall.setClickable(false);
        mLLNavi.setClickable(false);
    }

    @Override
    public void setDetail(Hospital hospital) {
        mHospital = hospital;
        mPresenter.getImg(hospital.getImg());
        Spanned msg = Html.fromHtml(hospital.getMessage().trim());
        mTVMsg.setText(msg);
        mTVTel.append(hospital.getTel());
        mTVAddress.append(hospital.getAddress());
        mTels = TelNumUtil.getTelNumsFromCharsequence(hospital.getTel());
        //设置控件可点
        setClickable();
    }

    /**
     * 设置控件可点击
     */
    private void setClickable() {
        mFab.setClickable(true);
        mLLCall.setClickable(true);
        mLLNavi.setClickable(true);
    }

    @Override
    public void setImg(Bitmap img) {
        mToolbarLayout.setBackground(new BitmapDrawable(img));
    }

    @Override
    public void showError(String error) {
        mToastUtil.showToast(error);
    }

    @Event(value = R.id.ll_call)
    private void onTelClick(View v) {
        if (mTels == null || mTels.length == 0) {
            return;
        }
        mAlertDialog = mBuilder.setTitle("拨打电话").setItems(mTels, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel://" + mTels[i]));
                if (ActivityCompat.checkSelfPermission(HospitalDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
            }
        }).create();
        mAlertDialog.show();
    }

    @Event(R.id.ll_start_navi)
    private void onNaviClick(View v) {
        Intent intent = new Intent(this, NaviActivity.class);
        intent.putExtra(ConstantFactory.NAVI_CURRENT_CITY, mCity);
        intent.putExtra(ConstantFactory.NAVI_HOSPITAL_ADDRESS, mAddress);
        intent.putExtra(DATA_CURRENT_LATLONPOINT,mLatLonPoint);
        intent.putExtra(DATA_HOSPITAL_NAME,mHospital.getName());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //监视内存泄漏
        HealthyApplication.getApp().getRefWatcher().watch(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
