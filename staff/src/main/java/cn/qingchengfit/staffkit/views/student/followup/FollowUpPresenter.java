package cn.qingchengfit.staffkit.views.student.followup;

import android.content.Intent;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.responese.FollowUpDataStatistic;
import cn.qingchengfit.model.responese.SettingUsecase;
import cn.qingchengfit.model.responese.StaffResponse;
import cn.qingchengfit.model.responese.StudentTrackPreview;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.utils.DateUtils;
import java.util.Date;
import java.util.HashMap;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //
 * //Created by yangming on 16/12/11.
 */
public class FollowUpPresenter extends BasePresenter {

    public PresenterView view;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private RestRepository restRepository;
    private SettingUsecase usecase;

    @Inject public FollowUpPresenter(RestRepository restRepository, SettingUsecase usecase) {
        this.restRepository = restRepository;
        this.usecase = usecase;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        super.attachView(v);
        this.view = (PresenterView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public void getStudentTrackPreview() {
        HashMap<String, Object> params = gymWrapper.getParams();
        RxRegiste(restRepository.getGet_api()
            .qcGetTrackStudentPreview(App.staffId, params)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcDataResponse<StudentTrackPreview>>() {
                @Override public void call(QcDataResponse<StudentTrackPreview> studentTrackPreviewQcResponseData) {
                    view.onTrackPreview(studentTrackPreviewQcResponseData.data);
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    Timber.e(throwable.getMessage());
                    view.onShowError(throwable.getMessage());
                }
            }));
    }

    public void getStudentsStatistics() {
        HashMap<String, Object> params = gymWrapper.getParams();
        params.put("start", DateUtils.minusDay(new Date(), 29));
        params.put("end", DateUtils.getStringToday());
        RxRegiste(restRepository.getGet_api()
            .qcGetTrackStudentsStatistics(App.staffId, params)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcDataResponse<FollowUpDataStatistic>>() {
                @Override public void call(QcDataResponse<FollowUpDataStatistic> trackStudentsQcResponseData) {
                    view.onFollowUpStatistics(trackStudentsQcResponseData.data);
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    Timber.e(throwable.getMessage());
                    view.onShowError(throwable.getMessage());
                }
            }));
    }

    public void getSelfInfo() {
        usecase.getSelfInfo(new Action1<QcDataResponse<StaffResponse>>() {
            @Override public void call(QcDataResponse<StaffResponse> qcResponseSelfInfo) {
                if (qcResponseSelfInfo.getStatus() == ResponseConstant.SUCCESS) {
                    view.onSelfInfo(qcResponseSelfInfo.data.staff);
                } else {

                }
            }
        });
    }

    public interface PresenterView extends PView {
        void onTrackPreview(StudentTrackPreview preview);

        void onFollowUpStatistics(FollowUpDataStatistic statistics);

        void onSelfInfo(Staff bean);

        void onShowError(String e);
    }
}