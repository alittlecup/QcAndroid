package cn.qingchengfit.shop.vo;

import android.support.annotation.StringDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by huangbaole on 2018/1/19.
 */
@StringDef(value = {}) @Retention(RetentionPolicy.SOURCE) public @interface ShopOrderBy {
  /**
   * priority,inventory,total_sales,created_at
   */
  String PRRORITY_UP = "-priority";
  String PRRORITY_DOWN = "priority";
  String INVENTPORY_UP = "-inventory";
  String INVENTPORY_DOWN = "inventory";
  String TOTAL_SALES_UP = "-total_sales";
  String TOTAL_SALES_DOWN = "total_sales";
  String CREATED_AT_UP = "-created_at";
  String CREATED_AT_DOWN = "created_at";
}
