package com.satan.healthy.model.modelImpl;

import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.satan.healthy.entitiy.CommonResponse;
import com.satan.healthy.entitiy.News;
import com.satan.healthy.model.INewsDetailModel;
import com.satan.healthy.utils.ConstantFactory;
import com.satan.healthy.utils.GsonUtil;

/**
 * Created by Satan on 2016/08/25.
 */

public class NewsDetailModelImpl implements INewsDetailModel {

    @Override
    public void getNewsContent(int newsId, final CommonCallback<News> callback) {
        ApiStoreSDK.execute(ConstantFactory.getNewsUrl(newsId),ApiStoreSDK.GET,null,new ApiCallBack(){
            @Override
            public void onSuccess(int i, String s) {
                CommonResponse commonResponse = (CommonResponse) GsonUtil.getEntity(s,CommonResponse.class);
                if (commonResponse.isStatus()){
                    News news = (News) GsonUtil.getEntity(s,News.class);
                    callback.onSuccess(news);
                }else {
                    callback.onError("服务器错误，获取内容失败");
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
}
