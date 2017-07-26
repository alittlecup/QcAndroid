package cn.qingchengfit.staffkit.presenters;

import android.support.v4.util.ArrayMap;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.model.responese.ScoreStatus;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcResponseData;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.CView;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.utils.GymUtils;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ModuleConfigsPresenter extends BasePresenter {
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject RestRepository restRepository;
    private MVPView view;

    @Inject public ModuleConfigsPresenter() {
    }

    @Override public void attachView(PView v) {
        view = (MVPView) v;
    }

    public void getModuleConfigs() {

        RxRegiste(restRepository.getGet_api()
            .qcGetStudentScoreStatus(App.staffId, GymUtils.getParamsV2(gymWrapper.getCoachService(), null))
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcResponseData<ScoreStatus>>() {
                @Override public void call(QcResponseData<ScoreStatus> scoreStatusQcResponseData) {
                    view.onModuleStatus(scoreStatusQcResponseData.getData().getModule());
                }
            }, new NetWorkThrowable()));
    }

    public void putModuleConfigs(boolean open) {
        ArrayMap<String, Object> body = new ArrayMap<>();
        body.put("checkin", open);
        RxRegiste(restRepository.getPost_api()
            .qcPutScoreStatus(App.staffId, GymUtils.getParamsV2(gymWrapper.getCoachService(), null), body)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseData>() {
                @Override public void call(QcResponseData qcResponseData) {
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
