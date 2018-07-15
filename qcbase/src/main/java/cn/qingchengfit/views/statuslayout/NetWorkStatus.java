package cn.qingchengfit.views.statuslayout;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by huangbaole on 2018/3/29.
 */
@IntDef(value = {
    NetWorkStatus.FIVE_ZERO_ZERO, NetWorkStatus.FOUR_ZERO_FOUR, NetWorkStatus.FOUR_ZERO_THREE,
    NetWorkStatus.NETWORK_ERROR, NetWorkStatus.NETWORK_NOT_CONNECT, NetWorkStatus.NETWORK_OVER_TIME
}) @Retention(RetentionPolicy.SOURCE)

public @interface NetWorkStatus {
  int FIVE_ZERO_ZERO = 500;
  int FOUR_ZERO_FOUR = 404;
  int FOUR_ZERO_THREE = 403;
  int NETWORK_OVER_TIME = 602;
  int NETWORK_ERROR = 601;
  int NETWORK_NOT_CONNECT = 600;
}
