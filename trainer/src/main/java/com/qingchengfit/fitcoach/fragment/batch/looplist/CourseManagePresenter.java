//package com.qingchengfit.fitcoach.fragment.batch.looplist;
//
//import android.content.Intent;
//
//import cn.qingchengfit.di.PView;
//import cn.qingchengfit.di.Presenter;
//import com.qingchengfit.fitcoach.Configs;
//import cn.qingchengfit.network.ResponseConstant;
//import cn.qingchengfit.model.base.CoachService;
//import com.qingchengfit.fitcoach.http.bean.FixBatchBean;
//import cn.qingchengfit.network.response.QcResponse;
//
//import javax.inject.Inject;
//
//import rx.Subscription;
//import rx.functions.Action1;
//
///**
// * power by
// * <p>
// * d8888b.  .d8b.  d8888b. d88888b d8888b.
// * 88  `8D d8' `8b 88  `8D 88'     88  `8D
// * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
// * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
// * 88      88   88 88      88.     88 `88.
// * 88      YP   YP 88      Y88888P 88   YD
// * <p>
// * <p>
// * Created by Paper on 16/5/6 2016.
// */
//public class CourseManagePresenter implements Presenter {
//
//    CoachService coachService;
//
//    private Subscription spQuery;
//    private CourseManageView view;
//    private Subscription spDel;
//
//    @Inject
//    public CourseManagePresenter(CoachService gymBase, GymUseCase useCase) {
//        this.coachService = gymBase;
//        this.useCase = useCase;
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
//        view = (CourseManageView) v;
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
//        if (spQuery != null)
//            spQuery.unsubscribe();
//        if (spDel != null)
//            spDel.unsubscribe();
//    }
//
//    public void queryList(String batchid, final int coursetype) {
//        spQuery = useCase.queryBatchSchedule(batchid, coursetype, null, coachService.getId(), coachService.getModel(), new Action1<QcResponseBatchSchedules>() {
//            @Override
//            public void call(QcResponseBatchSchedules qcResponseBatchSchedules) {
//                if (qcResponseBatchSchedules.getStatus() == ResponseConstant.SUCCESS) {
//                    if (coursetype == Configs.TYPE_PRIVATE && qcResponseBatchSchedules.data.timetables != null) {
//                        view.onList(qcResponseBatchSchedules.data.timetables);
//                    } else {
//                        view.onList(qcResponseBatchSchedules.data.schedules);
//                    }
//                } else {
//                    ToastUtils.logHttp(qcResponseBatchSchedules);
//                }
//            }
//        });
//    }
//
//    public void delSchedules(String ids, int courseType) {
//        DelBatchScheduleBody body = new DelBatchScheduleBody();
//        body.brand = null;
//        body.ids = ids;
//        body.id = coachService.getId();
//        body.model = coachService.getModel();
//        spDel = useCase.delBatchSchedule(body, courseType, new Action1<QcResponse>() {
//            @Override
//            public void call(QcResponse qcResponse) {
//                if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
//                    view.onDelSuccess();
//                } else {
//                    ToastUtils.logHttp(qcResponse);
//                }
//            }
//        });
//
//    }
//    public void updateSchedules(String id, FixBatchBean body, int courseType) {
//        spDel = useCase.updateBatchSchedule(body,id, courseType, new Action1<QcResponse>() {
//            @Override
//            public void call(QcResponse qcResponse) {
//                if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
//                    view.onFixSuccess();
//                } else {
//                    ToastUtils.logHttp(qcResponse);
//
//                }
//            }
//        });
//
//    }
//
//}
