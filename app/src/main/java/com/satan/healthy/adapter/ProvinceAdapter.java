package com.satan.healthy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.satan.healthy.R;
import com.satan.healthy.entitiy.Province;

import java.util.List;

/**
 * Created by Satan on 2016/08/17.
 */

public class ProvinceAdapter extends BaseAdapter<Province> {
    public ProvinceAdapter(Context context, List<Province> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(convertView==null){
            convertView = getInflater().inflate(R.layout.item_province,null);
            holder = new ViewHolder();
            holder.tvProvince = (TextView) convertView.findViewById(R.id.tv_province_item);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvProvince.setText(getItem(position).getProvince());
        return convertView;
    }
    class ViewHolder{
        TextView tvProvince;
    }
}
