package cn.qingchengfit.staffkit.views.cardtype.standard;

import android.content.Intent;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.body.OptionBody;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.usecase.CardStandardUsecase;
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
 * Created by Paper on 16/3/17 2016.
 */
public class AddCardStandardPresenter extends BasePresenter {

    CardStandardUsecase usecase;
    AddCardStandardView view;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private Subscription addSp;

    @Inject public AddCardStandardPresenter(CardStandardUsecase usecase) {
        this.usecase = usecase;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (AddCardStandardView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
        if (addSp != null) addSp.unsubscribe();
    }

    public void addCardstandard(String cardtpl_id, OptionBody body) {
        addSp = usecase.createCardstandard(cardtpl_id, gymWrapper.id(), gymWrapper.model(), gymWrapper.brand_id(), body,
            new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                        view.onSuccess();
                    } else {
                        view.onFailed(qcResponse.getMsg());
                    }
                }
            });
    }
}
