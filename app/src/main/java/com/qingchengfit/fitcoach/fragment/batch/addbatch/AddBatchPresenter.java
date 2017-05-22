package com.qingchengfit.fitcoach.fragment.batch.addbatch;

import android.content.Intent;
import com.anbillon.qcmvplib.PView;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.bean.ArrangeBatchBody;
import com.qingchengfit.fitcoach.di.BasePresenter;
import com.qingchengfit.fitcoach.http.QcResponseBtachTemplete;
import com.qingchengfit.fitcoach.http.ResponseConstant;
import com.qingchengfit.fitcoach.http.RestRepository;
import cn.qingchengfit.model.base.CoachService;
import com.qingchengfit.fitcoach.http.bean.QcResponse;
import javax.inject.Inject;
import rx.Subscription;
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
 * Created by Paper on 16/5/4 2016.
 */
public class AddBatchPresenter extends BasePresenter {
    private AddBatchView view;
    CoachService coachService;
    private Subscription sp;
    private Subscription spCheck;
    private Subscription spTmpl;

    @Inject
    RestRepository mRestRepository;

    @Inject
    public AddBatchPresenter(CoachService coachService) {
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
        view = (AddBatchView) v;
        if (sp != null)
            sp.unsubscribe();
        if (spCheck != null)
            spCheck.unsubscribe();
        if (spTmpl != null)
            spTmpl.unsubscribe();
    }

    @Override
    public void attachIncomingIntent(Intent intent) {

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void unattachView() {
        super.unattachView();
        view = null;
        if (sp != null)
            sp.unsubscribe();
        if (spCheck != null)
            spCheck.unsubscribe();
    }


    public void arrangeBatch(ArrangeBatchBody body) {
        sp = mRestRepository.getPost_api().qcArrangeBatch(App.coachid+"",coachService.getId()+"",coachService.getModel(),body)
             .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                             .subscribe(new Action1<QcResponse>() {
                                 @Override
                                 public void call(QcResponse qcResponse) {
                                     if (ResponseConstant.checkSuccess(qcResponse)) {
                                         view.onSuccess();
                                     } else view.onFailed();
                                 }
                             }, new Action1<Throwable>() {
                                 @Override
                                 public void call(Throwable throwable) {
                                     view.onFailed();
                                 }
                             });
    }

    public void checkBatch(int coursetype, ArrangeBatchBody body) {
        spCheck = mRestRepository.getPost_api().qcCheckBatch(App.coachid+"",coursetype== Configs.TYPE_PRIVATE ? "private" : "group",coachService.getId()+"",coachService.getModel(),body)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override
                public void call(QcResponse qcResponse) {
                    if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                        view.checkOk();
                    } else {
                        view.checkFailed(qcResponse.getMsg());
                        //                    ToastUtils.logHttp(qcResponse);
                    }
                }
            },throwable -> {});

    }

    public void getBatchTemplete(int coursetype, String teacher_id, String course_id) {
        mRestRepository.getGet_api().qcGetBatchTemplate(App.coachid+"",coursetype== Configs.TYPE_PRIVATE ? "private" : "group",coachService.getId()+"",coachService.getModel(),teacher_id,course_id)
             .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                             .subscribe(new Action1<QcResponseBtachTemplete>() {
                                 @Override
                                 public void call(QcResponseBtachTemplete qcResponseBtachTemplete) {
                                     if (ResponseConstant.checkSuccess(qcResponseBtachTemplete)) {
                                         view.onTemplete(qcResponseBtachTemplete.data.rule, qcResponseBtachTemplete.data.time_repeats, qcResponseBtachTemplete.data.max_users);
                                     }
                                 }
                             }, new Action1<Throwable>() {
                                 @Override
                                 public void call(Throwable throwable) {
                                 }
                             });
        //spTmpl = gymUseCase.getBatchTemplete(coursetype, coachService.getId(), coachService.getModel(), teacher_id, course_id, new Action1<QcResponseBtachTemplete>() {
        //    @Override
        //    public void call(QcResponseBtachTemplete qcResponseBtachTemplete) {
        //        if (qcResponseBtachTemplete.getStatus() == ResponseConstant.SUCCESS) {
        //            view.onTemplete(qcResponseBtachTemplete.data.rule, qcResponseBtachTemplete.data.time_repeats, qcResponseBtachTemplete.data.max_users);
        //        } else {
        //            ToastUtils.logHttp(qcResponseBtachTemplete);
        //        }
        //    }
        //});
    }

}
