package com.example.text.utils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    private static ZoneId ZONE;
    private static Locale LOCALE; // 若需强制中文用 Locale.CHINA

    private static DateTimeFormatter TIME_FORMAT;
    private static DateTimeFormatter WEEKDAY_FORMAT;
    private static DateTimeFormatter DATE_FORMAT;

    public DateUtils(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            ZONE = ZoneId.systemDefault();
            LOCALE = Locale.CHINA;
            TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm", LOCALE);
            WEEKDAY_FORMAT = DateTimeFormatter.ofPattern("EEEE", LOCALE);
            DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yy/MM/dd", LOCALE);
        }
    }

    /**
     * 格式化聊天时间戳：
     * - 今天：HH:mm
     * - 昨天：昨天HH:mm
     * - 过去 1~6 天内：星期几
     * - 超过 7 天：yy/MM/dd
     */
    public static String formatDate(long timestamp) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime time = Instant.ofEpochMilli(timestamp).atZone(ZONE).toLocalDateTime();
            LocalDate today = LocalDate.now(ZONE);
            LocalDate msgDate = time.toLocalDate();
            long deltaDays = ChronoUnit.DAYS.between(msgDate, today);
            if (deltaDays == 0) {
                // 今天
                return time.format(TIME_FORMAT);
            } else if (deltaDays == 1) {
                // 昨天
                return "昨天" + time.format(TIME_FORMAT);
            } else if (deltaDays > 1 && deltaDays < 7) {
                // 过去一周内，显示星期几（如“星期一”）
                return time.format(WEEKDAY_FORMAT);
            } else {
                // 超过 7 天，显示日期 yy/MM/dd
                return time.format(DATE_FORMAT);
            }
        }else{
            Calendar now = Calendar.getInstance();
            Calendar target = Calendar.getInstance();
            target.setTimeInMillis(timestamp);

            // 获取清零时间的两个日历（用于计算纯日期差）
            Calendar nowZero = (Calendar) now.clone();
            Calendar targetZero = (Calendar) target.clone();
            clearTime(nowZero);
            clearTime(targetZero);

            long diffMillis = nowZero.getTimeInMillis() - targetZero.getTimeInMillis();
            int daysDiff = (int) (diffMillis / (24 * 60 * 60 * 1000));

            if (daysDiff == 0) {
                // 今天
                return formatTime(target.getTime());
            } else if (daysDiff == 1) {
                // 昨天
                return "昨天 " + formatTime(target.getTime());
            } else if (daysDiff > 1 && daysDiff < 7) {
                // 1~6 天前（显示星期）
                return formatWeek(target.getTime());
            } else {
                // 7 天前（显示日期）
                return formatFullDate(target.getTime());
            }
        }
    }

    // 清除时分秒毫秒（用于纯日期比较）
    private static void clearTime(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }

    // 格式：15:30
    private static String formatTime(Date date) {
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date);
    }

    // 格式：星期一（中文系统）
    private static String formatWeek(Date date) {
        return new SimpleDateFormat("EEEE", Locale.getDefault()).format(date);
    }

    // 格式：23/06/01
    private static String formatFullDate(Date date) {
        return new SimpleDateFormat("yy/MM/dd", Locale.getDefault()).format(date);
    }
}

