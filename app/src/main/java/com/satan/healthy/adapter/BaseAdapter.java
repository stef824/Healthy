package com.satan.healthy.adapter;

import android.content.Context;
import android.view.LayoutInflater;

import java.util.List;

public abstract class BaseAdapter<E> extends android.widget.BaseAdapter {
	private Context context;
	private List<E> data;
	private LayoutInflater inflater;

	public BaseAdapter(Context context, List<E> data) {
		super();
		this.context = context;
		this.data = data;
		setInflater();
	}

	protected Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	protected List<E> getData() {
		return data;
	}

	public void setData(List<E> data) {
		this.data = data;
	}

	protected LayoutInflater getInflater() {
		return inflater;
	}

	private void setInflater() {
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public E getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}
