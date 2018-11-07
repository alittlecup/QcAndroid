package cn.qingchengfit.staffkit.views.main;

import android.content.Intent;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.BrandsResponse;
import cn.qingchengfit.model.responese.HomeInfo;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.staffkit.rxbus.custom.QcResponseOperator;
import cn.qingchengfit.utils.PreferenceUtils;
import java.util.HashMap;
import javax.inject.Inject;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

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
 * Created by Paper on 16/3/3 2016.
 */
public class HomePresenter extends BasePresenter {

    @Inject StaffRespository restRepository;
    @Inject LoginStatus loginStatus;
    @Inject SerPermisAction serPermisAction;

    private HomeView homeView;
    private Subscription sp;
    private Subscription spBrand;

    @Inject public HomePresenter(StaffRespository restRepository) {
        this.restRepository = restRepository;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        this.homeView = (HomeView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        this.homeView = null;
        if (sp != null) sp.unsubscribe();
        if (spBrand != null) spBrand.unsubscribe();
    }

    public void queryBrands() {
        spBrand = restRepository.getStaffAllApi()
            .qcGetBrands(loginStatus.staff_id()).onBackpressureBuffer().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .lift(new QcResponseOperator<QcDataResponse<BrandsResponse>>())
            .subscribe(new Action1<QcDataResponse<BrandsResponse>>() {
                @Override public void call(final QcDataResponse<BrandsResponse> qcResponseBrands) {
                    if (qcResponseBrands.getStatus() == ResponseConstant.SUCCESS) {
                        if (homeView != null) homeView.onBrands(qcResponseBrands.data.brands);
                    } else {

                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    public void updatePermission() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("brand_id", PreferenceUtils.getPrefString(App.context, Configs.CUR_BRAND_ID, ""));
        RxRegiste(restRepository.getStaffAllApi()
            .qcPermission(App.staffId, params).onBackpressureBuffer().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(qcResponse -> {
                if (ResponseConstant.checkSuccess(qcResponse) && qcResponse.data.permissions != null) {
                    serPermisAction.writePermiss(qcResponse.data.permissions);
                    queryHomeInfo();
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    Timber.e(throwable.getMessage());
                }
            }));
    }

    public void queryHomeInfo() {
        sp = restRepository.getStaffAllApi()
            .qcWelcomeHome(App.staffId, PreferenceUtils.getPrefString(App.context, Configs.CUR_BRAND_ID, ""))
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcDataResponse<HomeInfo>>() {
                @Override public void call(QcDataResponse<HomeInfo> qcResponse) {
                    if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                        homeView.setInfo(qcResponse.data.getStat());
                        homeView.setSpecialPoint(qcResponse.data.getQingcheng_activity_count());
                        App.gCanReload = true;
                    } else {
                    }
                }
            }, new NetWorkThrowable());
    }
}
