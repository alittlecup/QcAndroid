package cn.qingchengfit.card;

import cn.qingchengfit.card.network.CardApi;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.body.ChargeBody;
import cn.qingchengfit.model.body.CreateCardBody;
import cn.qingchengfit.model.responese.QcResponsePayWx;
import cn.qingchengfit.model.responese.Sellers;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
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
public class BuyCardUsecase {

    QcRestRepository restRepository;
@Inject LoginStatus loginStatus;
    @Inject public BuyCardUsecase(QcRestRepository restRepository) {
        this.restRepository = restRepository;
    }

    public Subscription getSalers(String brandid, String shop, String gymid, String gymmodle, Action1<QcDataResponse<Sellers>> action1) {
        return restRepository.createGetApi(CardApi.class)
            .qcGetSalersAndCoach(loginStatus.staff_id(), brandid, shop, gymid, gymmodle)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    public Subscription chargeCard(String card_id, String brand_id, String shop_id, String modelid, String model, ChargeBody body,
        Action1<QcResponsePayWx> action1) {
        return restRepository.createGetApi(CardApi.class)
            .qcCardCharge(loginStatus.staff_id(), card_id, brand_id, shop_id, modelid, model, body)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    public Subscription chargeCardWX(String card_id, String brand_id, String shop_id, String modelid, String model, ChargeBody body,
        Action1<QcResponsePayWx> action1) {
        body.setCard_id(card_id);
        //        CoachService coachService = gymBaseInfoAction.getGymByShopIdNow(PreferenceUtils.getPrefString(App.context, Configs.CUR_BRAND_ID,""),shop_id);
        //        body.setId(modelid);
        //        body.setModel(model);
        return restRepository.createGetApi(CardApi.class)
            .qcCardChargeWechat(loginStatus.staff_id()//, card_id
                , brand_id, shop_id, modelid, model, body)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    public Subscription buyCard(CreateCardBody body, String brand_id, String shop_id, String gymid, String gymmodel,
        Action1<QcResponsePayWx> action1) {
        return restRepository.createGetApi(CardApi.class)
            .qcCreateRealcard(loginStatus.staff_id(), body, brand_id, shop_id, gymid, gymmodel)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }
}
