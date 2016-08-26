package com.satan.healthy;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.satan.healthy.entitiy.CityListResponse;
import com.satan.healthy.entitiy.Province;
import com.satan.healthy.model.IModel;
import com.satan.healthy.utils.GsonUtil;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import org.xutils.x;

import java.util.List;

import static com.satan.healthy.utils.ConstantFactory.URL_PROVINCES_AND_CITIES;

/**
 * Created by Satan on 2016/08/15.
 */

public class HealthyApplication extends Application {
    /**
     * 持有自身对象引用
     */
    private static HealthyApplication app;
    private List<Province> mProvinces;
    private RequestQueue mQueue;
    private ImageLoader mImageLoader;
    private LruCache<String, Bitmap> mCache;
    private RefWatcher mRefWatcher;

    public HealthyApplication() {
        app = this;
    }

    public static HealthyApplication getApp() {
        return app;
    }

    public RequestQueue getQueue() {
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(this);
        }
        return mQueue;
    }

    public RefWatcher getRefWatcher() {
        return mRefWatcher;
    }

    public ImageLoader getImageLoader() {
        if (mImageLoader == null) {
            mCache = new LruCache<>((int) (Runtime.getRuntime().maxMemory() / 8 / 1024));
            Log.d("cache", (int) (Runtime.getRuntime().maxMemory() / 8 / 1024) + "");
            mImageLoader = new ImageLoader(getQueue(), new ImageLoader.ImageCache() {
                @Override
                public Bitmap getBitmap(String s) {
                    return mCache.get(s);
                }

                @Override
                public void putBitmap(String s, Bitmap bitmap) {
                    if (mCache.get(s) == null) {
                        mCache.put(s, bitmap);
                    }
                }
            });
        }
        return mImageLoader;
    }

    public void getProvinces(final IModel.CommonCallback<List<Province>> callback) {
        if (mProvinces == null) {
            ApiStoreSDK.execute(URL_PROVINCES_AND_CITIES, ApiStoreSDK.GET, null, new ApiCallBack() {
                @Override
                public void onSuccess(int i, String s) {
                    CityListResponse response = (CityListResponse) GsonUtil.getEntity(s, CityListResponse.class);
                    if (response.isStatus()) {
                        if (response.getTngou() != null && !response.getTngou().isEmpty()) {
                            callback.onSuccess(response.getTngou());
                            mProvinces = response.getTngou();
                        }
                    } else {
                        callback.onError("服务器错误，请稍后重试");
                    }
                }

                @Override
                public void onError(int i, String s, Exception e) {
                    String error = e.getMessage();
                    if (error != null && !"".equals(error)) {
                        callback.onError(e.getMessage());
                    }
                }
            });
        }else{
            callback.onSuccess(mProvinces);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mRefWatcher = LeakCanary.install(this);
        ApiStoreSDK.init(this, "0ff3337b6d565e37e2e42f7a70e56351");
        x.Ext.init(this);
    }

    @Override
    public void onTerminate() {
        mCache.evictAll();
        mQueue.stop();
        mQueue = null;
        app = null;
        super.onTerminate();
    }
}
