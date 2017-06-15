package cn.qingchengfit.staffkit.views.setting;

import android.content.Intent;
import cn.qingchengfit.model.responese.QcResponse;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.staffkit.mvpbase.CommonPView;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.mvpbase.Presenter;
import cn.qingchengfit.staffkit.rxbus.RxBus;
import cn.qingchengfit.staffkit.rxbus.event.LoadingEvent;
import cn.qingchengfit.staffkit.usecase.SettingUseCase;
import cn.qingchengfit.staffkit.usecase.bean.FeedBackBody;
import javax.inject.Inject;
import rx.Subscription;
import rx.functions.Action1;

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
 * Created by Paper on 16/2/22 2016.
 */
public class ReportPresenter implements Presenter {

    private SettingUseCase useCase;
    private Subscription subcription;
    private CommonPView pView;

    @Inject public ReportPresenter(SettingUseCase useCase) {
        this.useCase = useCase;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        pView = (CommonPView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        if (subcription != null) subcription.unsubscribe();
    }

    public void postReport(FeedBackBody body) {
        RxBus.getBus().post(new LoadingEvent(true));
        subcription = useCase.report(body, new Action1<QcResponse>() {
            @Override public void call(QcResponse qcResponse) {
                RxBus.getBus().post(new LoadingEvent(false));
                if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                    pView.onSuccess();
                } else {

                }
            }
        });
    }
}
