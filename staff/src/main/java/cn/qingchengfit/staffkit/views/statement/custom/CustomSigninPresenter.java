package cn.qingchengfit.staffkit.views.statement.custom;

import android.content.Intent;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.CardTpl;
import cn.qingchengfit.model.responese.CardTpls;
import cn.qingchengfit.model.responese.GymCardtpl;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.usecase.StatementUsecase;
import cn.qingchengfit.utils.DateUtils;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import rx.Subscription;
import rx.functions.Action1;

/**
 *
 */
public class CustomSigninPresenter extends BasePresenter {

    @Inject StatementUsecase usecase;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    PresenterView customSaleView;

    private String startTime, endTime;
    private String courseId;
    private String shopid = "0";
    private Subscription spCards;

    @Inject public CustomSigninPresenter(StatementUsecase usecase) {
        this.usecase = usecase;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        customSaleView = (PresenterView) v;
        startTime = DateUtils.Date2YYYYMMDD(new Date());
        endTime = DateUtils.Date2YYYYMMDD(new Date());
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
            RxRegiste(usecase.queryCardTypeList(gymWrapper.brand_id(), 0, new Action1<QcDataResponse<CardTpls>>() {
                @Override public void call(QcDataResponse<CardTpls> qcResponseCardTpls) {
                    if (ResponseConstant.checkSuccess(qcResponseCardTpls)) customSaleView.onGetCards(qcResponseCardTpls.data.card_tpls);
                }
            }));
        } else {
            RxRegiste(usecase.queryGymCardTpl(gymWrapper.id(), gymWrapper.model(), 0, new Action1<QcDataResponse<GymCardtpl>>() {
                @Override public void call(QcDataResponse<GymCardtpl> qcResponseGymCardtpl) {
                    if (ResponseConstant.checkSuccess(qcResponseGymCardtpl)) customSaleView.onGetCards(qcResponseGymCardtpl.data.card_tpls);
                }
            }));
        }
    }

    public interface PresenterView extends PView {
        void onGetCards(List<CardTpl> cardtpls);
    }
}
