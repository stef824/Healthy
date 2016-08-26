package com.satan.healthy.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.satan.healthy.R;
import com.satan.healthy.entitiy.Hospital;
import com.satan.healthy.utils.ConstantFactory;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Satan on 2016/08/16.
 */
public class HospitalAdapter extends BaseQuickAdapter<Hospital>{
    public HospitalAdapter(int layoutResId, List<Hospital> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Hospital hospital) {
        baseViewHolder.setText(R.id.tv_hospital_item_title,hospital.getName());
        baseViewHolder.setText(R.id.tv_hospital_item_level,hospital.getLevel());
        Picasso.with(mContext).load(ConstantFactory.getImageUrl(hospital.getImg())).into((ImageView) baseViewHolder.getView(R.id.iv_hospital_item));
    }
}
