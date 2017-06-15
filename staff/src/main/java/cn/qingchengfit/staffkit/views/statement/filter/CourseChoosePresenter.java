package cn.qingchengfit.staffkit.views.statement.filter;

import android.content.Intent;
import android.os.Bundle;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.model.LoginStatus;
import cn.qingchengfit.model.responese.CourseTypeSample;
import cn.qingchengfit.model.responese.CourseTypeSamples;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.usecase.StatementUsecase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
 * Created by Paper on 16/6/29 2016.
 */
public class CourseChoosePresenter extends BasePresenter {

    StatementUsecase usecase;
    CourseChooseView view;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private RestRepository restRepository;
    private List<CourseTypeSample> mAllCourse = new ArrayList<>();
    private List<CourseTypeSample> mPrivateCourse = new ArrayList<>();
    private List<CourseTypeSample> mGroupCourse = new ArrayList<>();
    private int mType = -1;

    @Inject public CourseChoosePresenter(StatementUsecase usecase, RestRepository restRepository) {
        this.usecase = usecase;
        this.restRepository = restRepository;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (CourseChooseView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public void changeType(int type) {
        mType = type;
        changeCourse();
    }

    public void changeCourse() {
        if (mAllCourse != null) {
            if (mType < 0) {//所有
                view.onCourseList(mAllCourse);
            } else if (mType == Configs.TYPE_PRIVATE) {
                view.onCourseList(mPrivateCourse);
            } else if (mType == Configs.TYPE_GROUP) {
                view.onCourseList(mGroupCourse);
            }
        } else {
            queryCourse();
        }
    }

    public void setCourse(Bundle intent) {
        List<CourseTypeSample> c = intent.getParcelableArrayList("course");
        if (c != null) {
            mAllCourse.clear();
            mAllCourse.addAll(c);
            mPrivateCourse.clear();
            mGroupCourse.clear();
            CourseTypeSample course = new CourseTypeSample();
            course.setId("-3");
            course.setName("所有团课");
            mGroupCourse.add(course);
            CourseTypeSample course1 = new CourseTypeSample();
            course1.setId("-2");
            course1.setName("所有私教");
            mPrivateCourse.add(course1);

            for (int i = 0; i < mAllCourse.size(); i++) {
                if (mAllCourse.get(i).is_private()) {
                    mPrivateCourse.add(mAllCourse.get(i));
                } else {
                    mGroupCourse.add(mAllCourse.get(i));
                }
            }

            CourseTypeSample course3 = new CourseTypeSample();
            course3.setId("-1");
            course3.setName("所有课程");
            mAllCourse.add(0, course3);
        }
    }

    public List<CourseTypeSample> getSecondCourse() {
        return mAllCourse;
    }

    public void queryCourse() {
        HashMap<String, Object> params = gymWrapper.getParams();
        params.put("key", "course_all");
        params.put("method", "get");
        RxRegiste(restRepository.getGet_api()
            .qcGetAllCoursesPermission(App.staffId, params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseData<CourseTypeSamples>>() {
                @Override public void call(QcResponseData<CourseTypeSamples> qcResponseChooseCourses) {
                    if (ResponseConstant.checkSuccess(qcResponseChooseCourses)) {
                        mAllCourse = qcResponseChooseCourses.data.courses;
                        mPrivateCourse.clear();
                        mGroupCourse.clear();
                        CourseTypeSample course = new CourseTypeSample();
                        course.setId("-3");
                        course.setName("所有团课");
                        mGroupCourse.add(course);
                        CourseTypeSample course1 = new CourseTypeSample();
                        course1.setId("-2");
                        course1.setName("所有私教");
                        mPrivateCourse.add(course1);
                        for (int i = 0; i < mAllCourse.size(); i++) {
                            if (mAllCourse.get(i).is_private()) {
                                mPrivateCourse.add(mAllCourse.get(i));
                            } else {
                                mGroupCourse.add(mAllCourse.get(i));
                            }
                        }
                        CourseTypeSample course3 = new CourseTypeSample();
                        course3.setId("-1");
                        course3.setName("所有课程");
                        mAllCourse.add(0, course3);
                        changeCourse();
                    } else {

                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            }));
        //        RxRegiste(usecase.queryCourse(brand.getId(), coachService.getId(), coachService.getModel(), new Action1<CourseTypeSamples>() {
        //            @Override
        //            public void call(CourseTypeSamples qcResponseChooseCourses) {
        //                if (ResponseConstant.checkSuccess(qcResponseChooseCourses)) {
        //                    mAllCourse = qcResponseChooseCourses.data.courses;
        //                    mPrivateCourse.clear();
        //                    mGroupCourse.clear();
        //                    CourseTypeSample course = new CourseTypeSample();
        //                    course.setId("-3");
        //                    course.setName("所有团课");
        //                    mGroupCourse.add(course);
        //                    CourseTypeSample course1 = new CourseTypeSample();
        //                    course1.setId("-2");
        //                    course1.setName("所有私教");
        //                    mPrivateCourse.add(course1);
        //                    for (int i = 0; i < mAllCourse.size(); i++) {
        //                        if (mAllCourse.get(i).is_private()) {
        //                            mPrivateCourse.add(mAllCourse.get(i));
        //                        } else mGroupCourse.add(mAllCourse.get(i));
        //                    }
        //                    CourseTypeSample course3 = new CourseTypeSample();
        //                    course3.setId("-1");
        //                    course3.setName("所有课程");
        //                    mAllCourse.add(0, course3);
        //                    changeCourse();
        //                } else {
        //
        //                }
        //            }
        //        }));
    }
}
