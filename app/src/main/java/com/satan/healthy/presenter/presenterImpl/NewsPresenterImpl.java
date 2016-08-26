package com.satan.healthy.presenter.presenterImpl;

import com.satan.healthy.entitiy.NewsResponse;
import com.satan.healthy.model.IModel;
import com.satan.healthy.model.INewsModel;
import com.satan.healthy.model.modelImpl.NewsModelImpl;
import com.satan.healthy.presenter.INewsPresenter;
import com.satan.healthy.view.INewsView;

/**
 * Created by Satan on 2016/08/24.
 */

public class NewsPresenterImpl implements INewsPresenter {
    private INewsView mView;
    private INewsModel mModel;

    public NewsPresenterImpl(INewsView view) {
        mView = view;
        mModel = new NewsModelImpl();
    }

    @Override
    public void getNewses(final int page) {
        mModel.getNewses(page, new IModel.CommonCallback<NewsResponse>() {
            @Override
            public void onSuccess(NewsResponse newsResponse) {
                if (page==1){
                    mView.setTotalItems(90);
                }
                mView.setNewses(newsResponse.getTngou());
            }

            @Override
            public void onError(String error) {
                mView.showError(error);
            }
        });
    }
}
