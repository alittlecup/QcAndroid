package cn.qingchengfit.student.listener;

import android.support.annotation.StringDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef(value = { DateGroupDimension.DAY, DateGroupDimension.WEEK, DateGroupDimension.MONTH })
@Retention(RetentionPolicy.RUNTIME) public @interface DateGroupDimension {
  String WEEK = "week";
  String DAY = "day";
  String MONTH = "month";
}
