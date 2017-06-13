package cn.qingchengfit.staffkit.views.batch.list;

import cn.qingchengfit.model.responese.CourseTypeSample;
import cn.qingchengfit.model.responese.GroupCourseSchedule;
import cn.qingchengfit.model.responese.QcResponsePrivateDetail;
import cn.qingchengfit.staffkit.mvpbase.PView;
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
 * Created by Paper on 16/3/29 2016.
 */
public interface CourseBatchDetailView extends PView {
    void onGoup(CourseTypeSample course, List<GroupCourseSchedule> batch);

    void onPrivate(QcResponsePrivateDetail.PrivateCoach coach, List<QcResponsePrivateDetail.PrivateBatch> batch);
}
