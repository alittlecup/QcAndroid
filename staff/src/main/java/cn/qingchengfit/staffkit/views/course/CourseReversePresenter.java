package cn.qingchengfit.staffkit.views.course;

import android.content.Intent;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.responese.CourseReportDetail;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by fb on 2017/5/16.
 */

public class CourseReversePresenter extends BasePresenter {

    public CourseView view;
    @Inject StaffRespository restRepository;
    @Inject GymWrapper gymWrapper;

    @Inject public CourseReversePresenter(StaffRespository restRepository) {
        this.restRepository = restRepository;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        super.attachView(v);
        this.view = (CourseView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public void qcGetCourseReverse(String scheduleId) {
        RxRegiste(restRepository.getStaffAllApi()
            .qcGetCourseReportDetail(App.staffId, scheduleId, gymWrapper.getParams())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcDataResponse<CourseReportDetail>>() {
                @Override
                public void call(QcDataResponse<CourseReportDetail> courseReportDetailQcResponseData) {
                    if (courseReportDetailQcResponseData.status == 200) {
                        view.onGetSuccessed(courseReportDetailQcResponseData.data);
                    } else {
                        view.onGetFailed(courseReportDetailQcResponseData.getMsg());
                    }
                }
            }, new NetWorkThrowable()));
    }

    public interface CourseView extends PView {
        void onGetSuccessed(CourseReportDetail courseReportDetail);

        void onGetFailed(String msg);
    }
}
