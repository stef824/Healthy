package com.satan.healthy.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Satan on 2016/08/18.
 */

public class TelNumUtil {
    public static String[] getTelNumsFromCharsequence(CharSequence sequence) {
        String[] telNums = null;
        Pattern mobile = Pattern.compile("1[3|4|5|7|8][0-9]\\d{8}");
        Pattern prefix = Pattern.compile("\\d{3,4}-|\\d{3,4}\\s-");
        Pattern suffix = Pattern.compile("-\\d{7,8}|-\\s\\d{7,8}|,\\d{7,8}|，\\d{7,8}");
        Matcher matcher;
        //提取手机号
        String mobileNum = null;
        matcher = mobile.matcher(sequence);
        while (matcher.find()) {
            mobileNum = matcher.group();
            break;
        }
        //提取区号
        String code = null;
        matcher = prefix.matcher(sequence);
        while (matcher.find()) {
            code = matcher.group();
            break;
        }
        //提取座机号
        List<String> tels = new ArrayList<>();
        matcher = suffix.matcher(sequence);
        while (matcher.find()) {
            tels.add(matcher.group().substring(1));
        }
        //组合提取出的数据
        if (mobileNum != null) {
            telNums = new String[tels.size() + 1];
            for (int i = 0; i < tels.size(); i++) {
                telNums[i] = code + tels.get(i);
            }
            telNums[telNums.length - 1] = mobileNum;
        } else {
            telNums = new String[tels.size()];
            for (int i = 0; i < tels.size(); i++) {
                telNums[i] = code + tels.get(i);
            }
        }
        return telNums;
    }
}
