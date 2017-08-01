package cn.qingchengfit.staffkit.views.card.detail;

import android.content.Intent;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.inject.model.RealcardWrapper;
import cn.qingchengfit.model.responese.CardResponse;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.usecase.RealCardUsecase;
import javax.inject.Inject;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
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
public class RealCardDetailPresenter extends BasePresenter {

    @Inject RealCardUsecase usecase;
    @Inject RealcardWrapper realCard;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    RealCardDetailView view;
    @Inject RestRepository mRestRepository;
    private Subscription spUnregist;
    private Subscription spDetails;
    private Subscription spResume;

    @Inject public RealCardDetailPresenter() {
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (RealCardDetailView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
        if (spUnregist != null) spUnregist.unsubscribe();
        if (spDetails != null) spDetails.unsubscribe();
        if (spResume != null) spResume.unsubscribe();
    }

    //销卡
    public void unRegisteCard() {
        spUnregist =
            usecase.unRegisteRealCard(realCard.id(), gymWrapper.brand_id(), gymWrapper.id(), gymWrapper.model(), new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                        view.onSuccessUnregiste();
                    } else {
                        //                    ToastUtils.logHttp(qcResponse);
                    }
                }
            });
    }

    //回复会员卡
    public void resumeCard() {
        spResume = usecase.resumeCard(realCard.id(), gymWrapper.brand_id(), gymWrapper.id(), gymWrapper.model(), new Action1<QcResponse>() {
            @Override public void call(QcResponse qcResponse) {
                if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                    view.onSuccessResume();
                } else {
                    // ToastUtils.logHttp(qcResponse);
                }
            }
        });
    }

    public int getCardType() {
        return realCard.type();
    }

    public void queryGetCardDetail() {
        RxRegiste(mRestRepository.getGet_api()
            .qcGetCardDetail(App.staffId, realCard.id(), gymWrapper.getParams())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcDataResponse<CardResponse>>() {
                @Override public void call(QcDataResponse<CardResponse> qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        view.onGetRealCardDetail(qcResponse.data.card);
                    } else {
                        view.onFailed(qcResponse.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    view.onFailed(throwable.getMessage());
                }
            }));
    }
}
