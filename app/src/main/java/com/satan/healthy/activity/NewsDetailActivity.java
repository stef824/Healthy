package com.satan.healthy.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.satan.healthy.R;
import com.satan.healthy.entitiy.News;
import com.satan.healthy.presenter.INewsDetailPresenter;
import com.satan.healthy.presenter.presenterImpl.NewsDetailPresenterImpl;
import com.satan.healthy.utils.ConstantFactory;
import com.satan.healthy.utils.DateUtil;
import com.satan.healthy.utils.ToastUtil;
import com.satan.healthy.view.INewsDetailView;
import com.satan.healthy.view.IView;
import com.squareup.picasso.Picasso;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import static com.satan.healthy.utils.ConstantFactory.DATA_NEWS;

@ContentView(R.layout.activity_news_detail)
public class NewsDetailActivity extends AppCompatActivity implements INewsDetailView{
    @ViewInject(R.id.toolbar_news_detail)
    private Toolbar mToolbar;
    @ViewInject(R.id.tv_news_detail_title)
    private TextView mTVTitle;
    @ViewInject(R.id.tv_news_detail_time)
    private TextView mTVTime;
    @ViewInject(R.id.tv_news_detail_content)
    private TextView mTVContent;
    @ViewInject(R.id.iv_news_detail_img)
    private ImageView mImageView;

    private News mNews;

    private ToastUtil mToastUtil;
    private INewsDetailPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToastUtil = new ToastUtil(this);
        mPresenter = new NewsDetailPresenterImpl(this);
        Intent intent = getIntent();
        mNews = (News) intent.getSerializableExtra(DATA_NEWS);
        mTVTitle.setText(mNews.getTitle());
        mTVTime.setText(DateUtil.format(mNews.getTime()));
        mPresenter.getNewsContent(mNews.getId());
        Picasso.with(this).load(ConstantFactory.getImageUrl(mNews.getImg())).into(mImageView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }else if(id==R.id.action_share_news){
            // TODO: 2016/08/24 分享
        }else if (id==R.id.action_collect_news){
            // TODO: 2016/08/25 收藏
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news_detail, menu);
        return true;
    }

    @Override
    public void setNewsContent(News news) {
        mNews.setMessag(news.getMessage());
        Spanned spanned = Html.fromHtml(mNews.getMessage());
        mTVContent.setText(spanned);
    }

    @Override
    public void showError(String error) {
        mToastUtil.showToast(error);
    }
}
