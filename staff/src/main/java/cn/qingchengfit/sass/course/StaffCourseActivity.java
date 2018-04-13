package cn.qingchengfit.sass.course;

import cn.qingchengfit.saasbase.cards.views.MutiChooseGymFragment;
import cn.qingchengfit.saasbase.course.CourseActivity;
import cn.qingchengfit.saasbase.course.batch.views.AddBatchFragment;
import cn.qingchengfit.saasbase.course.batch.views.BatchCopyFragment;
import cn.qingchengfit.saasbase.course.batch.views.BatchDetailCommonView;
import cn.qingchengfit.saasbase.course.batch.views.BatchListCategoryGroupFragment;
import cn.qingchengfit.saasbase.course.batch.views.BatchListCategoryPrivateFragment;
import cn.qingchengfit.saasbase.course.batch.views.BatchListGroupFragment;
import cn.qingchengfit.saasbase.course.batch.views.BatchListPrivateFragment;
import cn.qingchengfit.saasbase.course.batch.views.BatchScheduleListFragment;
import cn.qingchengfit.saasbase.course.batch.views.BatchSingleFragment;
import cn.qingchengfit.saasbase.course.batch.views.BatchTrainChooseFragment;
import cn.qingchengfit.saasbase.course.batch.views.EditBatchFragment;
import cn.qingchengfit.saasbase.course.batch.views.IBatchListCategoryFragment;
import cn.qingchengfit.saasbase.course.course.views.AddCourseFragment;
import cn.qingchengfit.saasbase.course.course.views.ChooseCoursePlanFragment;
import cn.qingchengfit.saasbase.course.course.views.CoachCommentListFragment;
import cn.qingchengfit.saasbase.course.course.views.CourseChooseFragment;
import cn.qingchengfit.saasbase.course.course.views.CourseDetailFragment;
import cn.qingchengfit.saasbase.course.course.views.CourseHomeInBrandFragment;
import cn.qingchengfit.saasbase.course.course.views.CourseImagesFragment;
import cn.qingchengfit.saasbase.course.course.views.CourseListFragment;
import cn.qingchengfit.saasbase.course.course.views.EditCourseFragment;
import cn.qingchengfit.saasbase.course.course.views.JacketManagerFragment;
import cn.qingchengfit.saasbase.course.course.views.ShopCommentsFragment;
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
  BatchSingleFragment.class, CourseDetailFragment.class, EditCourseFragment.class,
  CourseChooseFragment.class, CourseListFragment.class,JacketManagerFragment.class,ShopCommentsFragment.class,
  AddCourseFragment.class, CoachCommentListFragment.class, CourseHomeInBrandFragment.class,
  CourseImagesFragment.class, BatchDetailCommonView.class,
    MutiChooseGymFragment.class,ChooseCoursePlanFragment.class, BatchCopyFragment.class,
    BatchTrainChooseFragment.class,
})
public class StaffCourseActivity extends CourseActivity {

}
