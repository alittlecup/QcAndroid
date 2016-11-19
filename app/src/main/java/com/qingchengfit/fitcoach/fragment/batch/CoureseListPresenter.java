package com.qingchengfit.fitcoach.fragment.batch;

import android.content.Intent;

import javax.inject.Inject;

import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.mvpbase.Presenter;
import cn.qingchengfit.staffkit.usecase.GymUseCase;
import cn.qingchengfit.staffkit.usecase.bean.CoachService;
import cn.qingchengfit.staffkit.usecase.response.QcResponseGroupCourse;
import cn.qingchengfit.staffkit.usecase.response.QcResponsePrivateCourse;
import rx.Subscription;
import rx.functions.Action1;

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
public class CoureseListPresenter implements Presenter {

    GymUseCase gymUseCase;
    CoachService coachService;
    CourseListView view;
    private Subscription spgourp;
    private Subscription spPrivate;

    @Inject
    public CoureseListPresenter(GymUseCase gymUseCase, CoachService coachService) {
        this.gymUseCase = gymUseCase;
        this.coachService = coachService;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void attachView(PView v) {
        view = (CourseListView) v;
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void unattachView() {
        view = null;
        if (spgourp != null)
            spgourp.unsubscribe();
        if (spPrivate != null)
            spPrivate.unsubscribe();
    }

    public void getGroup() {
        spgourp = gymUseCase.getGroupCourse(null, coachService.getId(), coachService.getModel(), new Action1<QcResponseGroupCourse>() {
            @Override
            public void call(QcResponseGroupCourse qcResponseGroupCourse) {
                view.onGroup(qcResponseGroupCourse.data.courses);
                view.onCoursesInfo(qcResponseGroupCourse.data.total_count, qcResponseGroupCourse.data.order_url);
            }
        });
    }

    public void getPrivate() {
        spPrivate = gymUseCase.getPrivateCourse(null, coachService.getId(), coachService.getModel(), new Action1<QcResponsePrivateCourse>() {
            @Override
            public void call(QcResponsePrivateCourse qcResponsePrivateCourse) {
                view.onCoursesInfo(qcResponsePrivateCourse.data.total_count, qcResponsePrivateCourse.data.order_url);
                view.onPrivate(qcResponsePrivateCourse.data.coaches);
            }
        });
    }


}
