package cn.qingchengfit.saasbase.gymconfig.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saasbase.constant.ShopConfigs;
import cn.qingchengfit.saasbase.gymconfig.IGymConfigModel;
import cn.qingchengfit.saasbase.gymconfig.bean.ShopConfig;
import cn.qingchengfit.saasbase.gymconfig.network.response.ShopConfigBody;
import cn.qingchengfit.saasbase.gymconfig.network.response.ShopConfigListWrap;
import cn.qingchengfit.subscribes.NetSubscribe;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class OrderLimitPresenter extends BasePresenter<OrderLimitPresenter.MVPView> {

  @Inject IGymConfigModel gymConfigModel;

  @Inject public OrderLimitPresenter() {

  }

  public void saveConfigs(ShopConfigBody body) {
    RxRegiste(gymConfigModel.saveShopConfigs(body)
      .onBackpressureBuffer()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action1<QcResponse>() {
        @Override public void call(QcResponse qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            mvpView.onShowError("保存成功");
            mvpView.popBack();
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

  public void queryOrderLimit(boolean isPrivate) {
    RxRegiste(gymConfigModel.getConfigs(
      isPrivate ? ShopConfigs.PRIVATE_MINUTES_CAN_ORDER : ShopConfigs.TEAM_MINUTES_CAN_ORDER)
      .onBackpressureBuffer()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse<ShopConfigListWrap>>() {
        @Override
        public void onNext(QcDataResponse<ShopConfigListWrap> shopConfigListWrapQcDataResponse) {
          if (ResponseConstant.checkSuccess(shopConfigListWrapQcDataResponse)){
            if (shopConfigListWrapQcDataResponse.data != null && shopConfigListWrapQcDataResponse.data.configs !=null &&
              shopConfigListWrapQcDataResponse.data.configs.size() > 0)
            mvpView.onCourseOrderLimit(shopConfigListWrapQcDataResponse.data.configs.get(0));
          }else {
            mvpView.onShowError(shopConfigListWrapQcDataResponse.getMsg());
          }
        }
      }));
  }

  public void querySignLimit(boolean isPrivate) {
    String keys = isPrivate ? ShopConfigs.PRIVATE_SIGN_CLASS_OPEN
      + ","
      + ShopConfigs.PRIVATE_SIGN_CLASS_WAY
      + ","
      + ShopConfigs.PRIVATE_SIGN_CLASS_START
      + ","
      + ShopConfigs.PRIVATE_SIGN_CLASS_END : ShopConfigs.GROUP_SIGN_CLASS_OPEN
      + ","
      + ShopConfigs.GROUP_SIGN_CLASS_WAY
      + ","
      + ShopConfigs.GROUP_SIGN_CLASS_START
      + ","
      + ShopConfigs.GROUP_SIGN_CLASS_END;
    RxRegiste(gymConfigModel.getConfigs(keys)
      .onBackpressureBuffer()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse<ShopConfigListWrap>>() {
        @Override
        public void onNext(QcDataResponse<ShopConfigListWrap> qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            if (qcResponse.getData().configs != null && qcResponse.getData().configs.size() > 0) {
              mvpView.onSignClassLimit(qcResponse.getData().configs);
            }
          } else {
            mvpView.onShowError(qcResponse.getMsg());
          }
        }
      }));
  }

  public void queryCancelLimit(boolean isPrivate) {
    RxRegiste(gymConfigModel.getConfigs(
        isPrivate ? ShopConfigs.PRIVATE_MINUTES_CAN_CANCEL : ShopConfigs.TEAM_MINUTES_CAN_CANCEL)
      .onBackpressureBuffer()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse<ShopConfigListWrap>>() {
        @Override
        public void onNext(QcDataResponse<ShopConfigListWrap> qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            if (qcResponse.getData().configs != null && qcResponse.getData().configs.size() > 0) {
              mvpView.onCourseCancelLimit(qcResponse.getData().configs.get(0));
            }
          } else {
            mvpView.onShowError(qcResponse.getMsg());
          }
        }
      }));
  }

  public void querySubstituteLimit(boolean isPrivate) {
    RxRegiste(gymConfigModel.getConfigs(
        isPrivate ? ShopConfigs.PRIVATE_MINUTES_CAN_ORDER : ShopConfigs.TEAM_MINUTES_CAN_ORDER)
      .onBackpressureBuffer()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse<ShopConfigListWrap>>() {
        @Override
        public void onNext(QcDataResponse<ShopConfigListWrap> qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            if (qcResponse.getData().configs != null && qcResponse.getData().configs.size() > 0) {
              mvpView.onCourseSubstituteLimit(qcResponse.getData().configs.get(0));
            }
          } else {
            mvpView.onShowError(qcResponse.getMsg());
          }
        }
      }));
  }

  public interface MVPView extends CView {

    /**
     * 团课预约限制 开始前xx分钟
     */
    void onCourseOrderLimit(ShopConfig config);

    /**
     * 团课取消预约限制
     */
    void onCourseCancelLimit(ShopConfig config);

    /**
     * 排队候补
     */
    void onCourseSubstituteLimit(ShopConfig config);


    void onSignClassLimit(List<ShopConfig> configs);
  }
}