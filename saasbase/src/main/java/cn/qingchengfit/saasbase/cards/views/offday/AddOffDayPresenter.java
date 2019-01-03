package cn.qingchengfit.saasbase.cards.views.offday;

import android.content.Intent;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.cards.network.body.AddDayOffBody;
import cn.qingchengfit.saasbase.repository.ICardModel;
import cn.qingchengfit.subscribes.NetSubscribe;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
 * Created by Paper on 16/3/18 2016.
 */
public class AddOffDayPresenter extends BasePresenter {

    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject ICardModel cardModel;
    AddOffDayView view;

    @Inject public AddOffDayPresenter() {
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (AddOffDayView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public void commitOffDay(String cardId, String start, String end, String meg, String price, String remarks,int payment) {
        AddDayOffBody body = new AddDayOffBody(gymWrapper.model(), gymWrapper.id(), cardId);
        body.start = start;
        body.end = end;
        body.message = meg;
        body.price = price;
        body.remarks = remarks;
        body.charge_type=payment;
        RxRegiste(cardModel.qcAddDayOff(body)
            .onBackpressureLatest()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new NetSubscribe<QcDataResponse>() {
                @Override public void onNext(QcDataResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        view.onSucceess();
                    } else {
                        view.onFailed(qcResponse.getMsg());
                    }
                }
            }));
    }
}
