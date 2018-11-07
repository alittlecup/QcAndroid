package cn.qingchengfit.staffkit.presenters;

import android.support.v4.util.ArrayMap;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.ScoreStatus;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.utils.GymUtils;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ModuleConfigsPresenter extends BasePresenter {
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject StaffRespository restRepository;
    private MVPView view;

    @Inject public ModuleConfigsPresenter() {
    }

    @Override public void attachView(PView v) {
        view = (MVPView) v;
    }

    public void getModuleConfigs() {

        RxRegiste(restRepository.getStaffAllApi()
            .qcGetStudentScoreStatus(App.staffId, GymUtils.getParamsV2(gymWrapper.getCoachService(), null))
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcDataResponse<ScoreStatus>>() {
                @Override public void call(QcDataResponse<ScoreStatus> scoreStatusQcResponseData) {
                    view.onModuleStatus(scoreStatusQcResponseData.getData().getModule());
                }
            }, new NetWorkThrowable()));
    }

    public void putModuleConfigs(boolean open) {
        ArrayMap<String, Object> body = new ArrayMap<>();
        body.put("checkin", open);
        RxRegiste(restRepository.getStaffAllApi()
            .qcPutScoreStatus(App.staffId, GymUtils.getParamsV2(gymWrapper.getCoachService(), null), body)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcDataResponse>() {
                @Override public void call(QcDataResponse qcResponseData) {
                    if (ResponseConstant.checkSuccess(qcResponseData)) {
                        view.onStatusSuccess();
                    } else {
                        view.onShowError(qcResponseData.getMsg());
                    }
                }
            }, new NetWorkThrowable()));
    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public interface MVPView extends CView {
        void onModuleStatus(ScoreStatus.ModuleBean moduleBean);

        void onStatusSuccess();
    }
}
