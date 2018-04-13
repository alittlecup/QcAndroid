package cn.qingchengfit.shop.vo;

import android.support.annotation.StringDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by huangbaole on 2018/1/19.
 */
@StringDef(value = { Channel.CARD, Channel.RMB }) @Retention(RetentionPolicy.SOURCE)
public @interface Channel {
  public static final String CARD = "CARD";
  public static final String RMB = "RMB";
}
