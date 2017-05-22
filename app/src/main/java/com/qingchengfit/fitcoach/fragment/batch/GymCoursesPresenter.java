package com.qingchengfit.fitcoach.fragment.batch;

import android.content.Intent;
import cn.qingchengfit.model.base.CoachService;
import com.anbillon.qcmvplib.PView;
import com.anbillon.qcmvplib.Presenter;
import javax.inject.Inject;

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
 * Created by Paper on 16/3/23 2016.
 */
public class GymCoursesPresenter implements Presenter {

    CoachService coachService;
    GymCoursesView view;

    @Inject public GymCoursesPresenter(CoachService coachService) {
        this.coachService = coachService;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (GymCoursesView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        view = null;
    }

    public void queryData() {
        //        useCase.queryCourses(coachService.getId(), coachService.getModel(), new Action1<QcResponseCourse>() {
        //            @Override
        //            public void call(QcResponseCourse qcResponseCourse) {
        //                if (qcResponseCourse.getStatus() == ResponseConstant.SUCCESS) {
        //                    ArrayList<ImageThreeTextBean> group = new ArrayList<ImageThreeTextBean>();
        //                    ArrayList<ImageThreeTextBean> pri = new ArrayList<ImageThreeTextBean>();
        //                    for (QcResponseCourse.Course course : qcResponseCourse.data.shop.courses) {
        //
        //                        ImageThreeTextBean bean = new ImageThreeTextBean(course.photo, course.name, "时长: " + course.length / 60 + "分钟", "累计" + course.course_count + "节,服务" + course.service_count + "人次");
        //                        bean.tags.put(ImageThreeTextBean.TAG_MODEL, qcResponseCourse.data.service.getModel());
        //                        bean.tags.put(ImageThreeTextBean.TAG_ID, qcResponseCourse.data.service.getId());
        //                        bean.tags.put(ImageThreeTextBean.TAG_COURSE, course.id + "");
        //                        bean.tags.put(ImageThreeTextBean.TAG_LENGTH, course.length + "");
        //                        bean.tags.put(ImageThreeTextBean.TAG_COURSETYPE, course.is_private ? Configs.TYPE_PRIVATE + "" : Configs.TYPE_GROUP + "");
        //                        bean.showIcon = false;
        //                        if (course.is_private) {
        //                            pri.add(bean);
        //                        } else group.add(bean);
        //
        //                    }
        //
        //                    view.onData(pri, group, qcResponseCourse.data.shop.private_url, qcResponseCourse.data.shop.team_url);
        //                }
        //            }
        //        });
    }
}
