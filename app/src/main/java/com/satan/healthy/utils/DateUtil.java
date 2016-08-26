package com.satan.healthy.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Satan on 2016/08/24.
 */

public class DateUtil {
    public static String format(long timeMillis) {
        //待格式化的时间
        Calendar newsCalendar = Calendar.getInstance();
        newsCalendar.setTimeInMillis(timeMillis);
        //时间格式化工具
        SimpleDateFormat sdf = new SimpleDateFormat();
        //当前时间
        long currentTimeMillis = System.currentTimeMillis();
        Calendar currentTime = Calendar.getInstance();
        currentTime.setTimeInMillis(currentTimeMillis);
        //根据不同时间间隔，应用不同的格式化模板
        if (newsCalendar.get(Calendar.DAY_OF_YEAR) == currentTime.get(Calendar.DAY_OF_YEAR)) {
            sdf.applyPattern("HH:mm");
            return sdf.format(newsCalendar.getTime());
        } else if (currentTime.get(Calendar.DAY_OF_YEAR) - newsCalendar.get(Calendar.DAY_OF_YEAR) == 1) {
            sdf.applyPattern("昨天 HH:mm");
            return sdf.format(newsCalendar.getTime());
        } else if (currentTime.get(Calendar.YEAR) == newsCalendar.get(Calendar.YEAR)){
            sdf.applyPattern("MM月dd日 HH:mm");
            return sdf.format(newsCalendar.getTime());
        } else {
            sdf.applyPattern("yyyy年MM月dd日 HH:mm");
            return sdf.format(newsCalendar.getTime());
        }
    }
}
