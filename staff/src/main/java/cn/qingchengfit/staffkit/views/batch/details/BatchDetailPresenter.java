package cn.qingchengfit.staffkit.views.batch.details;

import android.content.Intent;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.body.ArrangeBatchBody;
import cn.qingchengfit.model.responese.QcResponse;
import cn.qingchengfit.model.responese.QcResponsePrivateBatchDetail;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.usecase.GymUseCase;
import cn.qingchengfit.utils.ToastUtils;
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
public class BatchDetailPresenter extends BasePresenter {

    GymUseCase gymUseCase;
    BatchDetailView view;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private Subscription sp;
    private Subscription spCheck;
    private Subscription spUpdate;
    private RestRepository restRepository;

    @Inject public BatchDetailPresenter(GymUseCase gymUseCase, RestRepository restRepository) {
        this.gymUseCase = gymUseCase;
        this.restRepository = restRepository;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (BatchDetailView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
        if (sp != null) sp.unsubscribe();
        if (spCheck != null) spCheck.unsubscribe();
        if (spUpdate != null) spUpdate.unsubscribe();
    }

    public void delbatch(String staffid, String batch_id) {
        RxRegiste(restRepository.getPost_api()
            .delBatch(staffid, batch_id, gymWrapper.getParams())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        view.onDelOk();
                    } else {
                        view.onFailed();
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    view.onFailed();
                }
            }));
    }

    public void queryData(String batch_id) {
        sp = gymUseCase.getBatchDetail(batch_id, gymWrapper.id(), gymWrapper.model(), null, new Action1<QcResponsePrivateBatchDetail>() {
            @Override public void call(QcResponsePrivateBatchDetail qcResponsePrivateBatchDetail) {
                if (qcResponsePrivateBatchDetail.getStatus() == ResponseConstant.SUCCESS) {
                    view.onCoach(qcResponsePrivateBatchDetail.data.batch.teacher);
                    view.onCourse(qcResponsePrivateBatchDetail.data.batch.course);
                    view.onRule(qcResponsePrivateBatchDetail.data.batch.rule, qcResponsePrivateBatchDetail.data.batch.max_users,
                        qcResponsePrivateBatchDetail.data.batch.is_free, qcResponsePrivateBatchDetail.data.batch.card_tpls,
                        qcResponsePrivateBatchDetail.data.batch.has_order);
                    view.onSpace(qcResponsePrivateBatchDetail.data.batch.spaces);
                    view.onTimeRepeat(qcResponsePrivateBatchDetail.data.batch.from_date, qcResponsePrivateBatchDetail.data.batch.to_date,
                        qcResponsePrivateBatchDetail.data.batch.time_repeats);
                } else {
                    view.onFailed();
                }
            }
        });
    }

    public void checkBatch(int coursetype, ArrangeBatchBody body) {
        spCheck = gymUseCase.checkBatch(gymWrapper.id(), gymWrapper.model(), coursetype, body, new Action1<QcResponse>() {
            @Override public void call(QcResponse qcResponse) {
                if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                    view.checkOk();
                } else {
                    view.onCheckFaild(qcResponse.getMsg());
                    ToastUtils.logHttp(qcResponse);
                }
            }
        });
    }

    public void updateBatch(String id, ArrangeBatchBody body) {
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
        upBody.is_free = body.is_free;

        spUpdate = gymUseCase.updateBatch(gymWrapper.id(), gymWrapper.model(), id, upBody, new Action1<QcResponse>() {
            @Override public void call(QcResponse qcResponse) {
                if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                    view.onSuccess();
                } else {
                    ToastUtils.logHttp(qcResponse);
                    view.onFailed();
                }
            }
        });
    }
}
