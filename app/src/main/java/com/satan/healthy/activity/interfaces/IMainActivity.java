package com.satan.healthy.activity.interfaces;

import com.satan.healthy.utils.ToastUtil;

/**
 * MainActivity提供的接口，提供一系列方法，供Fragment调用
 */
public interface IMainActivity {
    ToastUtil getToastUtil();
}
