package cn.qingchengfit.staffkit.views.setting;

import android.content.Intent;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.Presenter;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.usecase.SettingUseCase;
import cn.qingchengfit.staffkit.usecase.bean.FixPhoneBody;
import cn.qingchengfit.staffkit.usecase.bean.GetCodeBody;
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
public class FixPhonePresenter implements Presenter {

    private SettingUseCase useCase;
    private Subscription subscriptionQuery;
    private Subscription subcription;
    private FixPwView view;

    @Inject public FixPhonePresenter(SettingUseCase useCase) {
        this.useCase = useCase;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (FixPwView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        this.view = null;
        if (subcription != null) subcription.unsubscribe();
        if (subscriptionQuery != null) subscriptionQuery.unsubscribe();
    }

    public void fixPhone(FixPhoneBody body) {
        subcription = useCase.fixPhone(body, new Action1<QcResponse>() {
            @Override public void call(QcResponse qcResponse) {
                if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                    view.onSucceed();
                } else {
                    view.onError(qcResponse.getMsg());
                }
            }
        });
    }

    public void onQueryCode(GetCodeBody phone) {
        subscriptionQuery = useCase.getCode(phone, new Action1<QcResponse>() {
            @Override public void call(QcResponse qcResponse) {
                if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {

                }
            }
        });
    }
}
