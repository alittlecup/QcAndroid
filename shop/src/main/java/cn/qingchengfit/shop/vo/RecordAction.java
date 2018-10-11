package cn.qingchengfit.shop.vo;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by huangbaole on 2018/1/22.
 */
@IntDef(value = { RecordAction.ADD, RecordAction.REDUCE, RecordAction.RETURN, RecordAction.SALED })
@Retention(RetentionPolicy.SOURCE)
public @interface RecordAction {
  int ADD = 1;
  int REDUCE = 2;
  int SALED = 3;
  int RETURN = 4;
}
