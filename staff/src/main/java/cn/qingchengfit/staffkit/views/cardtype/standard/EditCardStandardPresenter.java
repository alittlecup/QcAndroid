package cn.qingchengfit.staffkit.views.cardtype.standard;

import android.content.Intent;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.model.LoginStatus;
import cn.qingchengfit.model.body.OptionBody;
import cn.qingchengfit.model.responese.QcResponse;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.mvpbase.Presenter;
import cn.qingchengfit.staffkit.usecase.CardStandardUsecase;
import javax.inject.Inject;
import rx.Subscription;
import rx.functions.Action1;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 16/3/28 2016.
 */
public class EditCardStandardPresenter implements Presenter {

    CardStandardUsecase usecase;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private EditCardStandardView view;
    private Subscription spDel;
    private Subscription spEdit;

    @Inject public EditCardStandardPresenter(CardStandardUsecase usecase) {
        this.usecase = usecase;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (EditCardStandardView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        view = null;
        if (spDel != null) spDel.unsubscribe();
        if (spEdit != null) spEdit.unsubscribe();
    }

    public void delStandard(String optionid) {
        spDel = usecase.DelCardstandard(gymWrapper.id(), gymWrapper.model(), gymWrapper.brand_id(), optionid, new Action1<QcResponse>() {
            @Override public void call(QcResponse qcResponse) {
                if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                    view.onSucceed();
                } else {
                    view.onFailed(qcResponse.getMsg());
                }
            }
        });
    }

    public void editStandard(OptionBody body) {
        OptionBody fix = new OptionBody();
        fix.can_charge = body.can_charge;
        fix.can_create = body.can_create;
        fix.charge = body.charge;
        fix.days = body.days;
        fix.description = body.description;
        fix.limit_days = body.limit_days;
        fix.price = body.price;

        spEdit =
            usecase.updateCardstandard(gymWrapper.id(), gymWrapper.model(), gymWrapper.brand_id(), body.id, fix, new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                        view.onSucceed();
                    } else {
                        view.onFailed(qcResponse.getMsg());
                    }
                }
            });
    }
}
