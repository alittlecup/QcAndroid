package com.qingchengfit.fitcoach.fragment.batch;

import android.content.Intent;

import com.anbillon.qcmvplib.PView;
import com.anbillon.qcmvplib.Presenter;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.http.RestRepository;
import com.qingchengfit.fitcoach.http.bean.CoachService;
import com.qingchengfit.fitcoach.http.bean.QcResponseGroupCourse;
import com.qingchengfit.fitcoach.http.bean.QcResponsePrivateCourse;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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

    CoachService coachService;
    CourseListView view;
    private Subscription spgourp;
    private Subscription spPrivate;

    @Inject
    RestRepository restRepository;
    @Inject
    public CoureseListPresenter( CoachService coachService) {
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
        spgourp = restRepository.getGet_api().qcGetGroupCourse(App.staffId,coachService.id+"",coachService.model,"")
         .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                         .subscribe(new Action1<QcResponseGroupCourse>() {
                             @Override
                             public void call(QcResponseGroupCourse qcResponseGroupCourse) {
                                 view.onGroup(qcResponseGroupCourse.data.courses);
                                 view.onCoursesInfo(qcResponseGroupCourse.data.total_count, qcResponseGroupCourse.data.order_url);
                             }
                         }, new Action1<Throwable>() {
                             @Override
                             public void call(Throwable throwable) {

                             }
                         });
    }

    public void getPrivate() {


        spPrivate = restRepository.getGet_api().qcGetPrivateCrourse(App.staffId, coachService.getId()+"", coachService.getModel(),null)
                 .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                 .subscribe(new Action1<QcResponsePrivateCourse>() {
                                     @Override
                                     public void call(QcResponsePrivateCourse qcResponsePrivateCourse) {
                                         view.onCoursesInfo(qcResponsePrivateCourse.data.total_count, qcResponsePrivateCourse.data.order_url);
                                         view.onPrivate(qcResponsePrivateCourse.data.coaches);
                                     }
                                 }, new Action1<Throwable>() {
                                     @Override
                                     public void call(Throwable throwable) {
//                                         view.onShowError(throwable.getMessage());
                                     }
                                 });
    }


}
