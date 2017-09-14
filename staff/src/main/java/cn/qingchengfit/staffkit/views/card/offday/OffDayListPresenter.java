package cn.qingchengfit.staffkit.views.card.offday;

import android.content.Intent;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.inject.model.RealcardWrapper;
import cn.qingchengfit.model.body.AheadOffDayBody;
import cn.qingchengfit.model.common.OffDay;
import cn.qingchengfit.model.responese.DayOffs;
import cn.qingchengfit.model.responese.Leave;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.usecase.RealCardUsecase;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
 * Created by Paper on 16/3/24 2016.
 */
public class OffDayListPresenter extends BasePresenter {

    @Inject RealCardUsecase usecase;
    @Inject RealcardWrapper realCard;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject RestRepository restRepository;
    OffDayListView view;
    private Subscription Cancelsp;
    private Subscription querysp;

    @Inject public OffDayListPresenter() {
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (OffDayListView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        view = null;
        if (Cancelsp != null) Cancelsp.unsubscribe();
        if (querysp != null) querysp.unsubscribe();
        super.unattachView();
    }

    public void queryData() {
        querysp = usecase.getDayOffList(realCard.id(), gymWrapper.brand_id(), gymWrapper.id(), gymWrapper.model(),
            new Action1<QcDataResponse<DayOffs>>() {
                @Override public void call(QcDataResponse<DayOffs> qcResponseDayOffs) {
                    if (qcResponseDayOffs.getStatus() == ResponseConstant.SUCCESS) {
                        List<OffDay> datas = new ArrayList<OffDay>();
                        for (Leave leave : qcResponseDayOffs.data.leaves) {
                            datas.add(leave.toOffDay());
                        }
                        view.onOffDayList(datas);
                    }
                }
            });
    }

    public void cancleOffDay(String offdayid) {
        Cancelsp = usecase.cancelDayOff(offdayid, gymWrapper.brand_id(), gymWrapper.id(), gymWrapper.model(), new Action1<QcResponse>() {
            @Override public void call(QcResponse qcResponse) {
                if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                    view.onSucceess();
                } else {
                    view.onFailed(qcResponse.getMsg());
                }
            }
        });
    }

    public void aheadOffDay(String offdayId, AheadOffDayBody body) {
        RxRegiste(restRepository.getPost_api()
            .qcAheadDayOff(loginStatus.staff_id(), offdayId, gymWrapper.getParams(), body)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        view.onSucceess();
                    } else {
                        view.onFailed(qcResponse.getMsg());
                    }
                }
            }, new NetWorkThrowable()));
    }
}
