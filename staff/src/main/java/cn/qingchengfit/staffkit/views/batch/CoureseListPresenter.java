package cn.qingchengfit.staffkit.views.batch;

import android.content.Intent;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.model.LoginStatus;
import cn.qingchengfit.model.responese.GroupCourseResponse;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.QcResponsePrivateCourse;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.mvpbase.Presenter;
import cn.qingchengfit.staffkit.usecase.GymUseCase;
import javax.inject.Inject;
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
    CourseListView view;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private Subscription spgourp;
    private Subscription spPrivate;

    @Inject public CoureseListPresenter(GymUseCase gymUseCase) {
        this.gymUseCase = gymUseCase;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (CourseListView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        view = null;
        if (spgourp != null) spgourp.unsubscribe();
        if (spPrivate != null) spPrivate.unsubscribe();
    }

    public void getGroup() {
        spgourp = gymUseCase.getGroupCourse(null, gymWrapper.id(), gymWrapper.model(), new Action1<QcResponseData<GroupCourseResponse>>() {
            @Override public void call(QcResponseData<GroupCourseResponse> qcResponseGroupCourse) {
                view.onGroup(qcResponseGroupCourse.data.courses);
                view.onCoursesInfo(qcResponseGroupCourse.data.total_count, qcResponseGroupCourse.data.order_url);
            }
        });
    }

    public void getPrivate() {
        spPrivate = gymUseCase.getPrivateCourse(null, gymWrapper.id(), gymWrapper.model(), new Action1<QcResponsePrivateCourse>() {
            @Override public void call(QcResponsePrivateCourse qcResponsePrivateCourse) {
                view.onCoursesInfo(qcResponsePrivateCourse.data.total_count, qcResponsePrivateCourse.data.order_url);
                view.onPrivate(qcResponsePrivateCourse.data.coaches);
            }
        });
    }
}
