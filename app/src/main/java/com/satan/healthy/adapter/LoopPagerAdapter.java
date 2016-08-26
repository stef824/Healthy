package com.satan.healthy.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

/**
 * Created by Satan on 2016/08/24.
 */

public class LoopPagerAdapter extends PagerAdapter {
    private ImageView[] mImageViews;

    public LoopPagerAdapter(ImageView[] imageViews) {
        mImageViews = imageViews;
    }

    @Override
    public int getCount() {
        return mImageViews.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView view = mImageViews[position];
        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
        ViewParent vp = view.getParent();
        if (vp != null) {
            ViewGroup parent = (ViewGroup) vp;
            parent.removeView(view);
        }
        container.addView(view, null);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ImageView view = mImageViews[position];
        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
        ViewParent vp = view.getParent();
        if (vp != null) {
            ViewGroup parent = (ViewGroup) vp;
            parent.removeView(view);
        }
        container.addView(view, null);
    }
}
