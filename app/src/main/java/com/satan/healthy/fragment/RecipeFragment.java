package com.satan.healthy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.satan.healthy.R;
import com.satan.healthy.activity.RecipeDetailActivity;
import com.satan.healthy.activity.interfaces.IMainActivity;
import com.satan.healthy.adapter.RecipeAdapter;
import com.satan.healthy.entitiy.Recipe;
import com.satan.healthy.fragment.interfaces.IFragment;
import com.satan.healthy.presenter.IRecipePresenter;
import com.satan.healthy.presenter.presenterImpl.RecipePresenterImpl;
import com.satan.healthy.utils.ToastUtil;
import com.satan.healthy.view.IRecipeView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.satan.healthy.utils.ConstantFactory.DATA_RECIPE;

/**
 * Created by Satan on 2016/08/15.
 */
@ContentView(R.layout.fragment_recipe)
public class RecipeFragment extends Fragment implements IFragment, IRecipeView {
    private int RECIPE_TYPE_LIST = 0;
    private int RECIPE_TYPE_SEARCH = 1;

    //注解控件
    @ViewInject(R.id.et_search_recipe)
    private EditText mETSearchRecipe;
    @ViewInject(R.id.iv_search_recipe_icon)
    private ImageView mIVSearchIcon;
    @ViewInject(R.id.rv_recipe)
    private RecyclerView mRVRecipe;
    @ViewInject(R.id.srw_refresh_recipe)
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private IRecipePresenter mPresenter;
    private ToastUtil mToastUtil;
    private IMainActivity mIMainActivity;

    private List<Recipe> mRecipes;
    private RecipeAdapter mAdapter;
    private GridLayoutManager mGridLayoutManager;
    private TextView mTVFooter;
    private int mTotalItem = -1;
    private int mPage;

    private int mRecipeType;
    private String mInput;
    private boolean currentPageLoaded;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new RecipePresenterImpl(this);
        mIMainActivity = (IMainActivity) getActivity();
        mToastUtil = mIMainActivity.getToastUtil();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        initList();
        getFirstPageRecipe();
        return view;
    }

    private void initList() {
        mRecipes = new ArrayList<>();
        mAdapter = new RecipeAdapter(R.layout.item_recipe, mRecipes);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        //设置尾部
        View footer = LayoutInflater.from(getActivity()).inflate(R.layout.footer_list, null);
        mTVFooter = (TextView) footer.findViewById(R.id.tv_footer);
        mAdapter.addFooterView(footer);
        //设置列表项点击监听
        mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
                intent.putExtra(DATA_RECIPE, mRecipes.get(i));
                startActivity(intent);
            }
        });
        //设置列表项长按监听，拦截长按事件，防止被点击监听处理
        mAdapter.setOnRecyclerViewItemLongClickListener(new BaseQuickAdapter.OnRecyclerViewItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int i) {
                return true;
            }
        });
        //列表布局管理器
        mGridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        mRVRecipe.setLayoutManager(mGridLayoutManager);
        mRVRecipe.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mGridLayoutManager.findLastCompletelyVisibleItemPosition() == mGridLayoutManager.getItemCount() - 1) {
                    if (mTotalItem==-1){
                        mTVFooter.setText("");
                        return;
                    }
                    if (mRecipes.size() == mTotalItem) {
                        mTVFooter.setText("--加载完成--");
                        return;
                    }
                    mTVFooter.setText("正在加载...");
                    if (currentPageLoaded) {
                        currentPageLoaded = false;
                        mPresenter.getRecipeList(mPage + 1);
                    }
                }
            }
        });
        //绑定适配器
        mRVRecipe.setAdapter(mAdapter);
        //下拉刷新
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorGreen, R.color.colorRed);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!currentPageLoaded) {
                    stopRefreshing();
                    return;
                }
                if (mRecipeType == RECIPE_TYPE_LIST) {
                    getFirstPageRecipe();
                } else if (mRecipeType == RECIPE_TYPE_SEARCH) {
                    getSearchRecipe();
                }

            }
        });
    }

    @Override
    public void skipToTop() {
        mRVRecipe.scrollToPosition(0);
    }

    @Override
    public void addToRecipes(List<Recipe> recipes) {
        if (mPage == 0) {
            mRecipes.clear();
        }
        mRecipes.addAll(recipes);
        mAdapter.notifyDataSetChanged();
        currentPageLoaded = true;
        mPage += 1;
        if (mTotalItem == mRecipes.size()) {
            mTVFooter.setText("--加载完成--");
        }
        stopRefreshing();
    }

    @Override
    public void setTotalItem(int total) {
        mTotalItem = total;
    }

    @Override
    public void showError(String error) {
        mToastUtil.showToast(error);
        currentPageLoaded = true;
        stopRefreshing();
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

    public void getFirstPageRecipe() {
        mPage = 0;
        mTotalItem = -1;
        currentPageLoaded = false;
        mPresenter.getRecipeList(mPage + 1);
        mRecipeType = RECIPE_TYPE_LIST;
        startRefreshing();
    }

    public void getSearchRecipe() {
        mPage = 0;
        mTotalItem = -1;
        currentPageLoaded = false;
        mPresenter.searchRecipe(mInput);
        mRecipeType = RECIPE_TYPE_SEARCH;
        startRefreshing();
    }

    @Event(value = R.id.iv_search_recipe_icon)
    private void onSearchClick(View v) {
        searchRecipe();

    }
    @Event(value = R.id.et_search_recipe,type= TextView.OnEditorActionListener.class)
    private boolean onEditorAction(TextView v, int actionId, KeyEvent event){
        if (actionId== EditorInfo.IME_ACTION_SEARCH){
            searchRecipe();
            return true;
        }else {
            return false;
        }
    }

    private void searchRecipe() {
        mInput = mETSearchRecipe.getText().toString();
        if (mRecipeType == RECIPE_TYPE_LIST) {
            if ("".equals(mInput)) {

            } else {
                getSearchRecipe();
            }
        } else if (mRecipeType == RECIPE_TYPE_SEARCH) {
            if ("".equals(mInput)) {
                getFirstPageRecipe();
            } else {
                getSearchRecipe();
            }
        }
    }
}
