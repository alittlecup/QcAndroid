package cn.qingchengfit.staffkit.views.batch.addbatch;

import android.content.Intent;
import android.support.annotation.Nullable;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.Presenter;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.body.ArrangeBatchBody;
import cn.qingchengfit.model.common.BatchOpenRule;
import cn.qingchengfit.model.responese.ScheduleTemplete;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.usecase.GymUseCase;
import cn.qingchengfit.utils.StringUtils;
import cn.qingchengfit.utils.ToastUtils;
import javax.inject.Inject;
import rx.Subscription;
import rx.functions.Action1;

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
public class AddBatchPresenter implements Presenter {
    GymUseCase gymUseCase;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private AddBatchView view;
    private Subscription sp;
    private Subscription spCheck;
    private Subscription spTmpl;

    private BatchOpenRule batchOpenRule = new BatchOpenRule();

    @Inject public AddBatchPresenter(GymUseCase gymUseCase) {
        this.gymUseCase = gymUseCase;
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
        view = (AddBatchView) v;
        if (sp != null) sp.unsubscribe();
        if (spCheck != null) spCheck.unsubscribe();
        if (spTmpl != null) spTmpl.unsubscribe();
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        view = null;
        if (sp != null) sp.unsubscribe();
        if (spCheck != null) spCheck.unsubscribe();
    }

    public void arrangeBatch(ArrangeBatchBody body) {
        sp = gymUseCase.arrangeBatch(gymWrapper.id(), gymWrapper.model(), body, new Action1<QcResponse>() {
            @Override public void call(QcResponse qcResponse) {
                if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                    view.onSuccess();
                } else {
                    view.onFailed(qcResponse.getMsg());
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
                    view.checkFailed(qcResponse.getMsg());
                }
            }
        });
    }

    public void getBatchTemplete(int coursetype, String teacher_id, String course_id) {
        spTmpl = gymUseCase.getBatchTemplete(coursetype, gymWrapper.id(), gymWrapper.model(), teacher_id, course_id,
            new Action1<QcDataResponse<ScheduleTemplete>>() {
                @Override public void call(QcDataResponse<ScheduleTemplete> qcResponseBtachTemplete) {
                    if (qcResponseBtachTemplete.getStatus() == ResponseConstant.SUCCESS) {
                        view.onTemplete(qcResponseBtachTemplete.data.is_free, qcResponseBtachTemplete.data.rule,
                            qcResponseBtachTemplete.data.time_repeats, qcResponseBtachTemplete.data.max_users);
                    } else {
                        ToastUtils.logHttp(qcResponseBtachTemplete);
                    }
                }
            });
    }
}
