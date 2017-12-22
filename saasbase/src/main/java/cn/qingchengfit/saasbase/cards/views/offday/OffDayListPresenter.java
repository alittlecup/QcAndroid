package cn.qingchengfit.saasbase.cards.views.offday;

import android.content.Intent;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saasbase.cards.bean.DayOffs;
import cn.qingchengfit.saasbase.cards.bean.Leave;
import cn.qingchengfit.saasbase.cards.bean.OffDay;
import cn.qingchengfit.saasbase.cards.network.body.AheadOffDayBody;
import cn.qingchengfit.saasbase.repository.ICardModel;
import cn.qingchengfit.subscribes.NetSubscribe;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
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

    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject QcRestRepository restRepository;
    @Inject ICardModel cardModel;
    OffDayListView view;

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
        super.unattachView();
        view = null;
    }

    public void queryData(String cardId) {
        RxRegiste(cardModel.qcGetDayOffList(cardId)
            .onBackpressureLatest()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new NetSubscribe<QcDataResponse<DayOffs>>() {
                @Override public void onNext(QcDataResponse<DayOffs> qcResponse) {
                    if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                        List<OffDay> datas = new ArrayList<OffDay>();
                        for (Leave leave : qcResponse.data.leaves) {
                            datas.add(leave.toOffDay());
                        }
                        view.onOffDayList(datas);
                    }else{
                        view.onFailed(qcResponse.getMsg());
                    }
                }
            }));
    }

    public void cancleOffDay(String leaveId) {
        RxRegiste(cardModel.qcDelDayOff(leaveId)
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

    public void aheadOffDay(String offdayId, AheadOffDayBody body) {
        RxRegiste(cardModel.qcAheadOffDay(offdayId, body)
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
