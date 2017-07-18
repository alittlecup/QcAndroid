package cn.qingchengfit.staffkit.views.batch.single;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.body.DelBatchScheduleBody;
import cn.qingchengfit.model.body.SingleBatchBody;
import cn.qingchengfit.model.common.Rule;
import cn.qingchengfit.model.responese.CardTplBatchShip;
import cn.qingchengfit.model.responese.CourseTypeSample;
import cn.qingchengfit.model.responese.QcResponse;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.model.responese.SingleBatch;
import cn.qingchengfit.model.responese.SingleBatchData;
import cn.qingchengfit.model.responese.Space;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcResponseData;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.CView;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.GymUtils;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class SingleBatchPresenter extends BasePresenter {
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private MVPView view;
    private RestRepository restRepository;

    @Inject public SingleBatchPresenter(RestRepository restRepository) {
        this.restRepository = restRepository;
    }

    public void querySingleBatchId(String staffid, boolean isPrivate, String singlebatchid) {
        RxRegiste(restRepository.getGet_api()
            .qcGetSingleBatch(staffid, GymUtils.getCourseTypeStr(isPrivate), singlebatchid, gymWrapper.getParams())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseData<SingleBatchData>>() {
                @Override public void call(QcResponseData<SingleBatchData> qcResponse) {
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
            }, new NetWorkThrowable()));
    }

    public void saveSingleBatch(String staffid, String scheduleId, boolean isPrivate, SingleBatchBody body) {
        body.id = gymWrapper.id();
        body.model = gymWrapper.model();
        RxRegiste(restRepository.getPost_api()
            .qcUpdateBatchSchedule(staffid, GymUtils.getCourseTypeStr(isPrivate), scheduleId, body)
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
            }, new NetWorkThrowable()));
    }

    public void delSingleBatchId(String staffid, boolean isPrivate, String singlebatchid) {
        DelBatchScheduleBody batchScheduleBody = new DelBatchScheduleBody();
        batchScheduleBody.id = gymWrapper.id();
        batchScheduleBody.model = gymWrapper.model();
        batchScheduleBody.ids = singlebatchid;
        RxRegiste(restRepository.getPost_api()
            .qcDelBatchSchedule(staffid, GymUtils.getCourseTypeStr(isPrivate), batchScheduleBody)
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
            }, new NetWorkThrowable()));
    }

    @Override public void attachView(PView v) {
        view = (MVPView) v;
    }

    public interface MVPView extends CView {
        void onCoach(Staff teacher);

        void onCourse(CourseTypeSample course);

        void onSpace(Space space, List<Space> spaces);

        void onRule(List<Rule> rules, int max_user, boolean isfree, List<CardTplBatchShip> cardTplBatchShips, boolean hasOrder);

        void onDate(Date start, Date end);

        void checkOk();

        void onSuccess();

        void onFailed();

        void onDelOk();
    }
}
