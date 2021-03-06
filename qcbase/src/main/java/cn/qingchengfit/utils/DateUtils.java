package cn.qingchengfit.utils;

import android.support.annotation.IntRange;
import android.text.TextUtils;
import android.util.Pair;
import com.bigkoo.pickerview.TimePopupWindow;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/9/16 2015.
 */
public class DateUtils {
  public static final Long MONTH_TIME = 31 * 24 * 60 * 60 * 1000l;
  public static final Long DAY_TIME = 24 * 60 * 60 * 1000L;
  public static final Long HOUR_TIME = 60 * 60 * 1000L;
  public static final Long MINITE_TIME = 60 * 1000L;
  public static final Long SECOND_TIME = 1000L;
  public static final String[] weeks = {"周一","周二","周三","周四","周五","周六","周日"};
  public static final String[] weeksSimple = {"一","二","三","四","五","六","日"};

  public static Date formatDateFromServer(String s) {
    try {
      if (s.contains("T")) s = s.replace("T", " ");
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
      Date date = null;
      date = formatter.parse(s);
      return date;
    } catch (ParseException e) {
      return new Date();
    } catch (Exception e) {
      return new Date();
    }
  }

  public static String getChineseWeekStr( int x){
    if (x <0 || x > 6){
      return "";
    }else return weeks[x];
  }

  public static String getDuring(Date start, Date end) {
    return Date2YYYYMMDD(start) + "至" + Date2YYYYMMDD(end);
  }

  public static String getDuringFromServer(String start, String end) {
    if (TextUtils.isEmpty(start) || TextUtils.isEmpty(end)) {
      LogUtil.e("getDuringFromServer: server date string error");
      return "";
    }
    return start.substring(0, 10) + "至" + end.substring(0, 10);
  }
 public static String getHHMMDuringFromServer(String start, String end ,boolean cross) {
    if (TextUtils.isEmpty(start) || TextUtils.isEmpty(end)) {
      LogUtil.e("getDuringFromServer: server date string error");
      return "";
    }
    return start.substring(11, 16) + "至" +(cross?" 次日":"")+ end.substring(11, 16);
  }

  public static String getYYMMfromServer(String s) {
    if (s.startsWith("3000")) {
      return "至今";
    } else {
      return s.substring(0, 7);
    }
  }

  public static String getYYYYMMDDfromServer(String s) {
    try {
      if (s.contains("T")) {
        return DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(s));
      } else {
        return s;
      }
    } catch (Exception e) {
      return "错误日期";
    }
  }

  public static boolean isOutOfDate(Date date) {
    return date.getTime() < getDayMidnight(new Date());
  }

  public static String DateToServer(Date date) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    return formatter.format(date);
  }

  /**
   * 获取3小时前的日期时间
   * 返回格式 2016-09-09T10:00:00
   * 签到列表参数用
   *
   * @return 2016-09-09T10:00:00
   */
  public static String date2YYMMDDTHHMMSS(Date date) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    String dateStr = formatter.format(date);
    String[] dateArr = dateStr.split(" ");
    StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(dateArr[0]).append("T").append(dateArr[1]);
    return stringBuffer.toString();
  }

  public static String date2YYMM(Date date) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
    return formatter.format(date);
  }

  public static Date YYMM2date(String s) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
    Date date = null;
    try {
      date = formatter.parse(s);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return date;
  }

 public static Date HHMM2date(String s) {
    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.CHINA);
    Date date = new Date();
    try {
      date = formatter.parse(s);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return date;
  }

  public static String getChineseMonth(Date date) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月", Locale.CHINA);
    return formatter.format(date);
  }

  public static String formatToMMFromServer(String s) {
    try {
      s = s.replace("T", " ");
      return s.substring(0, s.length());
    } catch (Exception e) {
      return "";
    }
  }

  public static String replaceTFromServer(String s) {
    return s.replace("T", " ");
  }

  public static Date formatDateFromYYYYMMDD(String s) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    Date date = null;
    try {
      date = formatter.parse(s);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return date;
  }

  public static Date getDateFromHHmmss(String s) {
    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
    Date date = null;
    try {
      date = formatter.parse(s);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return date;
  }

  public static Date getDateFromHHmm(String s) {
    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.CHINA);
    Date date = null;
    try {
      date = formatter.parse(s);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return date;
  }

  //    public static String getDateDay(Date d) {
  //
  //        SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd", Locale.CHINA);
  //        return formatter.format(d);
  //    }

  public static String Date2YYYYMMDD(Date d) {

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    return formatter.format(d);
  }

  //    public static String getServerDateDayAddMonth(Date d, int i) {
  //
  //        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
  //        return formatter.format(addDateMonth(d, i));
  //    }

  public static String Date2YYYYMMDDHHmm(Date d) {

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
    return formatter.format(d);
  }

  public static String Date2YYYYMMDDHHmmss(Date d) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    return formatter.format(d);
  }

  public static String Date2MMDDHHmm(Date d) {

    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA);
    return formatter.format(d);
  }

  public static String Date2MMDD(Date d) {

    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd", Locale.CHINA);
    return formatter.format(d);
  }
  public static String Date2HHmm(Date d) {

    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.CHINA);
    return formatter.format(d);
  }

  public static String Date2YYYYMM(Date d) {

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
    return formatter.format(d);
  }

  public static String getTimeHHMM(Date d) {
    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.CHINA);
    return formatter.format(d);
  }

  public static String formatToServer(Date d) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
    return formatter.format(d) + "T" + formatter2.format(d);
  }

  public static Date formatDateHHMM(String s) {
    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.CHINA);
    Date date = null;
    try {
      date = formatter.parse(s);
    } catch (ParseException e) {
    }
    return date;
  }

  public static String formatDateToServer(String s) {
    return s.replace(".", "-");
  }

  public static String YYMMToServer(String s) {
    return s + "-01";
  }

  public static Date addDateMonth(Date d, int i) {
    Calendar c = Calendar.getInstance();
    c.setTime(d);
    c.add(Calendar.MONTH, i);
    return c.getTime();
  }

  public static Date addDay(Date d, int i) {
    Calendar c = Calendar.getInstance();
    c.setTime(d);
    c.add(Calendar.DATE, i);
    return c.getTime();
  }
  public static Date add(Date d, int i,int type) {
    Calendar c = Calendar.getInstance();
    c.setTime(d);
    c.add(type, i);
    return c.getTime();
  }

  public static int getYear(){
    Calendar c = Calendar.getInstance();
    return c.get(Calendar.YEAR);
  }

  /**
   * 以YYMMDD的格式增加天数
   */
  public static String addDay(String d, int i) {
    try {
      Calendar c = Calendar.getInstance();
      c.setTime(formatDateFromYYYYMMDD(d));
      c.add(Calendar.DATE, i);
      return Date2YYYYMMDD(c.getTime());
    }catch (Exception e){
      return "";
    }
  }

  /**
   * 得到本周周一
   *
   * @return yyyy-MM-dd
   */
  public static String getMondayOfThisWeek(Date date) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
    if (day_of_week == 0) day_of_week = 7;
    c.add(Calendar.DATE, -day_of_week + 1);
    return Date2YYYYMMDD(c.getTime());
  }

  /**
   * 得到本周周日
   *
   * @return yyyy-MM-dd
   */
  public static String getSundayOfThisWeek(Date date) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
    if (day_of_week == 0) day_of_week = 7;
    c.add(Calendar.DATE, -day_of_week + 7);
    return Date2YYYYMMDD(c.getTime());
  }

  public static String getEndDayOfMonth(Date date) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.add(Calendar.MONTH, 1);
    c.set(Calendar.DAY_OF_MONTH, 0);
    return Date2YYYYMMDD(c.getTime());
  }

  public static String getEndDayOfMonthNew(Date date) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
    return Date2YYYYMMDD(c.getTime());
  }

  public static String getEndDayOfMonthNew(int year, int month) {
    Calendar c = Calendar.getInstance();
    c.set(Calendar.YEAR, year);
    c.set(Calendar.MONTH, month+1);
    c.set(Calendar.DAY_OF_MONTH, 0);

    return Date2YYYYMMDD(c.getTime());
  }

  public static String getStartDayOfMonth(int year, int month) {
    Calendar c = Calendar.getInstance();
    c.set(Calendar.YEAR, year);
    c.set(Calendar.MONTH, month);
    c.set(Calendar.DAY_OF_MONTH, 1);
    return Date2YYYYMMDD(c.getTime());
  }

  public static String getStartDayOfMonth(Date date) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.set(Calendar.DAY_OF_MONTH, 1);
    return Date2YYYYMMDD(c.getTime());
  }

  public static int interval(String start, String end) {
    if (start.isEmpty() || end.isEmpty()){
      return 0;
    }
    Date s = formatDateFromYYYYMMDD(start);
    Date e = formatDateFromYYYYMMDD(end);
    return (int) ((e.getTime() - s.getTime()) / DAY_TIME);
  }

  public static int interval(Date start, Date end) {
    return (int) ((end.getTime() - start.getTime()) / DAY_TIME);
  }

  @IntRange(from = 0, to = 6) public static int getDayOfWeek(Date date) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    int i = c.get(Calendar.DAY_OF_WEEK);
    if (i == 1) {
      return 6;
    } else {
      return i - 2;
    }
  }
  public static boolean isOverCurrent(Date date){
    return date.getTime()>new Date().getTime();
  }

  public static long getToadayMidnight() {
    Calendar c = Calendar.getInstance();
    c.setTime(new Date());
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    c.set(Calendar.MILLISECOND, 0);
    return c.getTime().getTime();
  }

  public static long getDayMidnight(Date d) {
    Calendar c = Calendar.getInstance();
    c.setTime(d);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    c.set(Calendar.MILLISECOND, 0);
    return c.getTimeInMillis();
  }

  public static long getDayMid(Date d) {
    Calendar c = Calendar.getInstance();
    c.setTime(d);
    c.set(Calendar.HOUR_OF_DAY, 12);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);
    c.set(Calendar.MILLISECOND, 0);
    return c.getTimeInMillis();
  }

  public static String minusDay(Date d, int day) {
    Calendar c = Calendar.getInstance();
    c.setTime(d);
    c.add(Calendar.DATE, -day);
    return Date2YYYYMMDD(c.getTime());
  }

  public static Date addHour(Date d, int hour) {
    Calendar c = Calendar.getInstance();
    c.setTime(d);
    c.add(Calendar.HOUR, -hour);
    return c.getTime();
  }

  public static int getYear(Date d) {
    Calendar c = Calendar.getInstance();
    c.setTime(d);
    return c.get(Calendar.YEAR);
  }

  /**
   * 记住要+1
   *
   * @param d 记住要+1
   */
  public static int getMonth(Date d) {
    Calendar c = Calendar.getInstance();
    c.setTime(d);
    return c.get(Calendar.MONTH);
  }

  /**
   * 得到一周的开始时间和结束时间
   */
  public static Pair<String, String> getWeek(int count) {
    Calendar s = Calendar.getInstance();
    s.add(Calendar.DATE, count * 7);
    s.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
    String start = Date2YYYYMMDD(s.getTime());
    s.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
    String end = Date2YYYYMMDD(s.getTime());
    return new Pair<>(start, end);
  }

  public static int dayNumFromToday(Date d) {
    return (int) ((getDayMid(d) - getDayMid(new Date())) / DAY_TIME);
  }

  public static boolean AlessB(String YYYYMMDDa, String YYYYMMDDb) {
    return formatDateFromYYYYMMDD(YYYYMMDDa).getTime() < formatDateFromYYYYMMDD(
        YYYYMMDDb).getTime();
  }

  public static boolean AlessOrEquelB(String YYYYMMDDa, String YYYYMMDDb) {
    return formatDateFromYYYYMMDD(YYYYMMDDa).getTime() <= formatDateFromYYYYMMDD(
        YYYYMMDDb).getTime();
  }
  public static boolean AlessTimeB(String HHmma, String HHmmb) {
    return formatDateHHMM(HHmma).getTime() < formatDateHHMM(
        HHmmb).getTime();
  }


  public static Date formatDatefromMMDD(String s) {
    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd", Locale.CHINA);
    Date date = null;
    try {
      date = formatter.parse(s);
    } catch (ParseException e) {
    }
    return date;
  }

  public static String Date2YYYYMMDDV2(Date d) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
    return formatter.format(d);
  }
  public static String Date2MMDDV2(Date d) {
    SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日", Locale.CHINA);
    return formatter.format(d);
  }

  public static int getDayOfMonth(Date d) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(d);
    return calendar.get(Calendar.DAY_OF_MONTH);
  }

  public static String getStringToday() {
    return DateUtils.Date2YYYYMMDD(new Date());
  }

  public static String getNotifacationTimeStr(Date d) {
    long intelnal = new Date().getTime() - d.getTime();
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    long lastDayInternal = d.getTime() - calendar.getTime().getTime();
    if (lastDayInternal < 4 * HOUR_TIME) {
      lastDayInternal = 4 * HOUR_TIME;
    }
    if (intelnal < MINITE_TIME) {
      return "刚刚";
    } else if (intelnal < HOUR_TIME) {
      return intelnal / MINITE_TIME + "分钟前";
    } else if (intelnal < HOUR_TIME * 4) {
      return intelnal / HOUR_TIME + "小时前";
    } else if (intelnal < lastDayInternal) {
      return "今天" + getTimeHHMM(d);
    } else if (intelnal < lastDayInternal + DAY_TIME) {
      return "昨天";
    } else {
      return Date2MMDD(d);
    }
  }

  public static int getAge(Date d) {
    Calendar c = Calendar.getInstance();
    c.setTime(new Date());
    int curYear = c.get(Calendar.YEAR);
    c.setTime(d);
    return curYear - c.get(Calendar.YEAR);
  }

  public static String date2TimePicker(Date d,TimePopupWindow.Type type){
    switch (type){
      case ALL:
        return date2YYMMDDTHHMMSS(d);
      case YEAR_MONTH:
        return date2YYMM(d);
      case YEAR_MONTH_DAY:
        return Date2YYYYMMDD(d);
      case MONTH_DAY:
        return Date2MMDD(d);
      case HOURS_MINS:
        return Date2HHmm(d);
      default:
        return Date2MMDDHHmm(d);
    }
  }

  public static String getFileNameFormServer(String str) {
    return str.replace("T", " ");
  }


  public static String changeDate(String curDate,int count,int type){
    Calendar c = Calendar.getInstance();
    c.setTime(formatDateFromYYYYMMDD(curDate));
    c.add(type,count);
    return Date2YYYYMMDD(c.getTime());
  }
}
