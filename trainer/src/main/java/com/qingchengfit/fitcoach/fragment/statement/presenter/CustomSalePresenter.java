package com.qingchengfit.fitcoach.fragment.statement.presenter;

import android.content.Intent;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.cards.network.response.CardTplListWrap;
import cn.qingchengfit.saasbase.report.bean.GymCardtpl;
import cn.qingchengfit.utils.DateUtils;
import com.qingchengfit.fitcoach.fragment.statement.CustomSaleView;
import com.qingchengfit.fitcoach.fragment.statement.StatementUsecase;
import java.util.Date;
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
 * Created by Paper on 16/3/11 2016.
 */
public class CustomSalePresenter extends BasePresenter {

    @Inject StatementUsecase usecase;

    CustomSaleView customSaleView;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
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
            RxRegiste(usecase.queryCardTypeList(gymWrapper.brand_id(), 0, new Action1<QcDataResponse<CardTplListWrap>>() {
                @Override public void call(QcDataResponse<CardTplListWrap> qcResponseCardTpls) {
                    if (ResponseConstant.checkSuccess(qcResponseCardTpls)) customSaleView.onGetCards(qcResponseCardTpls.data.card_tpls);
                }
            }));
        } else {
            RxRegiste(usecase.queryGymCardTpl(0, new Action1<QcDataResponse<GymCardtpl>>() {
                @Override public void call(QcDataResponse<GymCardtpl> qcResponseGymCardtpl) {
                    if (ResponseConstant.checkSuccess(qcResponseGymCardtpl)) customSaleView.onGetCards(qcResponseGymCardtpl.data.card_tpls);
                }
            }, gymWrapper.getParams()));
        }
    }
}
