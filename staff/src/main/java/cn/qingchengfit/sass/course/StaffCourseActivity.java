package cn.qingchengfit.sass.course;

import cn.qingchengfit.saasbase.course.CourseActivity;
import cn.qingchengfit.saasbase.course.batch.views.AddBatchFragment;
import cn.qingchengfit.saasbase.course.batch.views.BatchListCategoryGroupFragment;
import cn.qingchengfit.saasbase.course.batch.views.BatchListCategoryPrivateFragment;
import cn.qingchengfit.saasbase.course.batch.views.BatchListGroupFragment;
import cn.qingchengfit.saasbase.course.batch.views.BatchListPrivateFragment;
import cn.qingchengfit.saasbase.course.batch.views.BatchScheduleListFragment;
import cn.qingchengfit.saasbase.course.batch.views.BatchSingleFragment;
import cn.qingchengfit.saasbase.course.batch.views.EditBatchFragment;
import cn.qingchengfit.saasbase.course.batch.views.IBatchListCategoryFragment;
import cn.qingchengfit.saasbase.course.course.views.CourseChooseFragment;
import cn.qingchengfit.saasbase.course.course.views.CourseListFragment;
import com.anbillon.flabellum.annotations.Trunk;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/11/6.
 */
@Trunk(fragments = {
  BatchListPrivateFragment.class, BatchListGroupFragment.class, AddBatchFragment.class,
  BatchListCategoryGroupFragment.class, BatchListCategoryPrivateFragment.class,
  IBatchListCategoryFragment.class, EditBatchFragment.class, BatchScheduleListFragment.class,
  BatchSingleFragment.class,
  CourseChooseFragment.class, CourseListFragment.class
})
public class StaffCourseActivity extends CourseActivity {

}
