package com.satan.healthy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.satan.healthy.R;
import com.satan.healthy.activity.NewsDetailActivity;
import com.satan.healthy.activity.interfaces.IMainActivity;
import com.satan.healthy.adapter.LoopPagerAdapter;
import com.satan.healthy.adapter.NewsAdapter;
import com.satan.healthy.entitiy.News;
import com.satan.healthy.fragment.interfaces.IFragment;
import com.satan.healthy.presenter.INewsPresenter;
import com.satan.healthy.presenter.presenterImpl.NewsPresenterImpl;
import com.satan.healthy.ui.CircleIndicator;
import com.satan.healthy.ui.LoopViewPager;
import com.satan.healthy.utils.ToastUtil;
import com.satan.healthy.view.INewsView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.amap.api.col.c.m;
import static com.satan.healthy.utils.ConstantFactory.DATA_NEWS;

/**
 * Created by Satan on 2016/08/15.
 */
@ContentView(R.layout.fragment_news)
public class NewsFragment extends Fragment implements IFragment, INewsView {
    @ViewInject(R.id.rv_news)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.srl_news)
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ToastUtil mToastUtil;
    private INewsPresenter mPresenter;

    private List<News> mNewses;
    private NewsAdapter mNewsAdapter;
    private GridLayoutManager mGridLayoutManager;

    private LoopViewPager mViewPager;
    private LoopPagerAdapter mLoopPagerAdapter;
    private TextView mTVFooter;
    private CircleIndicator mIndicator;

    private int mPage;
    private int mTotalItems = -1;
    private boolean currentPageLoaded;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToastUtil = ((IMainActivity) getActivity()).getToastUtil();
        mPresenter = new NewsPresenterImpl(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        initList();
        getFirstPageNews();
        return view;
    }

    private void initList() {
        mNewses = new ArrayList<>();
        mNewsAdapter = new NewsAdapter(mNewses);
        mNewsAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //加头部
        ViewGroup parent = (ViewGroup) LayoutInflater.from(getActivity()).inflate(R.layout.item_big_news, null);
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.news_header, parent, false);
        prepareHeader(header);
        mNewsAdapter.addHeaderView(header);
        //加尾部
        View footer = View.inflate(getActivity(), R.layout.footer_list, null);
        mTVFooter = (TextView) footer.findViewById(R.id.tv_footer);
        mNewsAdapter.addFooterView(footer);
        //布局管理器
        mGridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(mNewsAdapter);
        //设置下拉刷新
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!currentPageLoaded) {
                    stopRefreshing();
                } else {
                    getFirstPageNews();
                }
            }
        });
        //设置加载更多
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mGridLayoutManager.findLastCompletelyVisibleItemPosition() + 1 == mGridLayoutManager.getItemCount()) {
                    if (mTotalItems == -1) {
                        mTVFooter.setText("");
                        return;
                    }
                    if (mNewses.size() == mTotalItems) {
                        mTVFooter.setText("--加载完成--");
                        return;
                    }
                    mTVFooter.setText("正在加载...");
                    //前一个请求页面已经加载完毕时，才会发送第二次请求，获取下一页数据，防止重复发送请求
                    if (currentPageLoaded) {
                        currentPageLoaded = false;
                        mPresenter.getNewses(mPage + 1);
                    }
                }
            }
        });
        //设置item点击监听
        mNewsAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                intent.putExtra(DATA_NEWS,mNewses.get(i));
                startActivity(intent);
            }
        });
        mNewsAdapter.setOnRecyclerViewItemLongClickListener(new BaseQuickAdapter.OnRecyclerViewItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int i) {
                return true;
            }
        });
    }

    private void prepareHeader(View header) {
        mViewPager = (LoopViewPager) header.findViewById(R.id.vp_new_header);
        mIndicator = (CircleIndicator) header.findViewById(R.id.indicator);
        int[] imageIds = {R.mipmap.header_image_01, R.mipmap.header_image_02, R.mipmap.header_image_03, R.mipmap.header_image_04};
        ImageView[] imageViews = new ImageView[imageIds.length];
        for (int i = 0; i < imageViews.length; i++) {
            imageViews[i] = new ImageView(getActivity());
            imageViews[i].setImageResource(imageIds[i]);
            imageViews[i].setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        //设置pager的适配器
        mLoopPagerAdapter = new LoopPagerAdapter(imageViews);
        mViewPager.setAdapter(mLoopPagerAdapter);
        mViewPager.setBoundaryLooping(true);
        //设置pager与indicator联动
        mIndicator.setViewPager(mViewPager);
    }

    @Override
    public void skipToTop() {
        mRecyclerView.scrollToPosition(0);
    }

    /**
     * 关闭刷新效果
     */
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
    public void setNewses(List<News> newses) {
        if (mPage == 0) {
            mNewses.clear();
        }
        mNewses.addAll(newses);
        mNewsAdapter.notifyDataSetChanged();
        mPage++;
        currentPageLoaded = true;
        stopRefreshing();
        if (mTotalItems == mNewses.size()) {
            mTVFooter.setText("--加载完成--");
        }
    }

    @Override
    public void setTotalItems(int totalItems) {
        mTotalItems = totalItems;
    }

    @Override
    public void showError(String error) {
        mToastUtil.showToast(error);
        currentPageLoaded = true;
        stopRefreshing();
    }

    public void getFirstPageNews() {
        mPage = 0;
        mTotalItems = -1;
        currentPageLoaded = false;
        mPresenter.getNewses(mPage + 1);
        startRefreshing();
    }
}
