package cn.qingchengfit.staffkit.views.gym;

import android.content.Intent;
import android.support.v4.app.Fragment;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.model.body.GymBody;
import cn.qingchengfit.model.common.Shop;
import cn.qingchengfit.model.responese.BrandsResponse;
import cn.qingchengfit.model.responese.QcResponseSystenInit;
import cn.qingchengfit.model.responese.ResponseService;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.staffkit.usecase.InitUseCase;
import cn.qingchengfit.staffkit.usecase.bean.SystemInitBody;
import cn.qingchengfit.utils.ToastUtils;
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
 * Created by Paper on 16/1/28 2016.
 */
public class SetGymPresenter extends BasePresenter {

  ISetGymView iSetGymView;
  InitUseCase initUseCase;
  @Inject StaffRespository mRestRepository;
  private Subscription spBrand;
  private Subscription spQueryBrand;
  private Subscription spCreateGym;

  @Inject public SetGymPresenter(InitUseCase useCase) {
    this.initUseCase = useCase;
  }

  @Override public void onStart() {

  }

  @Override public void onStop() {

  }

  @Override public void onPause() {

  }

  @Override public void attachView(PView v) {
    iSetGymView = (ISetGymView) v;
  }

  @Override public void attachIncomingIntent(Intent intent) {

  }

  @Override public void onCreate() {

  }

  @Override public void unattachView() {
    super.unattachView();
    iSetGymView = null;
    if (spQueryBrand != null) spQueryBrand.unsubscribe();
    if (spCreateGym != null) spCreateGym.unsubscribe();
    if (spBrand != null) spBrand.unsubscribe();
  }

  public void queryBrandList() {
    spQueryBrand = initUseCase.getBrandList(new Action1<QcDataResponse<BrandsResponse>>() {
      @Override public void call(QcDataResponse<BrandsResponse> qcResponse) {
        if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
          iSetGymView.onBrandList(qcResponse.data.brands);
        }
      }
    });
  }

  public void addGym(GymBody body) {
    spCreateGym = initUseCase.createGym(body, new Action1<QcResponse>() {
      @Override public void call(QcResponse qcResponse) {
        if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
          iSetGymView.onCreatGym();
        } else {
          // ToastUtils.logHttp(qcResponse);
        }
      }
    });
  }

  void initShop(SystemInitBody body) {
    RxRegiste(mRestRepository.getStaffAllApi()
        .qcSystemInit(body)
        .retry(new Func2<Integer, Throwable, Boolean>() {
          @Override public Boolean call(Integer integer, Throwable throwable) {
            return integer < 3 && throwable instanceof SocketTimeoutException;
          }
        })
        .doOnError(throwable -> {
          if (throwable != null) Timber.e("retrofit:" + throwable.getMessage());
        })
        .observeOn(AndroidSchedulers.mainThread())
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .subscribe(new Action1<QcResponseSystenInit>() {
          @Override public void call(QcResponseSystenInit qcResponseSystenInit) {
            if (ResponseConstant.checkSuccess(qcResponseSystenInit)) {
              iSetGymView.onSuccess(qcResponseSystenInit.data);
            } else {
              iSetGymView.onFailed();
              ToastUtils.showDefaultStyle(qcResponseSystenInit.getMsg());
            }
          }
        }));
  }

  void updateShop(String shopid, Shop shop) {
    RxRegiste(mRestRepository.getStaffAllApi()
        .qcPutService(App.staffId, shopid, shop)
        .observeOn(AndroidSchedulers.mainThread())
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .subscribe(new Action1<ResponseService>() {
          @Override public void call(ResponseService responseService) {
            if (ResponseConstant.checkSuccess(responseService)) {
              iSetGymView.onUpdateGym();
            } else {
              iSetGymView.onFailed();
              cn.qingchengfit.utils.ToastUtils.show(responseService.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  private void goFragment(Fragment fragment) {
    //        .getSupportFragmentManager().beginTransaction()
    //                .replace(R.id.frag,fragment)
    //                .addToBackStack(null)
    //                .commit();
  }
}
