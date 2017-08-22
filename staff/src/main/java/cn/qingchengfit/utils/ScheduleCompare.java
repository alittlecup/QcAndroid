package cn.qingchengfit.utils;

import cn.qingchengfit.staffkit.usecase.bean.ScheduleBean;
import java.util.Comparator;

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
 * Created by Paper on 15/10/12 2015.
 */
public class ScheduleCompare implements Comparator<ScheduleBean> {

    @Override public int compare(ScheduleBean lhs, ScheduleBean rhs) {
        if (lhs.time > rhs.time) {
            return 1;
        } else {
            return -1;
        }
    }
}
