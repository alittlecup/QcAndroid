package cn.qingchengfit.staffkit.views.gym;

import android.content.Intent;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.model.responese.CreatBrand;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.staffkit.rxbus.event.LoadingEvent;
import cn.qingchengfit.staffkit.usecase.InitUseCase;
import cn.qingchengfit.staffkit.usecase.bean.CreatBrandBody;
import javax.inject.Inject;
import rx.Subscription;
import rx.functions.Action1;

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
 * Created by Paper on 16/2/23 2016.
 */
public class CreateBrandPresenter extends BasePresenter {

    InitUseCase useCase;
    private Subscription spBrand;
    private AddBrandView addBrandView;

    @Inject public CreateBrandPresenter(InitUseCase useCase) {
        this.useCase = useCase;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        this.addBrandView = (AddBrandView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        this.addBrandView = null;
        if (spBrand != null) spBrand.unsubscribe();
    }

    public void createBrand(String brand, String s) {
        CreatBrandBody body = new CreatBrandBody();
        body.name = brand;
        body.photo = s;
        RxBus.getBus().post(new LoadingEvent(true));
        spBrand = useCase.createBrand(body, new Action1<QcDataResponse<CreatBrand>>() {
            @Override public void call(QcDataResponse<CreatBrand> qcResponsCreatBrand) {
                RxBus.getBus().post(new LoadingEvent(false));
                if (ResponseConstant.checkSuccess(qcResponsCreatBrand)) {
                    addBrandView.onSucceed(qcResponsCreatBrand.data);
                } else {
                    addBrandView.onFailed(qcResponsCreatBrand.msg);
                }
            }
        }, new Action1<Throwable>() {
            @Override public void call(Throwable throwable) {
                if (addBrandView != null) addBrandView.onFailed("网络错误");
            }
        });
    }
}
