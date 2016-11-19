package com.qingchengfit.fitcoach.fragment.batch.details;

import android.content.Intent;

import javax.inject.Inject;

import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.usecase.GymUseCase;
import cn.qingchengfit.staffkit.usecase.bean.Brand;
import cn.qingchengfit.staffkit.usecase.bean.CoachService;
import cn.qingchengfit.staffkit.usecase.body.ArrangeBatchBody;
import cn.qingchengfit.staffkit.usecase.response.QcResponse;
import cn.qingchengfit.staffkit.usecase.response.QcResponsePrivateBatchDetail;
import cn.qingchengfit.staffkit.usecase.response.ResponseConstant;
import cn.qingchengfit.staffkit.utils.GymUtils;
import cn.qingchengfit.staffkit.utils.ToastUtils;
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
public class BatchDetailPresenter extends BasePresenter {

    GymUseCase gymUseCase;
    CoachService coachService;
    BatchDetailView view;
    private Subscription sp;
    private Subscription spCheck;
    private Subscription spUpdate;
    private Brand brand;
    private RestRepository restRepository;

    @Inject
    public BatchDetailPresenter(GymUseCase gymUseCase, CoachService coachService,RestRepository restRepository,Brand brand) {
        this.gymUseCase = gymUseCase;
        this.coachService = coachService;
        this.restRepository = restRepository;
        this.brand = brand;
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
        view = (BatchDetailView) v;
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
        if (sp != null)
            sp.unsubscribe();
        if (spCheck != null)
            spCheck.unsubscribe();
        if (spUpdate != null)
            spUpdate.unsubscribe();

    }

    public void delbatch(String staffid, String batch_id){
        RxRegiste(restRepository.getPost_api().delBatch(staffid,batch_id, GymUtils.getParams(coachService,brand))
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<QcResponse>() {
                    @Override
                    public void call(QcResponse qcResponse) {
                        if (ResponseConstant.checkSuccess(qcResponse)){
                            view.onDelOk();
                        }else view.onFailed();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        view.onFailed();
                    }
                })
        );
    }


    public void queryData(String batch_id) {
        sp = gymUseCase.getBatchDetail(batch_id, coachService.getId(), coachService.getModel(), null, new Action1<QcResponsePrivateBatchDetail>() {
            @Override
            public void call(QcResponsePrivateBatchDetail qcResponsePrivateBatchDetail) {
                if (qcResponsePrivateBatchDetail.getStatus() == ResponseConstant.SUCCESS) {
                    view.onCoach(qcResponsePrivateBatchDetail.data.batch.teacher);
                    view.onCourse(qcResponsePrivateBatchDetail.data.batch.course);
                    view.onRule(qcResponsePrivateBatchDetail.data.batch.rule, qcResponsePrivateBatchDetail.data.batch.max_users);
                    view.onSpace(qcResponsePrivateBatchDetail.data.batch.spaces);
                    view.onTimeRepeat(qcResponsePrivateBatchDetail.data.batch.from_date ,qcResponsePrivateBatchDetail.data.batch.to_date);
                } else {

                }
            }
        });
    }

    public void checkBatch(int coursetype, ArrangeBatchBody body) {
        spCheck = gymUseCase.checkBatch(coachService.getId(), coachService.getModel(), coursetype, body, new Action1<QcResponse>() {
            @Override
            public void call(QcResponse qcResponse) {
                if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                    view.checkOk();
                } else {
                    ToastUtils.logHttp(qcResponse);

                }
            }
        });
    }

    public void updateBatch(String id, ArrangeBatchBody body){
        ArrangeBatchBody upBody = new ArrangeBatchBody();
        upBody.rules = body.rules;
        upBody.max_users = body.max_users;
        upBody.spaces = body.spaces;
        upBody.course_id = body.course_id;
        upBody.from_date = body.from_date;
        upBody.time_repeats = body.time_repeats;
        upBody.shop_id = body.shop_id;
        upBody.teacher_id = body.teacher_id;
        upBody.to_date = body.to_date;


        spUpdate = gymUseCase.updateBatch(coachService.getId(), coachService.getModel(), id, upBody, new Action1<QcResponse>() {
            @Override
            public void call(QcResponse qcResponse) {
                if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                    view.onSuccess();
                } else {
//                    ToastUtils.logHttp(qcResponse);
                    view.onFailed();
                }
            }
        });
    }

}
