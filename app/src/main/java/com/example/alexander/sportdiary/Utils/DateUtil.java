package com.example.alexander.sportdiary.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    public static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }

    public static int differenceInDays(Date date1, Date date2) {
        long milliseconds = date2.getTime() - date1.getTime();
        return (int) (milliseconds / (24 * 60 * 60 * 1000));
    }
}
