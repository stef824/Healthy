package com.satan.healthy.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.satan.healthy.R;
import com.satan.healthy.entitiy.City;
import com.satan.healthy.entitiy.Province;

import java.util.List;

/**
 * Created by Satan on 2016/08/17.
 */

public class CityAdapter extends BaseAdapter<City> {
    public CityAdapter(Context context, List<City> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(convertView==null){
            convertView = getInflater().inflate(R.layout.item_city,null);
            holder = new ViewHolder();
            holder.tvCity = (TextView) convertView.findViewById(R.id.tv_city_item);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvCity.setText(getItem(position).getCity());
        return convertView;
    }
    class ViewHolder{
        TextView tvCity;
    }
}
