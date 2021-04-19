package com.veggie.thefarm.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Created by Bohyun on 2021.04.16
 */
public class TimeUtils {

    public static String getFormatTimeStr(String format, Timestamp time) {
        try {
            return new SimpleDateFormat(format).format(time);
        } catch (Exception e) {
            return null;
        }
    }

}
