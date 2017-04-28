package com.qingchengfit.fitcoach.di;

import com.qingchengfit.fitcoach.fragment.batch.GymCoursesFragment;
import com.qingchengfit.fitcoach.fragment.batch.addbatch.AddBatchFragment;
import com.qingchengfit.fitcoach.fragment.batch.details.BatchDetailFragment;
import com.qingchengfit.fitcoach.fragment.batch.list.CourseBatchDetailFragment;
import com.qingchengfit.fitcoach.fragment.batch.single.SingleBatchFragment;
import com.qingchengfit.fitcoach.fragment.course.AddCourseFragment;
import com.qingchengfit.fitcoach.fragment.course.ChooseCoursePlanFragment;
import com.qingchengfit.fitcoach.fragment.course.CoachCommentDetailFragment;
import com.qingchengfit.fitcoach.fragment.course.CoachCommentListFragment;
import com.qingchengfit.fitcoach.fragment.course.CourseActivity;
import com.qingchengfit.fitcoach.fragment.course.CourseBaseInfoEditFragment;
import com.qingchengfit.fitcoach.fragment.course.CourseBaseInfoShowFragment;
import com.qingchengfit.fitcoach.fragment.course.CourseDetailFragment;
import com.qingchengfit.fitcoach.fragment.course.CourseFragment;
import com.qingchengfit.fitcoach.fragment.course.CourseImageViewFragment;
import com.qingchengfit.fitcoach.fragment.course.CourseImagesFragment;
import com.qingchengfit.fitcoach.fragment.course.CourseListFragment;
import com.qingchengfit.fitcoach.fragment.course.EditCourseFragment;
//import com.qingchengfit.fitcoach.fragment.course.GymCourseListFragment;
import com.qingchengfit.fitcoach.fragment.course.JacketManagerFragment;
import com.qingchengfit.fitcoach.fragment.course.ShopCommentsFragment;

import dagger.Component;

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
 * Created by Paper on 16/11/17.
 */
@Component(modules = CourseModule.class)
public interface CourseComponent {
    void inject(CourseActivity a);
    void inject(AddCourseFragment a);
    void inject(ChooseCoursePlanFragment a);
    void inject(CoachCommentDetailFragment a);
    void inject(CoachCommentListFragment a);
    void inject(CourseBaseInfoEditFragment a);
    void inject(CourseBaseInfoShowFragment a);
    void inject(CourseDetailFragment a);
    void inject(CourseFragment a);
    void inject(CourseImagesFragment a);
    void inject(CourseImageViewFragment a);
    void inject(CourseListFragment a);
    void inject(EditCourseFragment a);
    void inject(JacketManagerFragment a);
    void inject(ShopCommentsFragment a);



    void inject(GymCoursesFragment fragment);
    void inject(com.qingchengfit.fitcoach.fragment.batch.CourseListFragment fragment);
    void inject(CourseBatchDetailFragment fragment);
    void inject(AddBatchFragment fragment);
    void inject(BatchDetailFragment fragment);
    void inject(SingleBatchFragment fragment);



}
