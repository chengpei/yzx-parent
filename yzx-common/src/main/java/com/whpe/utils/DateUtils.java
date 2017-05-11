package com.whpe.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/3/3.
 */
public class DateUtils {

    private static SimpleDateFormat sdf = new SimpleDateFormat();

    public static String getFormatDate(Date date, String format){
        if (date == null){
            return null;
        }
        sdf.applyPattern(format);
        return sdf.format(date);
    }

    public static Date getDateForString(String hostDateTime, String format) throws ParseException {
        if (StringUtils.isEmpty(hostDateTime)){
            return null;
        }
        sdf.applyPattern(format);
        return sdf.parse(hostDateTime);
    }
}
