package cn.qingchengfit.card.buy;

import android.content.Intent;
import cn.qingchengfit.card.BuyCardUsecase;
import cn.qingchengfit.card.network.CardApi;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.model.body.CreateCardBody;
import cn.qingchengfit.model.responese.CacluScore;
import cn.qingchengfit.model.responese.QcResponsePayWx;
import cn.qingchengfit.model.responese.Sellers;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.repository.ICardModel;
import cn.qingchengfit.saascommon.constant.Configs;
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
 * Created by Paper on 16/4/21 2016.
 */
public class CompletedBuyPresenter extends BasePresenter {

    BuyCardUsecase usecase;
    CompletedBuyView view;
    @Inject QcRestRepository mRestRepository;
    @Inject SerPermisAction serPermisAction;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject ICardModel cardModel;
    private Subscription spSalers;
    private Subscription spBuy;

    @Inject public CompletedBuyPresenter(BuyCardUsecase usecase) {
        this.usecase = usecase;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (CompletedBuyView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
        if (spSalers != null) spSalers.unsubscribe();
        if (spBuy != null) spBuy.unsubscribe();
    }

    boolean hasEditPermission() {
        return serPermisAction.check(PermissionServerUtils.CARDSETTING_CAN_CHANGE);
    }

    public void getSalers() {
        spSalers = usecase.getSalers(gymWrapper.brand_id(), gymWrapper.shop_id(), gymWrapper.id(), gymWrapper.model(),
            new Action1<QcDataResponse<Sellers>>() {
                @Override public void call(QcDataResponse<Sellers> qcResponseSalers) {
                    if (qcResponseSalers.getStatus() == ResponseConstant.SUCCESS) {
                        view.onSalers(qcResponseSalers.data.users);
                    } else {
                        view.onFailed(qcResponseSalers.getMsg());
                    }
                }
            });
    }

    public void buyCard(final CreateCardBody body) {
        //        String tmp = body.user_name;
        //        body.user_name = null;
        final CreateCardBody upBody = new CreateCardBody();
        upBody.charge_type = body.charge_type;
        upBody.account = body.account;
        upBody.card_no = body.card_no;
        upBody.card_tpl_id = body.card_tpl_id;
        upBody.check_valid = body.check_valid;
        upBody.end = body.end;
        upBody.price = body.price;
        upBody.remarks = body.remarks;
        upBody.seller_id = body.seller_id;
        upBody.start = body.start;
        upBody.times = body.times;
        upBody.user_ids = body.user_ids;
        upBody.shop_id = body.shop_id;
        upBody.valid_from = body.valid_from;
        upBody.valid_to = body.valid_to;
        upBody.is_auto_start = body.is_auto_start;
        //        upBody.user_name = null;
        spBuy = usecase.buyCard(upBody, null, null, gymWrapper.id(), gymWrapper.model(), new Action1<QcResponsePayWx>() {
            @Override public void call(QcResponsePayWx qcResponse) {
                if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                    if (body.charge_type < 6) {
                        cacluScore(body.price, body.user_ids);
                    } else {
                        view.onWxPay(qcResponse.data.url);
                    }
                } else {
                    view.onFailed(qcResponse.getMsg());
                }
            }
        });
    }

    public void cacluScore(String price, final String user_ids) {
        RxRegiste(cardModel
            .cacluScore(loginStatus.staff_id(), Configs.CACLU_SCORE_BUY, price,
                GymUtils.getParamsV2(gymWrapper.getCoachService(), gymWrapper.getBrand()))
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<cn.qingchengfit.network.response.QcDataResponse<CacluScore>>() {
                @Override public void call(
                    cn.qingchengfit.network.response.QcDataResponse<CacluScore> cacluScoreQcResponseData) {
                    if (ResponseConstant.checkSuccess(cacluScoreQcResponseData)) {
                        if (cacluScoreQcResponseData.getData().has_score) {
                            if (user_ids.contains(",") || user_ids.contains(Configs.SEPARATOR)) {
                                view.onScoreHint(cacluScoreQcResponseData.getData().number + "");
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
