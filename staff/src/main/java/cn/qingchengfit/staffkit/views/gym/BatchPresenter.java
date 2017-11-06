package cn.qingchengfit.staffkit.views.gym;

import android.content.Intent;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.body.ArrangeBatchBody;
import cn.qingchengfit.model.responese.QcResponseSystenInit;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.usecase.GymUseCase;
import cn.qingchengfit.staffkit.usecase.InitUseCase;
import cn.qingchengfit.staffkit.usecase.bean.SystemInitBody;
import cn.qingchengfit.staffkit.views.batch.ManageCourseView;
import cn.qingchengfit.utils.ToastUtils;
import javax.inject.Inject;
import rx.Subscription;
import rx.functions.Action1;
import timber.log.Timber;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/2/29 2016.
 */
public class BatchPresenter extends BasePresenter {

    @Inject InitUseCase initUseCase;
    GymUseCase useCase;
    CoachService coachService;
    ManageCourseView view;
    private Subscription sp;
    private Subscription spArrange;

    @Inject public BatchPresenter(InitUseCase initUseCase, GymUseCase useCase, CoachService coachService) {
        this.initUseCase = initUseCase;
        this.useCase = useCase;
        this.coachService = coachService;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        this.view = (ManageCourseView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        this.view = null;
        if (sp != null) sp.unsubscribe();
        if (spArrange != null) spArrange.unsubscribe();
    }

    public void initGymShop() {
        SystemInitBody body = (SystemInitBody) App.caches.get("init");
        body.auto_trial = false;
        sp = initUseCase.systemInit(body, new Action1<QcResponseSystenInit>() {
            @Override public void call(QcResponseSystenInit qcResponseSystenInit) {
                if (ResponseConstant.checkSuccess(qcResponseSystenInit)) {
                    view.onSucceed(qcResponseSystenInit.data);
                } else {
                    view.onFailed();
                    Timber.e("initGymShop:error    " + qcResponseSystenInit.getMsg());
                    ToastUtils.showDefaultStyle(qcResponseSystenInit.getMsg());
                }
            }
        });
    }

    public void arrangeBatch(ArrangeBatchBody body) {
        spArrange = useCase.arrangeBatch(coachService.getId(), coachService.getModel(), body, new Action1<QcResponse>() {
            @Override public void call(QcResponse qcResponse) {
                if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {

                } else {

                }
            }
        });
    }
}
