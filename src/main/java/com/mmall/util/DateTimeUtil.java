package com.mmall.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * @author colaz
 * @date 2019/5/3
 **/
public class DateTimeUtil {
    //使用joda time

    public static Date strToDate(String strDate, String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(format);
        DateTime dateTime = dateTimeFormatter.parseDateTime(strDate);
        return dateTime.toDate();
    }

    public static String dateToStr(Date date, String format) {
        if (date == null)
            return StringUtils.EMPTY;
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(format);
    }
}
