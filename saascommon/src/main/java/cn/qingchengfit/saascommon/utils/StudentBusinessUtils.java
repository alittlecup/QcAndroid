package cn.qingchengfit.saascommon.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Pair;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saascommon.R;
import cn.qingchengfit.saascommon.widget.CircleView;
import cn.qingchengfit.utils.DateUtils;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/8/11.
 */

public class StudentBusinessUtils {
  public static Drawable getStudentStatusDrawable(Context context, int x) {
    Drawable drawable;
    switch (x) {
      case 1:
        drawable = new CircleView(ContextCompat.getColor(context, R.color.qc_student_status_1));
        break;
      case 2:
        drawable = new CircleView(ContextCompat.getColor(context, R.color.qc_student_status_2));

        break;
      default:
        drawable = new CircleView(ContextCompat.getColor(context, R.color.qc_student_status_0));
        break;
    }
    drawable.setBounds(0, 0, 26, 26);
    return drawable;
  }

  public static <T extends Staff> String getIdStrFromStaffs(List<T> staffs) {
    if (staffs != null) {
      String ret = "";
      int i = 1;
      for (Staff staff : staffs) {
        ret = TextUtils.concat(ret, staff.getId()).toString();
        if (i < staffs.size()) ret = TextUtils.concat(ret, ",").toString();
        i++;
      }
      return ret;
    } else {
      return "";
    }
  }

  public static <T extends Staff> String getNamesStrFromStaffs(List<T> staffs) {
    if (staffs != null) {
      String ret = "";
      int i = 1;
      for (Staff staff : staffs) {
        ret = TextUtils.concat(ret, staff.username).toString();
        if (i < staffs.size()) ret = TextUtils.concat(ret, ",").toString();
        i++;
      }
      return ret;
    } else {
      return "";
    }
  }

  public static String getNumString(float fo) {
    try {
      boolean hasX = ((int) (fo * 100) % 100) != 0;

      if (hasX) {
        float f = fo;
        if (f / 10000f >= 1) {
          return String.format(Locale.CHINA, "%.1fW", f / 10000);
        } else {
          return String.format(Locale.CHINA, "%.2f", f);
        }
      } else {
        Integer i = (int) fo;
        if (i / 10000 > 0) {
          return String.format(Locale.CHINA, "%.1fW", ((float) i / 10000f));
        } else {
          return String.format(Locale.CHINA, "%d", i);
        }
      }
    } catch (Exception e) {

      return "";
    }
  }

  /**
   * 将折线图的原有数据转换为折线图需要的数据形式
   *
   * @param date_counts 原有数据集
   * @param offSetDay 偏移的天数
   * @param lineColor 绘制线的颜色
   * @param fillColor 填充阴影颜色
   */
  //public static LineData transformBean2Data(List<FollowUpDataStatistic.DateCountsBean> date_counts,
  //    int offSetDay, @ColorInt int lineColor, @ColorInt int fillColor) {
  //  List<Pair<String, Integer>> datas = new ArrayList<>();
  //  Calendar calendar = Calendar.getInstance();
  //  calendar.setTime(new Date());
  //  calendar.add(Calendar.DATE, -offSetDay);
  //  A:
  //  for (int i = 0; i < offSetDay; i++) {
  //    calendar.add(Calendar.DATE, 1);
  //    String s = DateUtils.Date2YYYYMMDD(calendar.getTime());
  //    for (FollowUpDataStatistic.DateCountsBean dateCountsBean : date_counts) {
  //      if (s.equals(dateCountsBean.date)) {
  //        Integer integer = Integer.valueOf(dateCountsBean.count);
  //        datas.add(new Pair<>(s, integer));
  //        continue A;
  //      }
  //    }
  //    datas.add(new Pair<>(s, 0));
  //  }
  //  List<Pair<String, Integer>> datasExpand = new ArrayList<>();
  //  datasExpand.add(new Pair<>(datas.get(0).first, 0));
  //  datasExpand.add(new Pair<>(datas.get(0).first, 0));
  //  datasExpand.add(new Pair<>(datas.get(0).first, 0));
  //  datasExpand.addAll(datas);
  //  datasExpand.add(new Pair<>(datas.get(datas.size() - 1).first, 0));
  //  datasExpand.add(new Pair<>(datas.get(datas.size() - 1).first, 0));
  //  datasExpand.add(new Pair<>(datas.get(datas.size() - 1).first, 0));
  //
  //  List<String> xValues = new ArrayList<>();
  //  for (Pair<String, Integer> pair : datasExpand) {
  //    xValues.add(pair.first);
  //  }
  //
  //  List<Entry> values = new ArrayList<>();
  //  List<Entry> values2 = new ArrayList<>();
  //  for (int i = 0; i < datas.size(); i++) {
  //    values.add(new Entry(i + 3, datas.get(i).second, datas.get(i).first));
  //  }
  //  for (int i = 0; i < datasExpand.size(); i++) {
  //    values2.add(new Entry(i, -1, datasExpand.get(i).first));
  //  }
  //  LineDataSet set1 = new LineDataSet(values, null);
  //
  //  LineDataSet set2 = new LineDataSet(values2, null);
  //
  //  set1.setColor(lineColor);
  //  set1.setCircleColor(lineColor);
  //  set1.setHighLightColor(lineColor);
  //  set1.setHighlightLineWidth(2);
  //  set1.setDrawHorizontalHighlightIndicator(false);
  //  set1.setDrawCircleHole(true);//
  //  set1.setLineWidth(2f);
  //  set1.setCircleRadius(4f);
  //  set1.setValueTextSize(9f);
  //  set1.setDrawValues(false);
  //  set1.setDrawFilled(true);
  //  set1.setFillColor(fillColor);
  //
  //  ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
  //  dataSets.add(set1); // add the datasets
  //  dataSets.add(set2); // add the datasets
  //
  //  return new LineData(dataSets);
  //}
  public static LineData transformBean2Data(List<Integer> date_counts, int offSetDay,
      @ColorInt int lineColor, @ColorInt int fillColor, boolean first) {
    offSetDay = date_counts.size();
    List<Pair<String, Integer>> datas = new ArrayList<>();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar.add(Calendar.DATE, -offSetDay);
    for (int i = 0; i < offSetDay; i++) {
      calendar.add(Calendar.DATE, 1);
      String s = DateUtils.Date2YYYYMMDD(calendar.getTime());
      datas.add(new Pair<>(s, date_counts.get(i)));
    }
    List<Pair<String, Integer>> datasExpand = new ArrayList<>();
    if (first) {
      datasExpand.add(new Pair<>(datas.get(datas.size() - 1).first, 0));
      datasExpand.add(new Pair<>(datas.get(datas.size() - 1).first, 0));
      datasExpand.add(new Pair<>(datas.get(datas.size() - 1).first, 0));
    }
    datasExpand.addAll(datas);


    List<String> xValues = new ArrayList<>();
    for (Pair<String, Integer> pair : datasExpand) {
      xValues.add(pair.first);
    }

    List<Entry> values = new ArrayList<>();
    List<Entry> values2 = new ArrayList<>();
    for (int i = 0; i < datas.size(); i++) {
      values.add(new Entry(i + 3, datas.get(i).second, datas.get(i).first));
    }
    for (int i = 0; i < datasExpand.size(); i++) {
      values2.add(new Entry(i, -1, datasExpand.get(i).first));
    }
    LineDataSet set1 = new LineDataSet(values, null);

    LineDataSet set2 = new LineDataSet(values2, null);

    set1.setColor(lineColor);
    set1.setCircleColor(lineColor);
    set1.setHighLightColor(lineColor);
    set1.setHighlightLineWidth(2);
    set1.setDrawHorizontalHighlightIndicator(false);
    set1.setDrawCircleHole(true);//
    set1.setLineWidth(2f);
    set1.setCircleRadius(4f);
    set1.setValueTextSize(9f);
    set1.setDrawValues(false);
    set1.setDrawFilled(true);
    set1.setFillColor(fillColor);

    ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
    dataSets.add(set1); // add the datasets
    dataSets.add(set2); // add the datasets

    return new LineData(dataSets);
  }

  /**
   * 将折线图的原有数据转换为折线图需要的数据形式
   *
   * @param bean 原有数据集
   * @param offSetDay 偏移的天数
   * @param type 常用的三种类型
   */
  //public static LineData transformBean2DataByType(FollowUpDataStatistic.NewCreateUsersBean bean,
  //    int offSetDay, int type) {
  //  switch (type) {
  //    case FollowUpHomeFragment.FollowUpStatus.NEW_CREATE_USERS:
  //      return transformBean2Data(bean.date_counts, offSetDay, Color.parseColor("#6eb8f1"),
  //          Color.parseColor("#aa6eb8f1"));
  //    case FollowUpHomeFragment.FollowUpStatus.NEW_FOLLOWING_USERS:
  //      return transformBean2Data(bean.date_counts, offSetDay, Color.parseColor("#f9944e"),
  //          Color.parseColor("#aaf9944e"));
  //    case FollowUpHomeFragment.FollowUpStatus.NEW_MEMBER_USERS:
  //      return transformBean2Data(bean.date_counts, offSetDay, Color.parseColor("#0db14b"),
  //          Color.parseColor("#aa0db14b"));
  //  }
  //  return null;
  //}
  //
  public static LineData transformBean2DataByType(List<Integer> bean, int offSetDay, String type,
      boolean first) {
    switch (type) {
      case "新注册":
        return transformBean2Data(bean, offSetDay, Color.parseColor("#6eb8f1"),
            Color.parseColor("#aa6eb8f1"), first);
      case "会员":
        return transformBean2Data(bean, offSetDay, Color.parseColor("#0db14b"),
            Color.parseColor("#aa0db14b"), first);
    }
    return null;
  }
}
