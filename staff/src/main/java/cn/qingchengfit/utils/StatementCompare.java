package cn.qingchengfit.utils;

import cn.qingchengfit.model.responese.QcResponseStatementDetail;
import java.util.Comparator;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 15/10/12 2015.
 */
public class StatementCompare implements Comparator<QcResponseStatementDetail.StatamentSchedule> {

    @Override public int compare(QcResponseStatementDetail.StatamentSchedule lhs, QcResponseStatementDetail.StatamentSchedule rhs) {
        if (DateUtils.formatDateFromServer(lhs.start).getTime() >= DateUtils.formatDateFromServer(rhs.start).getTime()) {
            return -1;
        } else {
            return 1;
        }
    }
}
