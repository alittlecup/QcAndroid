package cn.qingchengfit.staffkit.views.setting;

import android.content.Intent;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saasbase.login.bean.GetCodeBody;
import cn.qingchengfit.staffkit.usecase.SettingUseCase;
import cn.qingchengfit.saasbase.user.bean.FixPhoneBody;
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
public class FixPhonePresenter extends BasePresenter {

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
        super.unattachView();
        this.view = null;
        if (subcription != null) subcription.unsubscribe();
        if (subscriptionQuery != null) subscriptionQuery.unsubscribe();
    }



    public void onQueryCode(GetCodeBody phone) {

    }
}
