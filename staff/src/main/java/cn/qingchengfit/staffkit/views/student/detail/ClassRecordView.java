package cn.qingchengfit.staffkit.views.student.detail;

import cn.qingchengfit.di.PView;
import cn.qingchengfit.model.common.AttendanceRecord;
import cn.qingchengfit.model.common.Shop;
import cn.qingchengfit.model.responese.ClassRecords;
import java.util.List;

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
 * Created by Paper on 16/3/19 2016.
 */
public interface ClassRecordView extends PView {
    void onData(List<AttendanceRecord> statementBeans, ClassRecords.Stat stat, List<Shop> ss);
}
