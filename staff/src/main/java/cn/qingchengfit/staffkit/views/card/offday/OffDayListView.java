package cn.qingchengfit.staffkit.views.card.offday;

import cn.qingchengfit.di.PView;
import cn.qingchengfit.model.common.OffDay;
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
 * Created by Paper on 16/3/24 2016.
 */
public interface OffDayListView extends PView {
    void onOffDayList(List<OffDay> offDays);

    void onSucceess();

    void onFailed(String s);
}
