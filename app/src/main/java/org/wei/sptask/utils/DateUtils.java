package org.wei.sptask.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin on 2/8/2016.
 */
public class DateUtils {
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_ZONE_FORMAT="yyyy-MM-dd'T'HH:mm:ssZ";
    public static final String TIME_FORMAT = "h a";


    public static Date getDate(Long val){
        return new Date(val);
    }

    public static String getDateTimeString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(DateUtils.DATE_TIME_FORMAT);
        return formatter.format(date);
    }

    public static String getDateString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(DateUtils.DATE_FORMAT);
        return formatter.format(date);
    }

    public static String getTimeStringFromDateTimeZone(String str){
        SimpleDateFormat from = new SimpleDateFormat(DateUtils.DATE_TIME_ZONE_FORMAT);
        SimpleDateFormat to = new SimpleDateFormat(DateUtils.TIME_FORMAT);
        try {
            Date date = from.parse(str);
            return to.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
