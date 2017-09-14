package cn.qingchengfit.staffkit.views.card.charge;

import android.content.Intent;
import android.text.TextUtils;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.inject.model.RealcardWrapper;
import cn.qingchengfit.model.body.ChargeBody;
import cn.qingchengfit.model.responese.CacluScore;
import cn.qingchengfit.model.responese.QcResponsePayWx;
import cn.qingchengfit.model.responese.Sellers;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.staffkit.model.dbaction.SerPermisAction;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.usecase.BuyCardUsecase;
import cn.qingchengfit.utils.GymUtils;
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
 * Created by Paper on 16/4/27 2016.
 */
public class CompletedChargePresenter extends BasePresenter {

    @Inject BuyCardUsecase buyCardUsecase;
    @Inject RealcardWrapper realCard;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject SerPermisAction serPermisAction;
    @Inject RestRepository mRestRepository;
    private CompletedChargeView view;
    private Subscription spSales;
    private Subscription spcharge;
    private Subscription spWx;

    @Inject public CompletedChargePresenter() {
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (CompletedChargeView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    boolean hasEditPermission() {
        return serPermisAction.check(PermissionServerUtils.CARDSETTING_CAN_CHANGE);
    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
        if (spSales != null) spSales.unsubscribe();
        if (spcharge != null) spcharge.unsubscribe();
        if (spWx != null) spWx.unsubscribe();
    }

    public void getSalers(String shopid) {

        spSales = buyCardUsecase.getSalers(gymWrapper.brand_id(), shopid, gymWrapper.id(), gymWrapper.model(),
            new Action1<QcDataResponse<Sellers>>() {
                @Override public void call(QcDataResponse<Sellers> qcResponseSalers) {
                    if (qcResponseSalers.getStatus() == ResponseConstant.SUCCESS) {
                        view.onSalers(qcResponseSalers.data.users);
                    } else {

                    }
                }
            });
    }

    public void commitCharge(final ChargeBody body, final String userIds) {
        //        body.setCard_id(realCard.id);
        spcharge =
            buyCardUsecase.chargeCard(realCard.id(), gymWrapper.brand_id(), body.getShop_id(), gymWrapper.id(), gymWrapper.model(), body,
                new Action1<QcResponsePayWx>() {
                    @Override public void call(QcResponsePayWx qcResponse) {
                        if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                            if (body.getCharge_type() < 6) {
                                //                        view.onSuccess();
                                cacluScore(body.getPrice(), userIds);
                            } else {
                                view.onWechat(qcResponse.data.url);
                            }
                        } else {
                            view.onFailed(qcResponse.getMsg());
                        }
                    }
                });
    }

    public void chargbyWX(ChargeBody body) {

        spWx =
            buyCardUsecase.chargeCardWX(realCard.id(), gymWrapper.brand_id(), body.getShop_id(), gymWrapper.id(), gymWrapper.model(), body,
                new Action1<QcResponsePayWx>() {
                    @Override public void call(QcResponsePayWx qcResponse) {
                        if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                            if (TextUtils.isEmpty(qcResponse.data.qr_code_url)) {
                                view.onWechat(qcResponse.data.qr_code_url);
                            } else {
                                view.onWechat(qcResponse.data.url);
                            }
                        } else {
                            view.onFailed(qcResponse.getMsg());
                        }
                    }
                });
    }

    void cacluScore(String price, final String user_ids) {
        RxRegiste(mRestRepository.getGet_api()
            .qcGetScoreCalu(App.staffId, Configs.CACLU_SCORE_CHARGE, price,
                GymUtils.getParamsV2(gymWrapper.getCoachService(), gymWrapper.getBrand()))
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<cn.qingchengfit.network.response.QcDataResponse<CacluScore>>() {
                @Override public void call(
                    cn.qingchengfit.network.response.QcDataResponse<CacluScore> cacluScoreQcResponseData) {
                    if (cacluScoreQcResponseData.getStatus() == 200) {
                        if (cacluScoreQcResponseData.getData().has_score) {
                            if (user_ids.contains(",") || user_ids.contains(Configs.SEPARATOR)) {
                                view.onScoreHint(cacluScoreQcResponseData.getData().score + "");
                            } else {
                                view.onSuccess();
                            }
                        } else {
                            view.onSuccess();
                        }
                    } else {
                        view.onFailed(cacluScoreQcResponseData.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    view.onFailed(throwable.getMessage());
                }
            }));
    }
}
