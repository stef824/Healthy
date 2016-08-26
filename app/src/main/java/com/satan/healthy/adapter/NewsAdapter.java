package com.satan.healthy.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.satan.healthy.R;
import com.satan.healthy.entitiy.News;
import com.satan.healthy.utils.ConstantFactory;
import com.satan.healthy.utils.DateUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.amap.api.col.v.i;

/**
 * Created by Satan on 2016/08/24.
 */
public class NewsAdapter extends BaseMultiItemQuickAdapter<News> {


    public NewsAdapter(List<News> data) {
        super(data);
        addItemType(News.ONE_ITEM_PER_ROW, R.layout.item_big_news);
        addItemType(News.TOW_ITEMS_PER_ROW, R.layout.item_small_news);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, News news) {
        int itemType = baseViewHolder.getItemViewType();
        if (itemType == News.ONE_ITEM_PER_ROW) {
            Picasso.with(mContext).load(ConstantFactory.getImageUrl(news.getImg())).into((ImageView) baseViewHolder.getView(R.id.iv_big_news_item));
            baseViewHolder.setText(R.id.tv_big_news_item_title, news.getTitle());
            baseViewHolder.setText(R.id.tv_big_news_item_keywords, news.getKeywords());
            baseViewHolder.setText(R.id.tv_big_news_item_time, DateUtil.format(news.getTime()));
        } else if (itemType == News.TOW_ITEMS_PER_ROW) {
            baseViewHolder.setText(R.id.tv_small_news_item, news.getTitle());
            Picasso.with(mContext).load(ConstantFactory.getImageUrl(news.getImg())).into((ImageView) baseViewHolder.getView(R.id.iv_small_news_item));
        }
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position==0){
                        return 2;
                    }
                    if ((position+1)%6==0||(position)%6==0){
                        return 1;
                    }
                    return 2;
                }
            });
        }
    }
}
