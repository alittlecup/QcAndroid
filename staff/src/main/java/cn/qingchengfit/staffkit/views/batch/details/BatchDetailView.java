package cn.qingchengfit.staffkit.views.batch.details;

import cn.qingchengfit.di.PView;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.common.BatchOpenRule;
import cn.qingchengfit.model.common.Rule;
import cn.qingchengfit.model.responese.CardTplBatchShip;
import cn.qingchengfit.model.responese.CourseTypeSample;
import cn.qingchengfit.model.responese.Space;
import cn.qingchengfit.model.responese.Time_repeat;
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
 * Created by Paper on 16/4/30 2016.
 */
public interface BatchDetailView extends PView {
    void onCoach(Staff teacher);

    void onCourse(CourseTypeSample course);

    void onSpace(List<Space> spaces);

    void onRule(List<Rule> rules, int max_user, boolean isfree, List<CardTplBatchShip> cardTplBatchShips, boolean hasOrder);

    void onTimeRepeat(String timestart, String timeend, List<Time_repeat> time_repeats);
    void onOpenRule(BatchOpenRule rule);
    void checkOk();

    void onSuccess();

    void onFailed();
    void onCheckFaild(String s);
    void onDelOk();
}
