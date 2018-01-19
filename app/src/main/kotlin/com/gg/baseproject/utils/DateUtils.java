package com.gg.baseproject.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Creator : GG
 * Time    : 2017/11/22
 * Mail    : gg.jin.yu@gmail.com
 * Explain :
 */

public class DateUtils {
    private static SimpleDateFormat allDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat longDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static SimpleDateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat shortDateFormat = new SimpleDateFormat("MM.dd");

    /**
     * 获取现在时间
     *
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDate() {
        Date currentTime = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 获取不含毫秒的当前时间
     *
     * @return返回字符串格式 yyyy-MM-dd HH:mm
     */
    public static String getNowDate() {
        return getStringDate().substring(0, 16);
    }


    public static String getTimeAfterNow(int i) {
        String now = getNowDate();
        if (Integer.parseInt(now.split(":")[1]) < 15) {
            return longDateFormat.format(new Date(str2LongWHour(now.split(":")[0] + ":00") + i * 60 * 60 * 1000));
        } else if (Integer.parseInt(now.split(":")[1]) < 30) {
            return longDateFormat.format(new Date(str2LongWHour(now.split(":")[0] + ":15") + i * 60 * 60 * 1000));

        } else if (Integer.parseInt(now.split(":")[1]) < 45) {
            return longDateFormat.format(new Date(str2LongWHour(now.split(":")[0] + ":30") + i * 60 * 60 * 1000));

        } else if (Integer.parseInt(now.split(":")[1]) < 60) {
            return longDateFormat.format(new Date(str2LongWHour(now.split(":")[0] + ":45") + i * 60 * 60 * 1000));
        } else {
            return null;
        }
    }

    /**
     * 获取不含毫秒和分钟的当前时间
     *
     * @return返回字符串格式 yyyy-MM-dd HH:m0
     */
    public static String getNowHour() {
        return getStringDate().substring(0, 15) + "0";
    }

    /**
     * 获取传入时间的 小时和分钟
     */
    public static String getTimeHour(String date) {
        return date.substring(11, 16);
    }

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd
     */
    public static Date strToDate(String strDate) {
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = defaultDateFormat.parse(strDate, pos);
        return strtodate;
    }

    /**
     * 根据一个日期，返回是星期几的字符串
     */
    public static int getWeek(String sdate) {
        // 再转换为时间
        Date date = strToDate(sdate);
        return getWeek(date);
    }

    /**
     * 根据一个日期，返回是星期几的字符串
     */
    public static int getWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int week = c.get(Calendar.DAY_OF_WEEK);
        // hour中存的就是星期几了，其范围 1~7
        // 1=星期日 7=星期六，其他类推
        return week;
    }

    /**
     * 将传入的年月日 转为"yyyy-MM-dd"格式的date时间
     */
    @SuppressLint("SimpleDateFormat")
    public static Date getDate(String year, String month, String day) {
        String result = year + "-"
                + (month.length() == 1 ? ("0" + month) : month) + "-"
                + (day.length() == 1 ? ("0" + day) : day);
        try {
            return defaultDateFormat.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将str类型的时间转为long类型
     */
    public static long str2LongWHS(String time) {
        try {
            Date date = allDateFormat.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 将str类型的时间转为long类型
     */
    public static long str2LongWHour(String time) {
        try {
            Date date = longDateFormat.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 比较两个时间的大小  正数 前者大 负数 后者大
     */

    public static int compareTime(String d1, String d2) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");//创建日期转换对象HH:mm:ss为时分秒，年月日为yyyy-MM-dd
        try {
            Date dt1 = df.parse(d1);//将字符串转换为date类型
            Date dt2 = df.parse(d2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 将传入时间加上 i 个小时返回
     */
    public static String start2End(String startTime, int i) {
        startTime = startTime + ":00";
        long startLong = str2LongWHS(startTime);
        long endLong = startLong + 6 * 10 * 60 * 1000 * i;
        Date endDate = new Date(endLong);
        return allDateFormat.format(endDate).substring(0, 16);
    }

    /**
     * 获取"yyyy-MM-dd"格式的date时间
     */
    public static Date str2Date(String time) {
        try {
            Date date = defaultDateFormat.parse(time);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取"yyyy-MM-dd HH:mm:ss"格式的date时间
     */
    public static Date str2LongDate(String time) {
        try {
            Date date = allDateFormat.parse(time);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date str2ShortDate(String time) {
        try {
            Date date = shortDateFormat.parse(time);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * date转str  "yyyy-MM-dd"格式
     */
    public static String date2Str(Date date) {
        return defaultDateFormat.format(date);
    }

    /**
     * date转str  "yyyy-MM-dd HH:mm:ss"格式
     */
    public static String date2AllStr(Date date) {
        return allDateFormat.format(date);
    }

    /**
     * date转str  "MM.dd"格式
     */
    public static String date2ShortStr(Date date) {
        return shortDateFormat.format(date);
    }

    /**
     * 将str类型的时间传入 转为"yyyy-MM-dd"  再返回long类型
     */
    public static long str2Long(String time) {
        try {
            Date date = defaultDateFormat.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取str类型的"yyyy-MM-dd"格式的当前时间
     */
    public static String getCurrentDateStr() {
        Date dt = new Date();
        String date = defaultDateFormat.format(dt);
        return date;
    }

    /**
     * 获取传入时间的年份
     */
    public static int getYear(Date date) {
        return date.getYear() + 1900;
    }

    /**
     * 获取传入时间的月份
     */
    public static int getMonth(Date date) {
        return date.getMonth() + 1;
    }

    /**
     * 得到二个日期间的间隔天数
     */
    public static String getTwoDay(String sj1, String sj2) {
        long day = 0;
        try {
            Date date = defaultDateFormat.parse(sj1);
            Date mydate = defaultDateFormat.parse(sj2);
            day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            return "";
        }
        return day + "";
    }

    /**
     * type 控制是否判断传入时间是否是今天
     */
    public static String getWeekStr(int type, String sdate) {
        if (type == 1 && getTwoDay(getStringDate(), sdate).equals("0"))
            return "今天";
        int week = getWeek(sdate);
        if (week == 1)
            return "周日";
        if (week == 2)
            return "周一";
        if (week == 3)
            return "周二";
        if (week == 4)
            return "周三";
        if (week == 5)
            return "周四";
        if (week == 6)
            return "周五";
        if (week == 7)
            return "周六";

        return "";
    }

    public static String getWeekStr(int type, Calendar calendar) {
        Calendar c = Calendar.getInstance();
        if (type == 1 && c.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
                && c.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
                && c.get(Calendar.DATE) == calendar.get(Calendar.DATE))
            return "今天";
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        if (week == 1)
            return "日";
        if (week == 2)
            return "一";
        if (week == 3)
            return "二";
        if (week == 4)
            return "三";
        if (week == 5)
            return "四";
        if (week == 6)
            return "五";
        if (week == 7)
            return "六";

        return "";
    }

    /***************************************************************************
     * //nd=1表示返回的值中包含年度 //yf=1表示返回的值中包含月份 //rq=1表示返回的值中包含日期 //format表示返回的格式 1 以年月日中文返回 2 以横线-返回 //
     * 3 以斜线/返回 4 以缩写不带其它符号形式返回 // 5 以点号.返回
     **************************************************************************/
    public static String getStringDateMonth(String sdate, int nd, int yf, int rq, int format) {
        Date currentTime = strToDate(sdate);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        String s_nd = dateString.substring(0, 4); // 年份
        String s_yf = dateString.substring(5, 7); // 月份
        String s_rq = dateString.substring(8, 10); // 日期
        String sreturn = "";
        if (nd == 1) {
            sreturn = s_nd;
            // 处理间隔符
            if (format == 1)
                sreturn = sreturn + "年";
            else if (format == 2)
                sreturn = sreturn + "-";
            else if (format == 3)
                sreturn = sreturn + "/";
            else if (format == 5)
                sreturn = sreturn + ".";
        }
        // 处理月份
        if (yf == 1) {
            int i_yf = Integer.parseInt(s_yf);
            sreturn = sreturn + i_yf;
            if (format == 1)
                sreturn = sreturn + "月";
            else if (format == 2)
                sreturn = sreturn + "-";
            else if (format == 3)
                sreturn = sreturn + "/";
            else if (format == 5)
                sreturn = sreturn + ".";
        }
        // 处理日期
        if (rq == 1) {
            int i_rq = Integer.parseInt(s_rq);
            sreturn = sreturn + i_rq;
            if (format == 1)
                sreturn = sreturn + "日";
        }
        return sreturn;
    }

    /**
     * 把时间格式化为 多久以前
     */

    public static String convertTimestamp(String s) {
        if (TextUtils.isEmpty(s))
            return "";
        Date date = str2LongDate(s);
        return convertTimestamp(date);
    }

    /**
     * 把时间格式化为 多久以前
     */

    public static String convertTimestamp(long l) {
        if (l == 0)
            return "";
        Date date = new Date(l);
        return convertTimestamp(date);
    }

    /**
     * 把时间格式化为 多久以前
     */

    public static String convertTimestamp(Date date) {
        if (date == null)
            return "";
//        try {
//            Date date = f.parse(time);
//            Date now = new Date();
        long timelong = date.getTime();
        long nowlong = System.currentTimeMillis();

        String oldTimeDate = (date.getMonth() + 1) + "月" + date.getDate() + "日";

        long diff = -getNowDaysDiff(date2AllStr(date));
        if (diff == 0) { // 今天
            long timeInterval = (nowlong - timelong) / 1000 / 60; // 分
            if (timeInterval <= 0) { // 1分钟内
                return "刚刚";
            }
            if (timeInterval < 60) { // 1小时内
                return timeInterval + "分钟前";
            }
            timeInterval = timeInterval / 60; // 小时
            if (timeInterval <= 3) { // 3小时内
                return timeInterval + "小时前";
            }
            return "今天 " + allDateFormat.format(date).substring(11, 16);
        } else if (diff == 1) { // 昨天
            return "昨天 " + allDateFormat.format(date).substring(11, 16);
        } else if (diff <= 3 && diff > 0) {// 2/3天前
            return diff + " 天前";
        } else if (diff == -1) {
            long timeInterval = (nowlong - timelong) / 1000 / 60; // 分
            if (timeInterval <= 0) { // 1分钟内
                return "刚刚";
            }
            if (timeInterval < 60) { // 1小时内
                return -timeInterval + "分钟后";
            }
            timeInterval = timeInterval / 60; // 小时
            if (timeInterval <= 3) { // 3小时内
                return -timeInterval + "小时后";
            }
            return "今天 " + allDateFormat.format(date).substring(11, 16);
        } else if (diff >= -3 && diff < 0) {
            return -diff + " 天后";
        } else
            return oldTimeDate; // 更早
    }

    //time - 当前天 >0 说明未出发，否则是以前
    public static long getNowDaysDiff(String time) {
        try {
            Date d1 = defaultDateFormat.parse(time);
            Date d2 = new Date();
            String nowDay = defaultDateFormat.format(d2);
            d2 = defaultDateFormat.parse(nowDay);
            long diff = d1.getTime() - d2.getTime();
            long days = diff / (1000 * 60 * 60 * 24);
            return days;
        } catch (Exception ignored) {
        }
        return 0;
    }


    /**
     * 获取任意时间的下一个月
     * 描述:<描述函数实现的功能>.
     *
     * @param repeatDate
     * @return
     */
    public static String getPreMonth(String repeatDate) {
        String lastMonth = "";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM");
        int year = Integer.parseInt(repeatDate.substring(0, 4));
        String monthsString = repeatDate.substring(4, 6);
        int month;
        if ("0".equals(monthsString.substring(0, 1))) {
            month = Integer.parseInt(monthsString.substring(1, 2));
        } else {
            month = Integer.parseInt(monthsString.substring(0, 2));
        }
        cal.set(year, month, 1);
        cal.add(Calendar.DATE, 1);
        lastMonth = dft.format(cal.getTime());
        return lastMonth;
    }


    /**
     * @param str
     * @return
     */
    public static String getNextDay(String str) {
        Date today = str2Date(str);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return defaultDateFormat.format(calendar.getTime());
    }

    public static String getNextMonth(String str) {
        Date today = str2Date(str);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.MONTH, 1);
        return defaultDateFormat.format(calendar.getTime());
    }

}
