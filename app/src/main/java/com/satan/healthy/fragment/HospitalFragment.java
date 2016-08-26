package com.satan.healthy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.satan.healthy.R;
import com.satan.healthy.activity.HospitalDetailActivity;
import com.satan.healthy.activity.interfaces.IMainActivity;
import com.satan.healthy.adapter.HospitalAdapter;
import com.satan.healthy.entitiy.Hospital;
import com.satan.healthy.entitiy.Location;
import com.satan.healthy.fragment.interfaces.IHospitalFragment;
import com.satan.healthy.presenter.IHospitalPresenter;
import com.satan.healthy.presenter.presenterImpl.HospitalPresenterImpl;
import com.satan.healthy.utils.ToastUtil;
import com.satan.healthy.view.IHospitalView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.satan.healthy.utils.ConstantFactory.DATA_CURRENT_CITY;
import static com.satan.healthy.utils.ConstantFactory.DATA_CURRENT_LATLONPOINT;
import static com.satan.healthy.utils.ConstantFactory.DATA_HOSPITAL_ID;
import static com.satan.healthy.utils.ConstantFactory.DATA_HOSPITAL_NAME;
import static com.satan.healthy.utils.ConstantFactory.DATA_IS_AUTO_LOCATION;
import static com.satan.healthy.utils.ConstantFactory.NAVI_HOSPITAL_ADDRESS;

/**
 * Created by Satan on 2016/08/15.
 */
@ContentView(R.layout.fragment_hospital)
public class HospitalFragment extends Fragment implements IHospitalView, IHospitalFragment {
    //注解控件
    @ViewInject(R.id.srw_refresh_hospital)
    private SwipeRefreshLayout mSwipeRefreshLayout;
    @ViewInject(R.id.rv_hospital)
    private RecyclerView mRecyclerView;

    private TextView mTVFooter;

    //发布器
    private IHospitalPresenter mPresenter;

    //宿主Activity接口
    private IMainActivity mIMainActivity;

    //Toast工具
    private ToastUtil mToastUtil;

    //定位结果
    private Location mLocation;
    //当前选择的城市id
    private int mCityId;
    //标记当前定位模式为自动定位或选择城市
    private boolean isAutoLocation;

    //医院列表数据集合
    private List<Hospital> mHospitals;
    //医院列表适配器
    private HospitalAdapter mHospitalAdapter;
    //医院列表布局管理器
    private LinearLayoutManager mLinearLayoutManager;
    //当前页数
    private int mPage;
    //总条目数
    private int mTotalItem = -1;
    //标记当前请求的数据是否加载完毕
    private boolean currentPageLoaded;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化发布器
        mPresenter = new HospitalPresenterImpl(this);
        //初始化宿主接口
        mIMainActivity = (IMainActivity) getActivity();
        //初始化Toast工具
        mToastUtil = mIMainActivity.getToastUtil();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        //初始化列表
        initList();
        return view;
    }

    /**
     * 初始化列表
     */
    private void initList() {
        //初始化列表数据集合
        mHospitals = new ArrayList<>();
        //初始化列表适配器
        mHospitalAdapter = new HospitalAdapter(R.layout.item_hospital, mHospitals);
        //设置载入动画
        mHospitalAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //设置尾部
        View footer = LayoutInflater.from(getActivity()).inflate(R.layout.footer_list, null);
        mTVFooter = (TextView) footer.findViewById(R.id.tv_footer);
        mHospitalAdapter.addFooterView(footer);
        //设置列表项点击监听
        mHospitalAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Intent intent = new Intent(getActivity(), HospitalDetailActivity.class);
                intent.putExtra(DATA_HOSPITAL_ID, mHospitals.get(i).getId());
                intent.putExtra(DATA_HOSPITAL_NAME, mHospitals.get(i).getName());
                intent.putExtra(DATA_IS_AUTO_LOCATION,isAutoLocation);
                if(mLocation!=null){
                    Log.d(NAVI_HOSPITAL_ADDRESS,mHospitals.get(i).getAddress());
                    intent.putExtra(NAVI_HOSPITAL_ADDRESS,mHospitals.get(i).getAddress());
                    intent.putExtra(DATA_CURRENT_CITY,mLocation.getCity());
                    intent.putExtra(DATA_CURRENT_LATLONPOINT,mLocation.getLatLonPoint());
                }
                startActivity(intent);
            }
        });
        //设置列表项长按监听，拦截长按事件，防止被点击监听处理
        mHospitalAdapter.setOnRecyclerViewItemLongClickListener(new BaseQuickAdapter.OnRecyclerViewItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int i) {
                return true;
            }
        });
        //设置列表的布局管理器
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        //设置加载更多
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLinearLayoutManager.findLastCompletelyVisibleItemPosition() == mLinearLayoutManager.getItemCount() - 1) {
                    if (mTotalItem==-1){
                        mTVFooter.setText("");
                        return;
                    }
                    if (mHospitals.size() == mTotalItem) {
                        mTVFooter.setText("--加载完成--");
                        return;
                    }
                    mTVFooter.setText("正在加载...");
                    if(!currentPageLoaded){
                        return;
                    }
                    if (isAutoLocation) {
                        if (mLocation != null) {
                            currentPageLoaded = false;
                            mPresenter.getHospitalsNearby(mLocation.getLatLonPoint().getLongitude(), mLocation.getLatLonPoint().getLatitude(), mPage + 1);
                        }
                    } else {
                        if (mCityId != 0) {
                            currentPageLoaded =false;
                            mPresenter.getHospitalsOfCity(mCityId, mPage + 1);
                        }
                    }
                }
            }
        });
        //绑定适配器
        mRecyclerView.setAdapter(mHospitalAdapter);
        //设置下拉刷新
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorGreen, R.color.colorRed);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!currentPageLoaded) {
                    stopRefreshing();
                    return;
                }
                if (isAutoLocation) {
                    getFirstPageHospitalsNearby();
                } else {
                    getFirstPageHospitalsOfCity();
                }
            }
        });
        //设置控件不可用
        setDisabled();
    }

    private void setDisabled() {
        mSwipeRefreshLayout.setEnabled(false);
        mRecyclerView.setEnabled(false);
    }


    @Override
    public void setChosenCityId(int cityId) {
        mCityId = cityId;
        //设置控件可用
        setEnabled();
        //获取选定城市的第一页数据
        getFirstPageHospitalsOfCity();
    }

    private void setEnabled() {
        mRecyclerView.setEnabled(true);
        mSwipeRefreshLayout.setEnabled(true);
    }

    /**
     * 获取第一页附近医院数据
     */
    private void getFirstPageHospitalsNearby() {
        //加载首次数据，页码置0，总条目数置-1
        mPage = 0;
        mTotalItem = -1;
        //当前页面请求未加载完毕
        currentPageLoaded = false;
        //加载数据
        mPresenter.getHospitalsNearby(mLocation.getLatLonPoint().getLongitude(), mLocation.getLatLonPoint().getLatitude(), mPage + 1);
        //设置定位模式为自动定位
        isAutoLocation = true;
        //刷新效果
        startRefreshing();

    }

    /**
     * 开启刷新效果
     */
    private void startRefreshing() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            return;
        }
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void setLocation(Location location) {
        mLocation = location;
        //设置控件可用
        setEnabled();
        //加载第一页附近医院数据
        getFirstPageHospitalsNearby();
    }

    /**
     * 获取第一页城市医院数据
     */
    private void getFirstPageHospitalsOfCity() {
        mPage = 0;
        mTotalItem = -1;
        currentPageLoaded = false;
        mPresenter.getHospitalsOfCity(mCityId, mPage + 1);
        //设置定位模式为选择城市
        isAutoLocation = false;
        //刷新效果
        startRefreshing();
    }

    @Override
    public void addToHospitals(List<Hospital> hospitals) {
        if (mPage == 0) {
            mHospitals.clear();
        }
        mHospitals.addAll(hospitals);
        mHospitalAdapter.notifyDataSetChanged();
        mPage += 1;
        if (mTotalItem == mHospitals.size()) {
            mTVFooter.setText("--加载完成--");
        }
        currentPageLoaded = true;
        //关闭刷新效果
        stopRefreshing();
        if (mPage==0){
            skipToTop();
        }
    }

    private void stopRefreshing() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.post(new Runnable() {

                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    }

    @Override
    public void showError(String error) {
        mToastUtil.showToast(error);
        currentPageLoaded = true;
        stopRefreshing();
    }

    @Override
    public void setTotalItem(int totalItem) {
        mTotalItem = totalItem;
    }

    @Override
    public void skipToTop() {
        mRecyclerView.scrollToPosition(0);
    }
}
