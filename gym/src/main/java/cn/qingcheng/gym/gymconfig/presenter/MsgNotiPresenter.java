package cn.qingcheng.gym.gymconfig.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingcheng.gym.gymconfig.IGymConfigModel;
import cn.qingcheng.gym.gymconfig.bean.ShopConfig;
import cn.qingcheng.gym.gymconfig.network.response.ShopConfigBody;
import cn.qingcheng.gym.gymconfig.network.response.ShopConfigListWrap;
import cn.qingchengfit.subscribes.NetSubscribe;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MsgNotiPresenter extends BasePresenter<MsgNotiPresenter.MVPView> {

  @Inject IGymConfigModel gymConfigModel;

  @Inject public MsgNotiPresenter() {
  }

  public void editShopConfig(ShopConfigBody body) {
    RxRegiste(gymConfigModel.saveShopConfigs(body)
      .onBackpressureBuffer()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action1<QcResponse>() {
        @Override public void call(QcResponse qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            mvpView.onEditOk();
          } else {
            mvpView.onShowError(qcResponse.getMsg());
          }
        }
      }, new Action1<Throwable>() {
        @Override public void call(Throwable throwable) {
          mvpView.onShowError(throwable.getMessage());
        }
      }));
  }

  public void queryShopConfig(String key) {
    RxRegiste(gymConfigModel.getConfigs(key)
      .onBackpressureBuffer()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse<ShopConfigListWrap>>() {
        @Override public void onNext(QcDataResponse<ShopConfigListWrap> qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            mvpView.onShopConfigs(qcResponse.data.configs);
          } else {
            mvpView.onShowError(qcResponse.getMsg());
          }
        }
      }));
  }

  public interface MVPView extends CView {
    void onShopConfigs(List<ShopConfig> configs);

    void onEditOk();
  }
}