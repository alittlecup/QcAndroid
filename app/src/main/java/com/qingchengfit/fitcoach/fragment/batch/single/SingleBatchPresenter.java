package com.qingchengfit.fitcoach.fragment.batch.single;

import com.anbillon.qcmvplib.CView;
import com.anbillon.qcmvplib.PView;
import com.qingchengfit.fitcoach.bean.Coach;
import com.qingchengfit.fitcoach.bean.CourseTypeSample;
import com.qingchengfit.fitcoach.bean.Rule;
import com.qingchengfit.fitcoach.bean.SingleBatchBody;
import com.qingchengfit.fitcoach.bean.Space;
import com.qingchengfit.fitcoach.di.BasePresenter;
import com.qingchengfit.fitcoach.http.RestRepository;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;

public class SingleBatchPresenter extends BasePresenter {
    private MVPView view;

    private RestRepository restRepository;
    @Inject
    public SingleBatchPresenter(RestRepository restRepository) {
        this.restRepository = restRepository;
    }


    public void querySingleBatchId(String staffid,boolean isPrivate,String singlebatchid){
        //RxRegiste(restRepository.getGet_api().qcGetSingleBatch(staffid, GymUtils.getCourseTypeStr(isPrivate),singlebatchid,gymWrapper.getParams())
        //     .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        //                     .subscribe(new Action1<QcResponseData<SingleBatchData>>() {
        //                         @Override
        //                         public void call(QcResponseData<SingleBatchData> qcResponse) {
        //                             if (ResponseConstant.checkSuccess(qcResponse)) {
        //                                 if (qcResponse.getData().schedule != null || qcResponse.getData().timetable!= null) {
        //                                     SingleBatch singleBatch = qcResponse.getData().schedule == null?qcResponse.getData().timetable:qcResponse.getData().schedule ;
        //                                     view.onCoach(singleBatch.teacher);
        //                                     view.onCourse(singleBatch.course);
        //                                     view.onDate(DateUtils.formatDateFromServer(singleBatch.start), DateUtils.formatDateFromServer(singleBatch.end));
        //                                     view.onRule(qcResponse.getData().schedule == null?singleBatch.rule:singleBatch.rules, singleBatch.max_users,singleBatch.is_free,
        //                                             singleBatch.card_tpls,singleBatch.has_order
        //                                     );
        //                                     view.onSpace(singleBatch.space,singleBatch.spaces);
        //                                 }
        //                             } else view.onShowError(qcResponse.getMsg());
        //                         }
        //                     }, new Action1<Throwable>() {
        //                         @Override
        //                         public void call(Throwable throwable) {
        //                             view.onShowError(throwable.getMessage());
        //                         }
        //                     })
        //);
    }

    public void saveSingleBatch(String staffid, String scheduleId,boolean isPrivate, SingleBatchBody body){
        //body.id = gymWrapper.id();
        //body.model = gymWrapper.model();
        //RxRegiste(restRepository.getPost_api().qcUpdateBatchSchedule(staffid,GymUtils.getCourseTypeStr(isPrivate),scheduleId, body)
        //        .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        //        .subscribe(new Action1<QcResponse>() {
        //            @Override
        //            public void call(QcResponse qcResponse) {
        //                if (ResponseConstant.checkSuccess(qcResponse)) {
        //                    view.onSuccess();
        //                } else view.onShowError(qcResponse.getMsg());
        //            }
        //        }, new Action1<Throwable>() {
        //            @Override
        //            public void call(Throwable throwable) {
        //                view.onShowError(throwable.getMessage());
        //            }
        //        })
        //);

    }

    public void delSingleBatchId(String staffid,boolean isPrivate,String singlebatchid){
        //DelBatchScheduleBody batchScheduleBody = new DelBatchScheduleBody();
        //batchScheduleBody.id = gymWrapper.id();
        //batchScheduleBody.model = gymWrapper.model();
        //batchScheduleBody.ids = singlebatchid;
        //RxRegiste(restRepository.getPost_api().qcDelBatchSchedule(staffid,GymUtils.getCourseTypeStr(isPrivate),batchScheduleBody)
        //     .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        //                     .subscribe(new Action1<QcResponse>() {
        //                         @Override
        //                         public void call(QcResponse qcResponse) {
        //                             if (ResponseConstant.checkSuccess(qcResponse)) {
        //                                 view.onDelOk();
        //                             } else view.onShowError(qcResponse.getMsg());
        //                         }
        //                     }, new Action1<Throwable>() {
        //                         @Override
        //                         public void call(Throwable throwable) {
        //                             view.onShowError(throwable.getMessage());
        //                         }
        //                     })
        //);
    }

    @Override
    public void attachView(PView v) {
        view = (MVPView) v;
    }

    public interface MVPView extends CView {
        void onCoach(Coach teacher);

        void onCourse(CourseTypeSample course);

        void onSpace(Space space, List<Space> spaces);

        void onRule(List<Rule> rules, int max_user, boolean isfree, List<Object> cardTplBatchShips, boolean hasOrder);
        void onDate(Date start, Date end);
        void checkOk();
        void onSuccess();
        void onFailed();
        void onDelOk();
    }
}
