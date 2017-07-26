package cn.qingchengfit.staffkit.usecase;

import cn.qingchengfit.model.body.GymBody;
import cn.qingchengfit.model.responese.BrandsResponse;
import cn.qingchengfit.model.responese.CreatBrand;
import cn.qingchengfit.model.responese.QcResponse;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.QcResponseSystenInit;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.usecase.bean.CreatBrandBody;
import cn.qingchengfit.staffkit.usecase.bean.SystemInitBody;
import cn.qingchengfit.utils.CrashUtils;
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
 * Created by Paper on 16/2/23 2016.
 */
public class InitUseCase {
    RestRepository restRepository;

    @Inject public InitUseCase(RestRepository restRepository) {
        this.restRepository = restRepository;
    }

    public Subscription createBrand(CreatBrandBody body, Action1<QcResponseData<CreatBrand>> action1, Action1<Throwable> error) {
        return restRepository.qcCreateBrand(body)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(action1, error);
    }

    public Subscription systemInit(SystemInitBody body, Action1<QcResponseSystenInit> action1) {
        return restRepository.qcSystemInit(body).onBackpressureBuffer().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    CrashUtils.sendCrash(throwable);
                }
            });
    }

    public Subscription getBrandList(Action1<QcResponseData<BrandsResponse>> action1) {
        return restRepository.getGet_api()
            .qcGetBrands(App.staffId).onBackpressureBuffer().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    CrashUtils.sendCrash(throwable);
                }
            });
    }

    public Subscription createGym(GymBody body, Action1<QcResponse> action1) {
        return restRepository.getPost_api()
            .qcCreateGym(App.staffId, body.brand_id, body)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    CrashUtils.sendCrash(throwable);
                }
            });
    }
}
