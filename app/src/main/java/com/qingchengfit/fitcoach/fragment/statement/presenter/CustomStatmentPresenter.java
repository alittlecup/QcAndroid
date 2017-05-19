package com.qingchengfit.fitcoach.fragment.statement.presenter;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.utils.DateUtils;
import com.anbillon.qcmvplib.PView;
import com.anbillon.qcmvplib.Presenter;
import com.qingchengfit.fitcoach.bean.CourseTypeSample;
import com.qingchengfit.fitcoach.fragment.StatementDetailFragment;
import com.qingchengfit.fitcoach.fragment.statement.model.ClassStatmentFilterBean;
import com.qingchengfit.fitcoach.fragment.statement.CustomStatementView;
import com.qingchengfit.fitcoach.fragment.statement.StatementUsecase;
import com.qingchengfit.fitcoach.http.bean.CoachService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import rx.Subscription;

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

    private List<CourseTypeSample> courses;
    private List<QcStudentBean> studentBeans;

    private List<CoachService> mCoachServices = new ArrayList<>();
    private CustomStatementView customStatementView;
    private Subscription spGym;
    private Subscription spStudent;

    private long selectId;
    private String selectModel;
    private String startTime;
    private String endTime;
    private String course_id;
    private String teacher_id;
    private String course_extra;
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
        //spGym = GymBaseInfoAction.getGymBaseInfo().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<CoachService>>() {
        //    @Override public void call(List<CoachService> coachServices) {
        //        mCoachServices.clear();
        //        mCoachServices.addAll(coachServices);
        //
        //        List<String> gyms = new ArrayList<>();
        //        for (CoachService coachService : coachServices) {
        //            gyms.add(coachService.getName());
        //        }
        //        customStatementView.onGetGyms(gyms);
        //    }
        //});
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
        long id = mCoachServices.get(pos).getId();
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
        //spStudent = StudentAction.newInstance().getStudentByGym(id, model).subscribe(new Action1<List<QcStudentBean>>() {
        //    @Override public void call(List<QcStudentBean> qcStudentBeen) {
        //        List<String> stringList = new ArrayList<String>();
        //        for (int i = 0; i < qcStudentBeen.size(); i++) {
        //            stringList.add(qcStudentBeen.get(i).getUsername());
        //        }
        //        customStatementView.onGetStudents(stringList);
        //        studentBeans = qcStudentBeen;
        //    }
        //});
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
                StatementDetailFragment.newInstance(3, startTime, endTime, selectModel, selectId, teacher_id, course_id,
                    course_extra, bean))
            .addToBackStack(null)
            .commit();
    }
}
