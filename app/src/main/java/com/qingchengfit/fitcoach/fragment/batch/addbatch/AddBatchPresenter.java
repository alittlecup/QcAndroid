//package com.qingchengfit.fitcoach.fragment.batch.addbatch;
//
//import android.content.Intent;
//
//import javax.inject.Inject;
//
//import cn.qingchengfit.staffkit.mvpbase.PView;
//import cn.qingchengfit.staffkit.mvpbase.Presenter;
//import cn.qingchengfit.staffkit.usecase.GymUseCase;
//import cn.qingchengfit.staffkit.usecase.bean.CoachService;
//import cn.qingchengfit.staffkit.usecase.body.ArrangeBatchBody;
//import cn.qingchengfit.staffkit.usecase.response.QcResponse;
//import cn.qingchengfit.staffkit.usecase.response.QcResponseBtachTemplete;
//import cn.qingchengfit.staffkit.usecase.response.ResponseConstant;
//import cn.qingchengfit.staffkit.utils.ToastUtils;
//import rx.Subscription;
//import rx.functions.Action1;
//
///**
// * power by
// * <p/>
// * d8888b.  .d8b.  d8888b. d88888b d8888b.
// * 88  `8D d8' `8b 88  `8D 88'     88  `8D
// * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
// * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
// * 88      88   88 88      88.     88 `88.
// * 88      YP   YP 88      Y88888P 88   YD
// * <p/>
// * <p/>
// * Created by Paper on 16/5/4 2016.
// */
//public class AddBatchPresenter implements Presenter {
//    private AddBatchView view;
//    CoachService coachService;
//    GymUseCase gymUseCase;
//    private Subscription sp;
//    private Subscription spCheck;
//    private Subscription spTmpl;
//
//    @Inject
//    public AddBatchPresenter(CoachService coachService, GymUseCase gymUseCase) {
//        this.coachService = coachService;
//        this.gymUseCase = gymUseCase;
//    }
//
//    @Override
//    public void onStart() {
//
//    }
//
//    @Override
//    public void onStop() {
//
//    }
//
//    @Override
//    public void onPause() {
//
//    }
//
//    @Override
//    public void attachView(PView v) {
//        view = (AddBatchView) v;
//        if (sp != null)
//            sp.unsubscribe();
//        if (spCheck != null)
//            spCheck.unsubscribe();
//        if (spTmpl != null)
//            spTmpl.unsubscribe();
//    }
//
//    @Override
//    public void attachIncomingIntent(Intent intent) {
//
//    }
//
//    @Override
//    public void onCreate() {
//
//    }
//
//    @Override
//    public void unattachView() {
//        view = null;
//        if (sp != null)
//            sp.unsubscribe();
//        if (spCheck != null)
//            spCheck.unsubscribe();
//    }
//
//
//    public void arrangeBatch(ArrangeBatchBody body) {
//        sp = gymUseCase.arrangeBatch(coachService.getId(), coachService.getModel(), body, new Action1<QcResponse>() {
//            @Override
//            public void call(QcResponse qcResponse) {
//                if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
//                    view.onSuccess();
//                } else {
//                    view.onFailed();
//                    ToastUtils.logHttp(qcResponse);
//                }
//            }
//        });
//    }
//
//    public void checkBatch(int coursetype, ArrangeBatchBody body) {
//        spCheck = gymUseCase.checkBatch(coachService.getId(), coachService.getModel(), coursetype, body, new Action1<QcResponse>() {
//            @Override
//            public void call(QcResponse qcResponse) {
//                if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
//                    view.checkOk();
//                } else {
//                    view.checkFailed(qcResponse.getMsg());
////                    ToastUtils.logHttp(qcResponse);
//                }
//            }
//        });
//    }
//
//    public void getBatchTemplete(int coursetype, String teacher_id, String course_id) {
//        spTmpl = gymUseCase.getBatchTemplete(coursetype, coachService.getId(), coachService.getModel(), teacher_id, course_id, new Action1<QcResponseBtachTemplete>() {
//            @Override
//            public void call(QcResponseBtachTemplete qcResponseBtachTemplete) {
//                if (qcResponseBtachTemplete.getStatus() == ResponseConstant.SUCCESS) {
//                    view.onTemplete(qcResponseBtachTemplete.data.rule, qcResponseBtachTemplete.data.time_repeats, qcResponseBtachTemplete.data.max_users);
//                } else {
//                    ToastUtils.logHttp(qcResponseBtachTemplete);
//                }
//            }
//        });
//    }
//
//}