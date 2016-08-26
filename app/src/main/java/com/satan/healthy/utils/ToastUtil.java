package com.satan.healthy.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Satan on 2016/08/06.
 */

public class ToastUtil {
    private Toast toast;
    private Context context;
    public ToastUtil(Context context){
        this.context = context;
        toast = Toast.makeText(context,"",Toast.LENGTH_SHORT);
    }
    public void showToast(String msg){
        toast.setText(msg);
        toast.show();
    }
    public void dismissToast(){
        if (toast!=null){
            toast.cancel();
        }
    }
}
