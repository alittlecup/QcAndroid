package com.qingchengfit.fitcoach.fragment.batch.single;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.GymUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.bean.Coach;
import com.qingchengfit.fitcoach.bean.CourseDetail;
import com.qingchengfit.fitcoach.bean.Rule;
import com.qingchengfit.fitcoach.bean.SingleBatch;
import com.qingchengfit.fitcoach.bean.SingleBatchBody;
import com.qingchengfit.fitcoach.bean.Space;
import com.qingchengfit.fitcoach.http.ResponseConstant;
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

    @Inject public SingleBatchPresenter(RestRepository restRepository) {
        this.restRepository = restRepository;
    }

    public void querySingleBatchId(String staffid, boolean isPrivate, String singlebatchid) {
        RxRegiste(restRepository.getGet_api()
            .qcGetSingleBatch(staffid, GymUtils.getCourseTypeStr(isPrivate), singlebatchid, GymUtils.getParams(coachService))
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

        void checkOk();

        void onSuccess();

        void onFailed();

        void onDelOk();
    }
}
