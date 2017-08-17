package cn.qingchengfit.staffkit.views.batch.addbatch;

import cn.qingchengfit.model.common.Rule;
import cn.qingchengfit.model.responese.Time_repeat;
import cn.qingchengfit.staffkit.mvpbase.PView;
import java.util.ArrayList;

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
 * Created by Paper on 16/5/4 2016.
 */
public interface AddBatchView extends PView {
    void onSuccess();

    void onFailed();

    void checkOk();

    void checkFailed(String s);

    void onTemplete(boolean isFree, ArrayList<Rule> rules, ArrayList<Time_repeat> time_repeats, int maxuer);
}