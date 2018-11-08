package com.qingchengfit.fitcoach.fragment.statement.presenter;

import android.content.Intent;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.Utils.SaleCompare;
import com.qingchengfit.fitcoach.fragment.statement.SaleCardTypeView;
import com.qingchengfit.fitcoach.fragment.statement.StatementUsecase;
import com.qingchengfit.fitcoach.fragment.statement.model.QcResponseSaleDetail;
import com.qingchengfit.fitcoach.http.TrainerRepository;
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

    @Inject TrainerRepository mRestRepository;
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

    public void querySaleDetail(final String start, final String end) {
        RxRegiste(mRestRepository.getTrainerAllApi()
            .qcGetSaleDatail(String.valueOf(App.coachid), start, end, gymWrapper.getParams())
            .onBackpressureBuffer()
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
            }, new NetWorkThrowable()));
    }
}
