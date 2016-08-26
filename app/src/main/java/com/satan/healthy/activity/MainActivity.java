package com.satan.healthy.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.satan.healthy.R;
import com.satan.healthy.activity.interfaces.IMainActivity;
import com.satan.healthy.entitiy.Location;
import com.satan.healthy.fragment.HospitalFragment;
import com.satan.healthy.fragment.NewsFragment;
import com.satan.healthy.fragment.RecipeFragment;
import com.satan.healthy.fragment.interfaces.IFragment;
import com.satan.healthy.fragment.interfaces.IHospitalFragment;
import com.satan.healthy.service.APSLocationService;
import com.satan.healthy.ui.RoundImageView;
import com.satan.healthy.utils.ToastUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.satan.healthy.utils.ConstantFactory.ACTION_LOCATION_FAIL;
import static com.satan.healthy.utils.ConstantFactory.ACTION_LOCATION_SUCCESS;
import static com.satan.healthy.utils.ConstantFactory.DATA_CITY_ID;
import static com.satan.healthy.utils.ConstantFactory.DATA_CITY_NAME;
import static com.satan.healthy.utils.ConstantFactory.REQUEST_CODE_CHOOSE_CITY;
import static com.satan.healthy.utils.ConstantFactory.RESULT_CODE_CHOOSE_CITY;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IMainActivity {
    //注解控件
    @ViewInject(R.id.toolbar_hospital_detail)
    private Toolbar mToolbar;
    @ViewInject(R.id.drawer_layout)
    private DrawerLayout mDrawer;
    @ViewInject(R.id.nav_view)
    private NavigationView mNavigationView;
    @ViewInject(R.id.vp_main_pager)
    private ViewPager mPager;
    @ViewInject(R.id.tl_tabs)
    private TabLayout mTabs;
    @ViewInject(R.id.tv_location)
    private TextView mTvLocation;
    @ViewInject(R.id.fab_to_top)
    private FloatingActionButton mFabToTop;

    private RoundImageView mRoundImageView;

    //Toast工具
    private ToastUtil mToastUtil;

    //Fragment对象数组
    private List<Fragment> mFragments;
    //Pager适配器
    private FragmentPagerAdapter mFragmentPagerAdapter;
    //选项卡的标题数组
    private List<String> mPagerTitles;

    //启动定位服务的Intent
    private Intent mIntent;

    //广播接收器
    private BroadcastReceiver mReceiver;

    //IHospitalFragment接口
    private IHospitalFragment mIHospitalFragment;
    private int mLocationTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注解布局
        x.view().inject(this);
        //初始化Toast工具
        mToastUtil = new ToastUtil(this);
        //点击头像登录
        View view = mNavigationView.getHeaderView(0);
        mRoundImageView = (RoundImageView) view.findViewById(R.id.iv_head_image);
        mRoundImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        //设置ToolBar为布局的ActionBar
        setSupportActionBar(mToolbar);

        //设置Drawer和ToolBar联动
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //设置Drawer和ToolBar联动
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        //设置侧滑菜单项点击事件监听
        mNavigationView.setNavigationItemSelectedListener(this);

        //初始化pager、mFragmentPagerAdapter、mFragments
        mFragmentPagerAdapter = new FragmentAdapter(getSupportFragmentManager());
        mFragments = new ArrayList<>();
        mFragments.add(new NewsFragment());
        mFragments.add(new HospitalFragment());
        mFragments.add(new RecipeFragment());
        mPager.setOffscreenPageLimit(mFragments.size() - 1);
        mPager.setAdapter(mFragmentPagerAdapter);
        //设置tabs与pager联动
        mPagerTitles = new ArrayList<>();
        mPagerTitles.add(getResources().getString(R.string.main_page_tab_journey));
        mPagerTitles.add(getResources().getString(R.string.main_page_tab_spot));
        mPagerTitles.add(getResources().getString(R.string.main_page_tab_tobe));
        mTabs.setupWithViewPager(mPager);
        mTabs.setTabsFromPagerAdapter(mFragmentPagerAdapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 2:
                        break;
                    default:
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm.isActive()) {
                            imm.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                        break;
                }

                switch (position) {
                    case 1:
                        mTvLocation.setVisibility(View.VISIBLE);
                        break;
                    default:
                        mTvLocation.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        //定位按钮点击监听
        mTvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ChooseCityActivity.class);
                startActivityForResult(intent, REQUEST_CODE_CHOOSE_CITY);
            }
        });
        //初始化IHospitalFragment接口
        mIHospitalFragment = (IHospitalFragment) mFragments.get(1);
        //注册广播接收器
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mLocationTime++;
                switch (intent.getAction()) {
                    case ACTION_LOCATION_SUCCESS:
                        String city = intent.getStringExtra("city");
                        String district = intent.getStringExtra("district");
                        String street = intent.getStringExtra("street");
                        String aoi = intent.getStringExtra("aoi");
                        LatLonPoint latLonPoint = intent.getParcelableExtra("latLonPoint");
                        //截取aio信息，去除圆括号包含的部分
                        if (aoi.contains("(")) {
                            aoi = aoi.substring(0, aoi.indexOf("("));
                        }
                        mTvLocation.setText(street + aoi);
                        mIHospitalFragment.setLocation(new Location(city, district, street, aoi, latLonPoint));
                        //停止服务
                        stopService(mIntent);
                        break;
                    case ACTION_LOCATION_FAIL:
                        if (mLocationTime <= 3) {
                            startActivity(mIntent);
                        } else {
                            mTvLocation.setText("定位失败，选择城市");
                        }
                }

            }
        };
        IntentFilter filter = new IntentFilter("ACTION_LOCATION_SUCCESS");
        registerReceiver(mReceiver, filter);
        //启动定位服务
        mIntent = new Intent(this, APSLocationService.class);
        startService(mIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CHOOSE_CITY && resultCode == RESULT_CODE_CHOOSE_CITY && data != null) {
            String cityName = data.getStringExtra(DATA_CITY_NAME);
            if (cityName != null) {
                mTvLocation.setText(cityName);
                mIHospitalFragment.setChosenCityId(data.getIntExtra(DATA_CITY_ID, 0));
            }
        }
    }

    @Override
    public ToastUtil getToastUtil() {
        return mToastUtil;
    }

    /**
     * 自定义的FragmentPagerAdapter
     */
    private class FragmentAdapter extends FragmentPagerAdapter {
        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mPagerTitles.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        //点击ToolBar左上返回键的响应
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // 侧滑菜单项点击事件的响应
        int id = item.getItemId();

        if (id == R.id.nav_my_main_page) {
            // Handle the camera action
        } else if (id == R.id.nav_news_collection) {

        } else if (id == R.id.nav_hospital_collection) {

        } else if (id == R.id.nav_recipe_collection) {

        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_exit) {

        }
        //关闭侧滑菜单
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Event(value = R.id.fab_to_top)
    private void onFabClick(View v) {
        //Fragment中的列表跳至顶部
        ((IFragment) (mFragmentPagerAdapter.getItem(mPager.getCurrentItem()))).skipToTop();
    }

    @Override
    protected void onStop() {
        stopService(mIntent);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }
}
