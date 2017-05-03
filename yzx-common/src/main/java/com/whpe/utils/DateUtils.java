package com.whpe.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/3/3.
 */
public class DateUtils {

    private static SimpleDateFormat sdf = new SimpleDateFormat();

    public static String getFormatDate(Date date, String format){
        sdf.applyPattern(format);
        return sdf.format(date);
    }
}
