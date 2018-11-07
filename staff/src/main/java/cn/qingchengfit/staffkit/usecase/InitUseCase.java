package cn.qingchengfit.staffkit.usecase;

import cn.qingchengfit.model.body.GymBody;
import cn.qingchengfit.model.responese.BrandsResponse;
import cn.qingchengfit.model.responese.CreatBrand;
import cn.qingchengfit.model.responese.QcResponseSystenInit;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.staffkit.usecase.bean.CreatBrandBody;
import cn.qingchengfit.staffkit.usecase.bean.SystemInitBody;
import cn.qingchengfit.utils.CrashUtils;
import java.net.SocketTimeoutException;
import javax.inject.Inject;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func2;
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
 * Created by Paper on 16/2/23 2016.
 */
public class InitUseCase {
  StaffRespository restRepository;

  @Inject public InitUseCase(StaffRespository restRepository) {
    this.restRepository = restRepository;
  }

  public Subscription createBrand(CreatBrandBody body, Action1<QcDataResponse<CreatBrand>> action1,
      Action1<Throwable> error) {
    return restRepository.getStaffAllApi()
        .qcCreatBrand(body)
        .retry(new Func2<Integer, Throwable, Boolean>() {
          @Override public Boolean call(Integer integer, Throwable throwable) {
            return integer < 3 && throwable instanceof SocketTimeoutException;
          }
        })
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(action1, error);
  }

  public Subscription systemInit(SystemInitBody body, Action1<QcResponseSystenInit> action1) {
    return restRepository.getStaffAllApi()
        .qcSystemInit(body)
        .retry(new Func2<Integer, Throwable, Boolean>() {
          @Override public Boolean call(Integer integer, Throwable throwable) {
            return integer < 3 && throwable instanceof SocketTimeoutException;
          }
        })
        .doOnError(throwable -> {
          if (throwable != null) Timber.e("retrofit:" + throwable.getMessage());
        })
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(action1, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            CrashUtils.sendCrash(throwable);
          }
        });
  }

  public Subscription getBrandList(Action1<QcDataResponse<BrandsResponse>> action1) {
    return restRepository.getStaffAllApi()
        .qcGetBrands(App.staffId)
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(action1, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            CrashUtils.sendCrash(throwable);
          }
        });
  }

  public Subscription createGym(GymBody body, Action1<QcResponse> action1) {
    return restRepository.getStaffAllApi()
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
