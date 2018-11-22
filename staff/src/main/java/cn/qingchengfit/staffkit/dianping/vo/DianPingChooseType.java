package cn.qingchengfit.staffkit.dianping.vo;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @IntDef({ALIGN_BOTTOM, ALIGN_BASELINE, ALIGN_CENTER, ALIGN_TOP})
 * @Retention(RetentionPolicy.SOURCE) public @interface Align {
 * }
 */
@IntDef({ DianPingChooseType.CHOOSE_FACILITY, DianPingChooseType.CHOOSE_TAGS })
@Retention(RetentionPolicy.SOURCE) public @interface DianPingChooseType {
  int CHOOSE_FACILITY = 0;
  int CHOOSE_TAGS = 1;
}
