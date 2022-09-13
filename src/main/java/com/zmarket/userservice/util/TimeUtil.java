package com.zmarket.userservice.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
public class TimeUtil {
    public static String atStartOfWeek() {

        Calendar calender = Calendar.getInstance();

        calender.set(Calendar.DAY_OF_WEEK, calender.getFirstDayOfWeek());

        Date calenderToDate = calender.getTime();

        return atStartOfDay(calenderToDate);
    }



    public static String atStartOfDay(Date date) {

        LocalDateTime localDateTime = convertDateToLocalDateTime(date);

        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);

        return formatDate(startOfDay);
    }

    public static String atEndOfDay(Date date) {

        LocalDateTime localDateTime = convertDateToLocalDateTime(date);

        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);


        return formatDate(endOfDay);
    }

    public static String atStartOfTheMonth() {

        Calendar currentDate = Calendar.getInstance();

        currentDate.set(Calendar.DAY_OF_MONTH, 1);

        Date calenderToDate = currentDate.getTime();

        return atStartOfDay(calenderToDate);
    }

    private static LocalDateTime convertDateToLocalDateTime(Date date) {

        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static String formatDate(LocalDateTime date) {

        DateTimeFormatter dateTimeFormatter =   DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return date.format(dateTimeFormatter);

    }
}
