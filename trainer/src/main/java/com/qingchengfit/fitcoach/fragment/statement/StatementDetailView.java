package com.qingchengfit.fitcoach.fragment.statement;

import cn.qingchengfit.di.PView;
import cn.qingchengfit.saasbase.report.bean.QcResponseStatementDetail;
import java.util.List;

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
 * Created by Paper on 16/3/8 2016.
 */
public interface StatementDetailView extends PView {
    void onSuccess(List<QcResponseStatementDetail.StatamentSchedule> list);

    void onFailed(String e);
}
