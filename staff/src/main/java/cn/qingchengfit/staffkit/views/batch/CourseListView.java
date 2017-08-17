package cn.qingchengfit.staffkit.views.batch;

import cn.qingchengfit.model.responese.GroupCourse;
import cn.qingchengfit.model.responese.QcResponsePrivateCourse;
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
 * Created by Paper on 16/4/30 2016.
 */
public interface CourseListView extends PView {
    void onGroup(List<GroupCourse> groupClasses);

    void onPrivate(List<QcResponsePrivateCourse.PrivateClass> privateClasses);

    void onCoursesInfo(int course_course, String url);
}