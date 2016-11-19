package com.qingchengfit.fitcoach.fragment.batch.details;

import java.util.List;

import cn.qingchengfit.staffkit.model.bean.Course;
import cn.qingchengfit.staffkit.model.bean.Rule;
import cn.qingchengfit.staffkit.model.bean.Space;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.usecase.response.QcSchedulesResponse;

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
    void onCoach(QcSchedulesResponse.Teacher teacher);

    void onCourse(Course course);

    void onSpace(List<Space> spaces);

    void onRule(List<Rule> rules, int max_user);

    void onTimeRepeat(String timestart, String timeend);
    void checkOk();
    void onSuccess();
    void onFailed();
    void onDelOk();


}
