package com.qingchengfit.fitcoach.fragment.batch.list;

import android.content.Intent;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.model.base.CoachService;
import com.qingchengfit.fitcoach.http.ResponseConstant;
import com.qingchengfit.fitcoach.http.RestRepository;
import com.qingchengfit.fitcoach.http.bean.QcResponseGroupDetail;
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
 * Created by Paper on 16/3/29 2016.
 */
public class CourseBatchDetailPresenter extends BasePresenter {

    CoachService coachService;
    @Inject RestRepository restRepository;
    private CourseBatchDetailView view;
    private Subscription groupSp;
    private Subscription priSp;

    @Inject public CourseBatchDetailPresenter(CoachService coachService) {
        //        this.gymUseCase = gymUseCase;
        this.coachService = coachService;
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
        super.unattachView();
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

    public void queryGroup(String staffid, boolean isPrivate) {
        RxRegiste(restRepository.getGet_api().qcGetGroupCourses(staffid, coachService.id + "", coachService.getModel(), isPrivate ? 1 : 0)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseGroupDetail>() {
                @Override public void call(QcResponseGroupDetail qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        view.onGoup(qcResponse.data.course, qcResponse.data.batches);
                    } else {
                        view.onShowError(qcResponse.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    view.onShowError(throwable.getMessage());
                }
            }));
        //        groupSp = gymUseCase.getGroupBatches(id, coachService.getId(), coachService.getModel(), null, new Action1<QcResponseGroupDetail>() {
        //            @Override
        //            public void call(QcResponseGroupDetail qcResponseGroupDetail) {
        //                if (qcResponseGroupDetail.getStatus() == ResponseConstant.SUCCESS) {
        //                    view.onGoup(qcResponseGroupDetail.data.course, qcResponseGroupDetail.data.batches);
        //                } else {
        //
        //                }
        //            }
        //        });
    }

    public void queryPrivate(String staffid, String coachid) {
        //priSp = restRepository.getGet_api()
        //    .qcGetPrivateBatches(staffid, coachid, coachService.id + "", coachService.model, null)
      //    .onBackpressureBuffer().subscribeOn(Schedulers.io())
        //    .observeOn(AndroidSchedulers.mainThread())
        //    .subscribe(new Action1<QcResponsePrivateDetail>() {
        //        @Override public void call(QcResponsePrivateDetail qcResponse) {
        //            if (ResponseConstant.checkSuccess(qcResponse)) {
        //                view.onPrivate(qcResponse.data.coach, qcResponse.data.batches);
        //            } else {
        //                view.onShowError(qcResponse.getMsg());
        //            }
        //        }
        //    }, new Action1<Throwable>() {
        //        @Override public void call(Throwable throwable) {
        //            view.onShowError(throwable.getMessage());
        //        }
        //    });
    }
}
