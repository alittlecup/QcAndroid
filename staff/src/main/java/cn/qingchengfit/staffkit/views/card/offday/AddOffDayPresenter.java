package cn.qingchengfit.staffkit.views.card.offday;

import android.content.Intent;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.Presenter;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.inject.model.RealcardWrapper;
import cn.qingchengfit.model.body.AddDayOffBody;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.usecase.RealCardUsecase;
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
 * Created by Paper on 16/3/18 2016.
 */
public class AddOffDayPresenter implements Presenter {

    @Inject RealcardWrapper realCard;
    @Inject RealCardUsecase usecase;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    AddOffDayView view;
    private Subscription sp;

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
        view = null;
        if (sp != null) sp.unsubscribe();
    }

    public void commitOffDay(String start, String end, String meg, String price, String remarks) {
        AddDayOffBody body = new AddDayOffBody(gymWrapper.model(), gymWrapper.id(), realCard.id());
        body.start = start;
        body.end = end;
        body.message = meg;
        body.price = price;
        body.remarks = remarks;
        sp = usecase.createDayOff(gymWrapper.brand_id(), gymWrapper.id(), gymWrapper.model(), body, new Action1<QcResponse>() {
            @Override public void call(QcResponse qcResponse) {
                if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                    view.onSucceess();
                } else {
                    view.onFailed(qcResponse.getMsg());
                }
            }
        });
    }
}
