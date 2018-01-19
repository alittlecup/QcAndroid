package com.qingchengfit.fitcoach.fragment.statement.presenter;

import android.content.Intent;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.report.bean.CourseReportDetail;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.http.RestRepository;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by fb on 2017/5/16.
 */

public class CourseReversePresenter extends BasePresenter {

    public CourseView view;
    @Inject RestRepository restRepository;
    @Inject GymWrapper gymWrapper;

    @Inject public CourseReversePresenter(RestRepository restRepository) {
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
        RxRegiste(restRepository.getGet_api()
            .qcGetCourseReportDetail(String.valueOf(App.coachid), scheduleId, gymWrapper.getParams())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcDataResponse<CourseReportDetail>>() {
                @Override public void call(QcDataResponse<CourseReportDetail> courseReportDetailQcResponseData) {
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
