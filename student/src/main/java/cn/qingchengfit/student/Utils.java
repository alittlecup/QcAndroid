package cn.qingchengfit.student;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.util.Pair;
import cn.qingchengfit.student.bean.FollowUpDataStatistic;
import cn.qingchengfit.utils.DateUtils;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Utils {

  /**
   * 将折线图的原有数据转换为折线图需要的数据形式
   *
   * @param bean 原有数据集
   * @param offSetDay 偏移的天数
   * @param type 常用的三种类型
   */
  public static LineData transformBean2DataByType(FollowUpDataStatistic.NewCreateUsersBean bean,
      int offSetDay, int type) {
    switch (type) {
      case FollowUpStatus.NEW_CREATE_USERS:
        return transformBean2Data(bean.date_counts, offSetDay, Color.parseColor("#6eb8f1"),
            Color.parseColor("#aa6eb8f1"));
      case FollowUpStatus.NEW_FOLLOWING_USERS:
        return transformBean2Data(bean.date_counts, offSetDay, Color.parseColor("#f9944e"),
            Color.parseColor("#aaf9944e"));
      case FollowUpStatus.NEW_MEMBER_USERS:
        return transformBean2Data(bean.date_counts, offSetDay, Color.parseColor("#0db14b"),
            Color.parseColor("#aa0db14b"));
      case -1:
        return transformBean2Data(bean.date_counts, offSetDay, Color.parseColor("#FF8CB4B9"),
            Color.parseColor("#648CB4B9"));
    }
    return null;
  }

  /**
   * 将折线图的原有数据转换为折线图需要的数据形式
   *
   * @param date_counts 原有数据集
   * @param offSetDay 偏移的天数
   * @param lineColor 绘制线的颜色
   * @param fillColor 填充阴影颜色
   */
  public static LineData transformBean2Data(List<FollowUpDataStatistic.DateCountsBean> date_counts,
      int offSetDay, @ColorInt int lineColor, @ColorInt int fillColor) {
    List<Pair<String, Integer>> datas = new ArrayList<>();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar.add(Calendar.DATE, -offSetDay);
    A:
    for (int i = 0; i < offSetDay; i++) {
      calendar.add(Calendar.DATE, 1);
      String s = DateUtils.Date2YYYYMMDD(calendar.getTime());
      for (FollowUpDataStatistic.DateCountsBean dateCountsBean : date_counts) {
        if (s.equals(dateCountsBean.date)) {
          Integer integer = Integer.valueOf(dateCountsBean.count);
          datas.add(new Pair<>(s, integer));
          continue A;
        }
      }
      datas.add(new Pair<>(s, 0));
    }
    List<Pair<String, Integer>> datasExpand = new ArrayList<>();
    datasExpand.add(new Pair<>(datas.get(0).first, 0));
    datasExpand.add(new Pair<>(datas.get(0).first, 0));
    datasExpand.add(new Pair<>(datas.get(0).first, 0));
    datasExpand.addAll(datas);
    datasExpand.add(new Pair<>(datas.get(datas.size() - 1).first, 0));
    datasExpand.add(new Pair<>(datas.get(datas.size() - 1).first, 0));
    datasExpand.add(new Pair<>(datas.get(datas.size() - 1).first, 0));

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
  @Retention(RetentionPolicy.SOURCE)
  public @interface FollowUpStatus {
    int NEW_CREATE_USERS = 0;
    int NEW_FOLLOWING_USERS = 1;
    int NEW_MEMBER_USERS = 2;
  }
}
