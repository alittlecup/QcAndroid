package cn.qingchengfit.staffkit.views.statement.detail;

import android.content.Intent;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.model.LoginStatus;
import cn.qingchengfit.model.responese.QcResponseSaleDetail;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.usecase.NetError;
import cn.qingchengfit.staffkit.usecase.StatementUsecase;
import cn.qingchengfit.utils.SaleCompare;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
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
 * Created by Paper on 16/3/8 2016.
 */
public class SaleDetailPresenter extends BasePresenter {

    @Inject RestRepository mRestRepository;
    @Inject StatementUsecase usecase;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private SaleCardTypeView view;

    @Inject public SaleDetailPresenter(StatementUsecase usecase) {
        this.usecase = usecase;
    }

    @Override public void onStart() {
        super.onStart();
    }

    @Override public void onStop() {
        super.onStop();
    }

    @Override public void onPause() {
        super.onPause();
    }

    @Override public void attachView(PView v) {
        this.view = (SaleCardTypeView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {
        super.attachIncomingIntent(intent);
    }

    @Override public void onCreate() {
        super.onCreate();
    }

    @Override public void unattachView() {
        super.unattachView();
        this.view = null;
    }

    public void querySaleDetail(final String start, final String end, String shop_id, String card_id, String cards_extra, String seller_id,
        int type, int chargetype) {
        String shopid = shop_id;
        if (!gymWrapper.inBrand()) {
            shopid = null;
        }
        RxRegiste(mRestRepository.getGet_api()
            .qcGetSaleDatail(App.staffId, start, end, shopid, card_id, cards_extra, seller_id, type == 0 ? null : Integer.toString(type),
                chargetype == 0 ? null : Integer.toString(chargetype), gymWrapper.getParams())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseSaleDetail>() {
                @Override public void call(QcResponseSaleDetail qcResponseSaleDetail) {
                    if (qcResponseSaleDetail.getStatus() == ResponseConstant.SUCCESS) {
                        List<QcResponseSaleDetail.History> beans = qcResponseSaleDetail.data.histories;
                        Collections.sort(beans, new SaleCompare());
                        view.onSuccess(beans);
                    } else {

                    }
                }
            }, new NetError()));
    }
}
