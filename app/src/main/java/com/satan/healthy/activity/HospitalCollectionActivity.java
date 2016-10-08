package com.satan.healthy.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.satan.healthy.HealthyApplication;
import com.satan.healthy.R;
import com.satan.healthy.adapter.NewsCollectionAdapter;
import com.satan.healthy.dal.UserDao;
import com.satan.healthy.entitiy.CommonResponse;
import com.satan.healthy.entitiy.News;
import com.satan.healthy.entitiy.User;
import com.satan.healthy.utils.ConstantFactory;
import com.satan.healthy.utils.GsonUtil;
import com.satan.healthy.utils.ToastUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import static com.satan.healthy.utils.ConstantFactory.DATA_NEWS;
import static com.satan.healthy.utils.ConstantFactory.REQUEST_CODE_LOGIN;
import static com.satan.healthy.utils.ConstantFactory.RESULT_CODE_LOGIN;

@ContentView(R.layout.activity_news_collection)
public class HospitalCollectionActivity extends AppCompatActivity {
    @ViewInject(R.id.toolbar_news_collection)
    private Toolbar mToolbar;
    @ViewInject(R.id.rv_news_collections)
    private RecyclerView mRecyclerView;

    private User mUser;

    private UserDao mUserDao;

    private NewsCollectionAdapter mAdapter;

    private GridLayoutManager mLayoutManager;

    private ToastUtil mToastUtil;
    private List<Integer> mNewsIds;
    private AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try {
            mUserDao = new UserDao(HealthyApplication.getApp());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mToastUtil = new ToastUtil(this);
        mUser = HealthyApplication.getApp().getUser();
        if (mUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_CODE_LOGIN);
        } else {
            showFall();
        }

    }

    private void showFall() {
        new QueryNewsUrlsTask().execute(mUser);
    }

    private class QueryNewsUrlsTask extends AsyncTask<User, Void, List<Integer>> {


        @Override
        protected List<Integer> doInBackground(User... params) {
            mNewsIds = mUserDao.queryCollections(params[0], UserDao.COLLECTION_CATEGORY_NEWS);
            return mNewsIds;
        }

        @Override
        protected void onPostExecute(final List<Integer> newsIds) {
            mAdapter = new NewsCollectionAdapter(R.layout.item_collection_fall, newsIds);
            mAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int i) {
                    ApiStoreSDK.execute(ConstantFactory.getNewsUrl(newsIds.get(i)), ApiStoreSDK.GET, null, new ApiCallBack() {
                        @Override
                        public void onError(int i, String s, Exception e) {
                            mToastUtil.showToast("服务器错误，请稍后重试");
                        }

                        @Override
                        public void onSuccess(int i, String s) {
                            CommonResponse commonResponse = (CommonResponse) GsonUtil.getEntity(s, CommonResponse.class);
                            if (commonResponse.isStatus()) {
                                News news = (News) GsonUtil.getEntity(s, News.class);
                                Intent intent = new Intent(HospitalCollectionActivity.this, NewsDetailActivity.class);
                                intent.putExtra(DATA_NEWS, news);
                                startActivity(intent);
                            } else {
                                mToastUtil.showToast("服务器错误，请稍后重试");
                            }
                        }
                    });
                }
            });
            mAdapter.setOnRecyclerViewItemLongClickListener(new BaseQuickAdapter.OnRecyclerViewItemLongClickListener() {
                @Override
                public boolean onItemLongClick(View view, final int i) {
                    DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_NEGATIVE:
                                    dialog.dismiss();
                                    break;
                                case DialogInterface.BUTTON_POSITIVE:
                                    int count = mUserDao.deleteCollection(mUser, mNewsIds.get(i), UserDao.COLLECTION_CATEGORY_NEWS);
                                    if (count == 1) {
                                        mNewsIds.remove(i);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                    break;
                            }
                        }
                    };
                    if (mDialog == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(HospitalCollectionActivity.this);
                        mDialog = builder.setTitle("删除资讯收藏")
                                .setMessage("确定删除此条资讯收藏？")
                                .setNegativeButton("取消", onClickListener)
                                .setPositiveButton("确定", onClickListener)
                                .create();
                    }
                    mDialog.show();
                    return true;
                }
            });
            mRecyclerView.setAdapter(mAdapter);
            mLayoutManager = new GridLayoutManager(HospitalCollectionActivity.this, 2);
            mLayoutManager.setAutoMeasureEnabled(true);
            mRecyclerView.setLayoutManager(mLayoutManager);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUser = HealthyApplication.getApp().getUser();
        if (mUser != null) {
            showFall();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_LOGIN && resultCode == RESULT_CODE_LOGIN) {
            mUser = HealthyApplication.getApp().getUser();
            if (mUser != null) {
                showFall();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
