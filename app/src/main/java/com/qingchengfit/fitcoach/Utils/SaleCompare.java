package com.qingchengfit.fitcoach.Utils;

import cn.qingchengfit.utils.DateUtils;
import com.qingchengfit.fitcoach.fragment.statement.model.QcResponseSaleDetail;
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
public class SaleCompare implements Comparator<QcResponseSaleDetail.History> {

    @Override public int compare(QcResponseSaleDetail.History lhs, QcResponseSaleDetail.History rhs) {
        if (DateUtils.formatDateFromServer(lhs.created_at).getTime() >= DateUtils.formatDateFromServer(rhs.created_at).getTime()) {
            return -1;
        } else {
            return 1;
        }
    }
}
