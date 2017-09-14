package com.qingchengfit.fitcoach.fragment.batch.details;

import android.content.Intent;
import android.support.annotation.Nullable;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.utils.GymUtils;
import cn.qingchengfit.utils.StringUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.bean.ArrangeBatchBody;
import com.qingchengfit.fitcoach.bean.BatchOpenRule;
import com.qingchengfit.fitcoach.http.RestRepository;
import com.qingchengfit.fitcoach.http.bean.QcResponsePrivateBatchDetail;
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

    CoachService coachService;
    BatchDetailView view;
    private Subscription sp;
    private Subscription spCheck;
    private Subscription spUpdate;
    private Brand brand;
    private RestRepository restRepository;
    private BatchOpenRule batchOpenRule = new BatchOpenRule();

    @Inject public BatchDetailPresenter(CoachService coachService, RestRepository restRepository, Brand brand) {
        this.coachService = coachService;
        this.restRepository = restRepository;
        this.brand = brand;
    }


    /**
     * 判断规则合法性（type //1 立即开放 2 固定时间 3 提前X小时）
     */
    @Nullable
    public BatchOpenRule getBatchOpenRule() {
        if (batchOpenRule == null){
            return null;
        }
        if (batchOpenRule.type == 2 && StringUtils.isEmpty(batchOpenRule.open_datetime)){
            return null;
        }
        if (batchOpenRule.type == 3 && batchOpenRule.advance_hours == null)
            return null;
        return batchOpenRule;
    }

    public void setBatchOpenRule(BatchOpenRule batchOpenRule) {
        this.batchOpenRule = batchOpenRule;
    }

    public void setOpenRuleType(int type){
        this.batchOpenRule.type = type;
    }

    public void setOpenRuleTime(String time,Integer abeadHoure){
        this.batchOpenRule.advance_hours = abeadHoure;
        this.batchOpenRule.open_datetime = time;
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
            .qcDelBatch(staffid, batch_id, GymUtils.getParams(coachService, brand))
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        view.onDelOk();
                    } else {
                        view.onFailed(qcResponse.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    view.onFailed("server error");
                }
            }));
    }

    public void queryData(String coachid, String batch_id) {
        RxRegiste(restRepository.getGet_api()
            .qcGetBatchDetail(coachid, batch_id, coachService.getId() + "", coachService.getModel())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponsePrivateBatchDetail>() {
                @Override public void call(QcResponsePrivateBatchDetail qcResponsePrivateBatchDetail) {
                    if (qcResponsePrivateBatchDetail.getStatus() == ResponseConstant.SUCCESS) {
                        view.onCoach(qcResponsePrivateBatchDetail.data.batch.teacher);
                        view.onCourse(qcResponsePrivateBatchDetail.data.batch.course);
                        view.onRule(qcResponsePrivateBatchDetail.data.batch.rule, qcResponsePrivateBatchDetail.data.batch.max_users);
                        view.onSpace(qcResponsePrivateBatchDetail.data.batch.spaces);
                        view.onTimeRepeat(qcResponsePrivateBatchDetail.data.batch.from_date,
                            qcResponsePrivateBatchDetail.data.batch.to_date);
                        batchOpenRule = qcResponsePrivateBatchDetail.data.batch.open_rule;
                        view.onOpenRule(batchOpenRule);
                    } else {

                    }
                }
            }, new NetWorkThrowable())

        );

        //sp = gymUseCase.getBatchDetail(batch_id, coachService.getId(), coachService.getModel(), null, new Action1<QcResponsePrivateBatchDetail>() {
        //    @Override
        //    public void call(QcResponsePrivateBatchDetail qcResponsePrivateBatchDetail) {
        //
        //    }
        //});
    }

    public void checkBatch(String coachid, int coursetype, ArrangeBatchBody body) {
        spCheck = restRepository.getPost_api()
            .qcCheckBatch(coachid, coursetype == Configs.TYPE_PRIVATE ? "private" : "group", coachService.getId() + "",
                coachService.getModel(), body).onBackpressureBuffer().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        view.checkOk();
                    } else {
                        view.onFailed(qcResponse.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    view.onFailed(throwable.getMessage());
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
        upBody.to_date = body.to_date;
        upBody.open_rule = body.open_rule;
        RxRegiste(
        spUpdate = restRepository.getPost_api()
            .qcUpdateBatch(App.coachid + "", id, coachService.getId() + "", coachService.getModel(), upBody)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        view.onSuccess();
                    }
                    view.onFailed(qcResponse.getMsg());
                }
            }, new NetWorkThrowable()));
    }
}
