package com.satan.healthy.presenter.presenterImpl;

import com.satan.healthy.entitiy.News;
import com.satan.healthy.model.IModel;
import com.satan.healthy.model.INewsDetailModel;
import com.satan.healthy.model.modelImpl.NewsDetailModelImpl;
import com.satan.healthy.presenter.INewsDetailPresenter;
import com.satan.healthy.view.INewsDetailView;

/**
 * Created by Satan on 2016/08/25.
 */

public class NewsDetailPresenterImpl implements INewsDetailPresenter {
    private INewsDetailView mView;
    private INewsDetailModel mModel;

    public NewsDetailPresenterImpl(INewsDetailView view) {
        mView = view;
        mModel = new NewsDetailModelImpl();
    }

    @Override
    public void getNewsContent(int newsId) {
        mModel.getNewsContent(newsId, new IModel.CommonCallback<News>() {
            @Override
            public void onSuccess(News news) {
                mView.setNewsContent(news);
            }

            @Override
            public void onError(String error) {
                mView.showError(error);
            }
        });
    }
}
