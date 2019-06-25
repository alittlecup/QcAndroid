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
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.staffkit.constant.Get_Api;
import cn.qingchengfit.staffkit.constant.StaffAllApi;
import cn.qingchengfit.staffkit.usecase.StatementUsecase;
import cn.qingchengfit.utils.DateUtils;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 *
 */
public class CustomSigninPresenter extends BasePresenter {

    @Inject StatementUsecase usecase;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    PresenterView customSaleView;
    @Inject QcRestRepository qcRestRepository;
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
                .qcGetGymCardtpl(loginStatus.staff_id(), gymWrapper.id(), gymWrapper.model(),
                    null)
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

    public interface PresenterView extends PView {
        void onGetCards(List<CardTpl> cardtpls);
    }
}
