package com.qingchengfit.fitcoach.fragment.batch.single;

import android.support.annotation.Nullable;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.GymUtils;
import cn.qingchengfit.utils.StringUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.bean.BatchOpenRule;
import com.qingchengfit.fitcoach.bean.Coach;
import com.qingchengfit.fitcoach.bean.CourseDetail;
import com.qingchengfit.fitcoach.bean.Rule;
import com.qingchengfit.fitcoach.bean.SingleBatch;
import com.qingchengfit.fitcoach.bean.SingleBatchBody;
import com.qingchengfit.fitcoach.bean.Space;
import com.qingchengfit.fitcoach.http.RestRepository;
import com.qingchengfit.fitcoach.http.bean.CardTplBatchShip;
import com.qingchengfit.fitcoach.http.bean.DelCourseManage;
import com.qingchengfit.fitcoach.http.bean.QcResponseSingleBatch;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class SingleBatchPresenter extends BasePresenter {
    @Inject CoachService coachService;
    private MVPView view;
    private RestRepository restRepository;
    private BatchOpenRule batchOpenRule = new BatchOpenRule();

    @Inject public SingleBatchPresenter(RestRepository restRepository) {
        this.restRepository = restRepository;
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



    public void querySingleBatchId(String staffid, boolean isPrivate, String singlebatchid) {
        RxRegiste(restRepository.getGet_api()
            .qcGetSingleBatch(staffid, GymUtils.getCourseTypeStr(isPrivate), singlebatchid, GymUtils.getParams(coachService))
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseSingleBatch>() {
                @Override public void call(QcResponseSingleBatch qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        if (qcResponse.getData().schedule != null || qcResponse.getData().timetable != null) {
                            SingleBatch singleBatch =
                                qcResponse.getData().schedule == null ? qcResponse.getData().timetable : qcResponse.getData().schedule;
                            view.onCoach(singleBatch.teacher);
                            view.onCourse(singleBatch.course);
                            view.onDate(DateUtils.formatDateFromServer(singleBatch.start), DateUtils.formatDateFromServer(singleBatch.end));
                            view.onRule(qcResponse.getData().schedule == null ? singleBatch.rule : singleBatch.rules, singleBatch.max_users,
                                singleBatch.is_free, singleBatch.card_tpls, singleBatch.has_order);
                            view.onSpace(singleBatch.space, singleBatch.spaces);
                            batchOpenRule = singleBatch.open_rule;
                            view.onBatchOpenRule(batchOpenRule);
                        }
                    } else {
                        view.onShowError(qcResponse.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    view.onShowError(throwable.getMessage());
                }
            }));
    }

    public void saveSingleBatch(String staffid, String scheduleId, boolean isPrivate, SingleBatchBody body) {
        body.id = coachService.getId() + "";
        body.model = coachService.getModel();
        RxRegiste(restRepository.getPost_api()
            .qcUpdateSinglebatch(staffid, GymUtils.getCourseTypeStr(isPrivate), scheduleId, body)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        view.onSuccess();
                    } else {
                        view.onShowError(qcResponse.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    view.onShowError(throwable.getMessage());
                }
            }));
    }

    public void delSingleBatchId(String staffid, boolean isPrivate, String singlebatchid) {
        DelCourseManage batchScheduleBody = new DelCourseManage();
        batchScheduleBody.id = coachService.getId() + "";
        batchScheduleBody.model = coachService.getModel();
        batchScheduleBody.ids = singlebatchid;
        RxRegiste(restRepository.getPost_api()
            .qcDelCourseManage(App.coachid, GymUtils.getCourseTypeStr(isPrivate), batchScheduleBody)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        view.onDelOk();
                    } else {
                        view.onShowError(qcResponse.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    view.onShowError(throwable.getMessage());
                }
            }));
    }

    @Override public void attachView(PView v) {
        view = (MVPView) v;
    }

    public interface MVPView extends CView {
        void onCoach(Coach teacher);

        void onCourse(CourseDetail course);

        void onSpace(Space space, List<Space> spaces);

        void onRule(List<Rule> rules, int max_user, boolean isfree, List<CardTplBatchShip> cardTplBatchShips, boolean hasOrder);

        void onDate(Date start, Date end);

        void onBatchOpenRule(BatchOpenRule batchOpenRule);

        void checkOk();

        void onSuccess();

        void onFailed();

        void onDelOk();
    }
}
