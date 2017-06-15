package cn.qingchengfit.staffkit.views.batch.list;

import android.content.Intent;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.model.LoginStatus;
import cn.qingchengfit.model.responese.GroupCourseScheduleDetail;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.QcResponsePrivateDetail;
import cn.qingchengfit.model.responese.ResponseConstant;
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
 * Created by Paper on 16/3/29 2016.
 */
public class CourseBatchDetailPresenter implements Presenter {

    GymUseCase gymUseCase;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private CourseBatchDetailView view;
    private Subscription groupSp;
    private Subscription priSp;

    @Inject public CourseBatchDetailPresenter(GymUseCase gymUseCase) {
        this.gymUseCase = gymUseCase;
    }

    @Inject

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (CourseBatchDetailView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        view = null;
        if (groupSp != null) groupSp.unsubscribe();
        if (priSp != null) priSp.unsubscribe();
    }

    public void queryBatches() {

    }

    public void delCourse() {

    }

    public void delBtach() {

    }

    public void queryGroup(String id) {
        groupSp = gymUseCase.getGroupBatches(id, gymWrapper.id(), gymWrapper.model(), null,
            new Action1<QcResponseData<GroupCourseScheduleDetail>>() {
                @Override public void call(QcResponseData<GroupCourseScheduleDetail> qcResponseGroupDetail) {
                    if (qcResponseGroupDetail.getStatus() == ResponseConstant.SUCCESS) {
                        view.onGoup(qcResponseGroupDetail.data.course, qcResponseGroupDetail.data.batches);
                    } else {

                    }
                }
            });
    }

    public void queryPrivate(String id) {
        priSp = gymUseCase.getPrivateBatches(id, gymWrapper.id(), gymWrapper.model(), null, new Action1<QcResponsePrivateDetail>() {
            @Override public void call(QcResponsePrivateDetail qcResponsePrivateDetail) {
                if (qcResponsePrivateDetail.getStatus() == ResponseConstant.SUCCESS) {
                    view.onPrivate(qcResponsePrivateDetail.data.coach, qcResponsePrivateDetail.data.batches);
                } else {

                }
            }
        });
    }
}
