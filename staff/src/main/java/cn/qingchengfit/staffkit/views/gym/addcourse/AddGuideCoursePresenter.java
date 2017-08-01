package cn.qingchengfit.staffkit.views.gym.addcourse;

import android.content.Intent;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.Presenter;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.body.CourseBody;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.usecase.GymUseCase;
import javax.inject.Inject;
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
 * Created by Paper on 16/3/28 2016.
 */
public class AddGuideCoursePresenter implements Presenter {

    AddGuideCourseView view;
    GymUseCase useCase;
    CoachService coachService;

    @Inject public AddGuideCoursePresenter(GymUseCase useCase, CoachService coachService) {
        this.useCase = useCase;
        this.coachService = coachService;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (AddGuideCourseView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        view = null;
    }

    public void addCourse(CourseBody body) {
        useCase.createCourses(body, coachService.getId(), coachService.getModel(), new Action1<QcResponse>() {
            @Override public void call(QcResponse qcResponse) {
                if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                    //新增成功
                    view.onSucceed();
                } else {
                    view.onFailed(qcResponse.msg);
                }
            }
        });
    }

    public void editCourse(String courseid, CourseBody body) {
        body.id = null;
        useCase.updateCourses(courseid, body, coachService.getId(), coachService.getModel(), new Action1<QcResponse>() {
            @Override public void call(QcResponse qcResponse) {
                if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                    //新增成功
                    view.onSucceed();
                } else {
                    view.onFailed(qcResponse.msg);
                }
            }
        });
    }

    //    public void delCourse(String id) {
    //        useCase.delCourses(id, coachService.getId(), coachService.getModel(), new Action1<QcResponse>() {
    //            @Override
    //            public void call(QcResponse qcResponse) {
    //                if (ResponseConstant.checkSuccess(qcResponse)){
    //                    view.onSucceed();
    //                }else {
    //                    view.onFailed(qcResponse.getMsg());
    //                }
    //            }
    //        });
    //    }
}
