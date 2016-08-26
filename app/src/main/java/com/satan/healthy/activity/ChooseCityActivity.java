package com.satan.healthy.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.satan.healthy.R;
import com.satan.healthy.adapter.CityAdapter;
import com.satan.healthy.adapter.ProvinceAdapter;
import com.satan.healthy.entitiy.City;
import com.satan.healthy.entitiy.Province;
import com.satan.healthy.presenter.IChooseCityPresenter;
import com.satan.healthy.presenter.presenterImpl.ChooseCityPresenterImpl;
import com.satan.healthy.utils.ToastUtil;
import com.satan.healthy.view.IChooseCityView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.satan.healthy.utils.ConstantFactory.DATA_CITY_ID;
import static com.satan.healthy.utils.ConstantFactory.DATA_CITY_NAME;
import static com.satan.healthy.utils.ConstantFactory.RESULT_CODE_CHOOSE_CITY;

@ContentView(R.layout.activity_choose_city)
public class ChooseCityActivity extends AppCompatActivity implements IChooseCityView {
    //注解控件
    @ViewInject(R.id.toolbar_choose_city)
    private Toolbar mToolbar;
    @ViewInject(R.id.et_search_city)
    private EditText mETSearch;
    @ViewInject(R.id.lv_search_list)
    private ListView mLVSearch;
    @ViewInject(R.id.lv_provinces)
    private ListView mLVProvinces;
    @ViewInject(R.id.lv_cities)
    private ListView mLVCities;

    private IChooseCityPresenter mPresenter;
    private ToastUtil mToastUtil;

    private List<Province> mProvinces;
    private ProvinceAdapter mProvinceAdapter;
    private List<City> mCities;
    private CityAdapter mCityAdapter;
    private List<City> mMatchedCites;
    private ArrayAdapter<City> mMatchedCityAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        setSupportActionBar(mToolbar);
        //设置Toolbar返回图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mPresenter = new ChooseCityPresenterImpl(this);
        mToastUtil = new ToastUtil(this);
        //为城市和匹配城市列表初始化
        mCities = new ArrayList<>();
        mCityAdapter = new CityAdapter(this, mCities);
        mLVCities.setAdapter(mCityAdapter);
        mMatchedCites = new ArrayList<>();
        mMatchedCityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mMatchedCites);
        mLVSearch.setAdapter(mMatchedCityAdapter);
        //获取省市列表
        mPresenter.getProvincesAndCities();
    }


    @Override
    public void setProvinces(List<Province> provinces) {
        mProvinces = provinces;
        //显示省份列表
        mProvinceAdapter = new ProvinceAdapter(this, mProvinces);
        mLVProvinces.setAdapter(mProvinceAdapter);
        //为省份列表设置监听
        mLVProvinces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //显示对应省份的城市列表
                mCities.clear();
                mCities.addAll(mProvinces.get(i).getCitys());
                mCityAdapter.notifyDataSetChanged();
            }
        });
        mETSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("onTextChanged", String.valueOf(charSequence));
                mPresenter.getMatchedCities(mProvinces, String.valueOf(charSequence));
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    @Override
    public void setMatchedCities(List<City> cities) {
        if (cities.isEmpty()) {
            mLVSearch.setVisibility(View.GONE);
        } else {
            mLVSearch.setVisibility(View.VISIBLE);
            mMatchedCites.clear();
            mMatchedCites.addAll(cities);
            mMatchedCityAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showError(String error) {
        mToastUtil.showToast(error);
    }

    //搜索框匹配城市列表项点击监听
    @Event(value = R.id.lv_search_list, type = AdapterView.OnItemClickListener.class)
    private void onMatchedCityClick(AdapterView<?> parent, View v, int position, long id) {
        City cityChosen = mMatchedCites.get(position);
        Intent data = new Intent();
        data.putExtra(DATA_CITY_ID, cityChosen.getId());
        data.putExtra(DATA_CITY_NAME, cityChosen.getCity());
        setResult(RESULT_CODE_CHOOSE_CITY, data);
        finish();
    }

    //城市列表项点击监听
    @Event(value = R.id.lv_cities, type = AdapterView.OnItemClickListener.class)
    private void onCityClick(AdapterView<?> parent, View v, int position, long id) {
        City cityChosen = mCities.get(position);
        Intent data = new Intent();
        data.putExtra(DATA_CITY_ID, cityChosen.getId());
        data.putExtra(DATA_CITY_NAME, cityChosen.getCity());
        setResult(RESULT_CODE_CHOOSE_CITY, data);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()){
                imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
            }
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
