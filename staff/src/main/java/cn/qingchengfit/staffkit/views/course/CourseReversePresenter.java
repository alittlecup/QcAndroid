package cn.qingchengfit.staffkit.views.course;

import android.content.Intent;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.responese.CourseReportDetail;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
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
            .qcGetCourseReportDetail(App.staffId, scheduleId, gymWrapper.getParams())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<cn.qingchengfit.network.response.QcResponseData<CourseReportDetail>>() {
                @Override
                public void call(cn.qingchengfit.network.response.QcResponseData<CourseReportDetail> courseReportDetailQcResponseData) {
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
