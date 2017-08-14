package cn.qingchengfit.staffkit.views.statement.custom;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.Presenter;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.responese.ClassStatmentFilterBean;
import cn.qingchengfit.model.responese.CourseTypeSample;
import cn.qingchengfit.saasbase.db.GymBaseInfoAction;
import cn.qingchengfit.staffkit.model.dbaction.StudentAction;
import cn.qingchengfit.staffkit.usecase.StatementUsecase;
import cn.qingchengfit.staffkit.views.statement.detail.StatementDetailFragment;
import cn.qingchengfit.utils.DateUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

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
 * Created by Paper on 16/3/11 2016.
 */
public class CustomStatmentPresenter implements Presenter {

    @Inject StatementUsecase usecase;
    @Inject GymBaseInfoAction gymBaseInfoAction;
    @Inject StudentAction studentAction;

    private List<CourseTypeSample> courses;
    private List<QcStudentBean> studentBeans;

    private List<CoachService> mCoachServices = new ArrayList<>();
    private CustomStatementView customStatementView;
    private Subscription spGym;
    private Subscription spStudent;

    private String selectId = "0", selectModel, startTime, endTime, course_id, teacher_id, course_extra;
    private Subscription spCourse;

    @Inject public CustomStatmentPresenter(StatementUsecase usecase) {
        this.usecase = usecase;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        customStatementView = (CustomStatementView) v;
        startTime = DateUtils.Date2YYYYMMDD(new Date());
        endTime = DateUtils.Date2YYYYMMDD(new Date());
        spGym = gymBaseInfoAction.getGymBaseInfo().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<CoachService>>() {
            @Override public void call(List<CoachService> coachServices) {
                mCoachServices.clear();
                mCoachServices.addAll(coachServices);

                List<String> gyms = new ArrayList<>();
                for (CoachService coachService : coachServices) {
                    gyms.add(coachService.getName());
                }
                customStatementView.onGetGyms(gyms);
            }
        });
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        customStatementView = null;
        if (spGym != null) spGym.unsubscribe();
        if (spStudent != null) spStudent.unsubscribe();
        if (spCourse != null) spCourse.unsubscribe();
    }

    public void selectGym(int pos) {
        String id = mCoachServices.get(pos).getId();
        String model = mCoachServices.get(pos).getModel();
        //        spGym = usecase.queryResponseDetail(id, model, new Action1<QcResponseServiceDetial>() {
        //            @Override
        //            public void call(QcResponseServiceDetial qcResponseServiceDetial) {
        //                courses = qcResponseServiceDetial.data.service.courses;
        //                Collections.sort(courses, new CourseComparator());
        //                List<String> courseStr = new ArrayList<String>();
        //                for (CourseTypeSample course : courses) {
        //                    courseStr.add(course.getName());
        //                }
        //                customStatementView.onGetCars(courseStr);
        //
        //                studentBeans = qcResponseServiceDetial.data.service.users;
        //                Collections.sort(studentBeans, new StudentComparator());
        //                List<String> studentStr = new ArrayList<String>();
        //                for (StudentBean studentBean : studentBeans) {
        //                    studentStr.add(studentBean.getUsername());
        //                }
        //                customStatementView.onGetStudents(studentStr);
        //            }
        //        });
        selectId = id;
        selectModel = model;
        spStudent = studentAction.getStudentByGym(id, model).subscribe(new Action1<List<QcStudentBean>>() {
            @Override public void call(List<QcStudentBean> qcStudentBeen) {
                List<String> stringList = new ArrayList<String>();
                for (int i = 0; i < qcStudentBeen.size(); i++) {
                    stringList.add(qcStudentBeen.get(i).getUsername());
                }
                customStatementView.onGetStudents(stringList);
                studentBeans = qcStudentBeen;
            }
        });
        //        spCourse = usecase.queryCourse(id,, model, new Action1<CourseTypeSamples>() {
        //            @Override
        //            public void call(CourseTypeSamples qcResponseChooseCourses) {
        //                if (qcResponseChooseCourses.getStatus() == ResponseConstant.SUCCESS) {
        //                    List<String> stringList = new ArrayList<String>();
        //                    for (int i = 0; i < qcResponseChooseCourses.data.courses.size(); i++) {
        //                        stringList.add(qcResponseChooseCourses.data.courses.get(i).getName());
        //                    }
        //                    customStatementView.onGetCars(stringList);
        //                    courses = qcResponseChooseCourses.data.courses;
        //                } else {
        //                    // ToastUtils.logHttp(qcResponseChooseCourses);
        //                }
        //            }
        //        });

    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public void setCourse_extra(String course_extra) {
        this.course_extra = course_extra;
    }

    public void selectStartTime(String start) {
        startTime = start;
    }

    public void selectEndTime(String end) {
        endTime = end;
    }

    public void completedCustom(FragmentManager fragmentManager, int layout) {
        ClassStatmentFilterBean bean = new ClassStatmentFilterBean();
        bean.start = startTime;
        bean.end = endTime;
        Staff teacher = new Staff();
        teacher.id = teacher_id;
        bean.coach = teacher;
        CourseTypeSample course = new CourseTypeSample();
        course.setId(course_id);
        bean.course = course;
        if (course_extra != null) {
            if (course_extra.equals("all_private")) {
                bean.course_type = -2;
                bean.course = null;
            } else if (course_extra.equals("all_public")) {
                bean.course_type = -3;
                bean.course = null;
            }
        }

        fragmentManager.beginTransaction()
            .add(layout,
                StatementDetailFragment.newInstance(3, startTime, endTime, selectModel, Integer.parseInt(selectId), teacher_id, course_id,
                    course_extra, bean))
            .addToBackStack(null)
            .commit();
    }
}
