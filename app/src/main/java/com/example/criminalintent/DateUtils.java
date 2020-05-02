package com.example.criminalintent;

import android.content.Context;
import android.text.format.DateFormat;

import java.util.Date;

public class DateUtils {
    /**挑战练习九 自定义时间格式
     * @param activity
     * @param mDate
     * @return 返回String类型的时间
     */
    public static String dateToChinese(Context activity, Date mDate) {
        String date;
        //自动根据系统时间格式显示
        if (DateFormat.is24HourFormat(activity)) {
            date = DateFormat.format("yyyy年MM月dd日 EEEE", mDate).toString();
        } else {
            date = DateFormat.format("yyyy年MM月dd日 EEEE", mDate).toString();
        }
        return date;
    }

    public static String timeToChinese(Context activity, Date mDate) {
        String date;

        if (DateFormat.is24HourFormat(activity)) {
            date = DateFormat.format("kk:mm", mDate).toString();
        } else {
            date = DateFormat.format("hh:mm", mDate).toString();
        }
        return date;
    }

    public static String allToChinese(Context activity, Date mDate) {
        String date;

        if (DateFormat.is24HourFormat(activity)) {
            date = DateFormat.format("yyyy年MM月dd日 EEEE kk:mm", mDate).toString();
        } else {
            date = DateFormat.format("yyyy年MM月dd日 EEEE hh:mm", mDate).toString();
        }
        return date;
    }
}