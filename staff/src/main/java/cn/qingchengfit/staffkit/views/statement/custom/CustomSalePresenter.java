package cn.qingchengfit.staffkit.views.statement.custom;

import android.content.Intent;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.GymCardtpl;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.staffkit.constant.Get_Api;
import cn.qingchengfit.staffkit.constant.StaffAllApi;
import cn.qingchengfit.staffkit.usecase.StatementUsecase;
import cn.qingchengfit.utils.DateUtils;
import java.util.Date;
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
 * Created by Paper on 16/3/11 2016.
 */
public class CustomSalePresenter extends BasePresenter {

    @Inject StatementUsecase usecase;

    CustomSaleView customSaleView;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject QcRestRepository qcRestRepository;
    private String startTime, endTime;
    private String selectId;
    private String selectModel;
    private String courseId;
    private String courseName;
    private String shopid = "0";
    private Subscription spCards;

    @Inject public CustomSalePresenter(StatementUsecase usecase) {
        this.usecase = usecase;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        customSaleView = (CustomSaleView) v;
        startTime = DateUtils.Date2YYYYMMDD(new Date());
        endTime = DateUtils.Date2YYYYMMDD(new Date());
        //        spCards = usecase.queryCards(new Action1<QcResponseCards>() {
        //            @Override
        //            public void call(QcResponseCards qcResponseCards) {
        //                cardSystems.clear();
        //                for (QcResponseCards.CardSystem system : qcResponseCards.data.systems) {
        //                    for (Card card : system.card_tpls) {
        //                        card.system_id = system.system_id;
        //                        cardSystems.add(card);
        //                    }
        //                }
        //                Collections.sort(cardSystems, new CardComparator());
        //
        //            }
        //        });

    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        if (spCards != null) spCards.unsubscribe();
        this.customSaleView = null;
    }

    public void selectCard(String cardid) {
        courseId = cardid;
        //        gymId = cardSystems.get(pos).system_id;
        //        courseId = Integer.parseInt(cardSystems.get(pos).getId());
        //        courseName = cardSystems.get(pos).getName();
    }

    public void selectShopid(String shopid) {
        this.shopid = shopid;
    }

    public void selectStartTime(String start) {
        startTime = start;
    }

    public void selectEndTime(String end) {
        endTime = end;
    }

    public void queryCardTpl() {
        if (gymWrapper.inBrand()) {
            RxRegiste(qcRestRepository.createRxJava1Api(Get_Api.class)
                .qcGetBrandCardtpl(loginStatus.staff_id(), gymWrapper.brand_id())
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<QcDataResponse<GymCardtpl>>() {
                    @Override
                    public void call(QcDataResponse<GymCardtpl> gymCardtplQcDataResponse) {
                        if (ResponseConstant.checkSuccess(gymCardtplQcDataResponse)) {
                            if (customSaleView != null) {
                                customSaleView.onGetCards(gymCardtplQcDataResponse.data.card_tpls);
                            }
                        }
                    }
                }, new NetWorkThrowable()));
        } else {

            RxRegiste(qcRestRepository.createRxJava1Api(StaffAllApi.class)
                .qcGetGymCardtpl(loginStatus.staff_id(), gymWrapper.id(), gymWrapper.model(), null)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<QcDataResponse<GymCardtpl>>() {
                    @Override
                    public void call(QcDataResponse<GymCardtpl> gymCardtplQcDataResponse) {
                        if (ResponseConstant.checkSuccess(gymCardtplQcDataResponse)) {
                            if (customSaleView != null) {
                                customSaleView.onGetCards(gymCardtplQcDataResponse.data.card_tpls);
                            }
                        }
                    }
                }, new NetWorkThrowable()));
        }
    }
}
