package com.satan.healthy.model.modelImpl;

import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.satan.healthy.entitiy.CommonResponse;
import com.satan.healthy.entitiy.News;
import com.satan.healthy.entitiy.NewsResponse;
import com.satan.healthy.model.INewsModel;
import com.satan.healthy.utils.ConstantFactory;
import com.satan.healthy.utils.GsonUtil;

import java.util.List;

/**
 * Created by Satan on 2016/08/24.
 */

public class NewsModelImpl implements INewsModel {
    @Override
    public void getNewses(int page, final CommonCallback<NewsResponse> callback) {
        ApiStoreSDK.execute(ConstantFactory.getNewsesUrl(page), ApiStoreSDK.GET, null, new ApiCallBack() {
            @Override
            public void onSuccess(int i, String s) {
                CommonResponse commonResponse = (CommonResponse) GsonUtil.getEntity(s, CommonResponse.class);
                if (commonResponse.isStatus()) {
                    NewsResponse newsResponse = (NewsResponse) GsonUtil.getEntity(s, NewsResponse.class);
                    if (newsResponse.getTngou() != null && !newsResponse.getTngou().isEmpty()) {
                        setItemType(newsResponse.getTngou());
                        setKeywords(newsResponse.getTngou());
                        callback.onSuccess(newsResponse);
                    }
                } else {
                    callback.onError("服务器错误，请稍后再试");
                }
            }

            @Override
            public void onError(int i, String s, Exception e) {
                String error = e.getMessage();
                if (error != null && !"".equals(error)) {
                    callback.onError(e.getMessage());
                } else {
                    callback.onError("服务器错误，请稍后再试");
                }
            }
        });
    }

    private void setKeywords(List<News> newses) {
        for (News news : newses) {
            String[] keywords = news.getKeywords().split(" ");
            if (keywords.length<=3){
                continue;
            }
            news.setKeywords(keywords[0]+" "+keywords[1]+" "+keywords[2]);
        }
    }

    private void setItemType(List<News> newses) {
        for (int i = 0; i < newses.size(); i++) {
            if ((i + 1) % 6 == 0 || (i + 2) % 6 == 0) {
                newses.get(i).setItemType(News.TOW_ITEMS_PER_ROW);
            } else {
                newses.get(i).setItemType(News.ONE_ITEM_PER_ROW);
            }
        }
    }
}
