package cn.qingchengfit.notisetting.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.body.RenewBody;
import cn.qingchengfit.model.responese.QcResponseRenew;
import cn.qingchengfit.model.responese.SignInConfig;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.staffkit.constant.Get_Api;
import cn.qingchengfit.staffkit.constant.Post_Api;
import cn.qingchengfit.staffkit.constant.ShopConfigs;
import cn.qingchengfit.staffkit.rxbus.event.PayEvent;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class NotiSettingMsgChargePresenter extends BasePresenter {
  @Inject GymWrapper gymWrapper;
  @Inject QcRestRepository qcRestRepository;
  @Inject LoginStatus loginStatus;
  private MVPView view;
  private int curCount = 0;

  @Inject public NotiSettingMsgChargePresenter() {
  }

  public void chargeSMS(int pos, String wxCode) {
    RxRegiste(qcRestRepository.createRxJava1Api(Post_Api.class)
        .qcCharge(new RenewBody.Builder().app_id(wxCode)
            .model(gymWrapper.model())
            .id(gymWrapper.id())
            .type("gym_sms")
            .count(getChargeCount(pos))
            .build())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcResponseRenew>() {
          @Override public void call(QcResponseRenew qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              view.onPayPage(qcResponse.data.url);
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  public void queryCurSMSleft() {
    RxRegiste(qcRestRepository.createRxJava1Api(Get_Api.class)
        .qcGetShopConfig(loginStatus.staff_id(), ShopConfigs.SMS_NUM, gymWrapper.getParams())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<SignInConfig.Data>>() {
          @Override public void call(QcDataResponse<SignInConfig.Data> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              if (qcResponse.data.configs != null && qcResponse.data.configs.size() > 0) {
                curCount = qcResponse.data.configs.get(0).getValueInt();
                view.onSmsLeft(curCount);
              }
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  @Override public void attachView(final PView v) {
    view = (MVPView) v;
    RxBusAdd(PayEvent.class).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<PayEvent>() {
          @Override public void call(PayEvent payEvent) {
            if (payEvent.result == 0) {
              ((MVPView) v).onPaySuccess();
            } else {
              ((MVPView) v).onPayFailed();
            }
          }
        });
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  public int getCurCount() {
    return curCount;
  }

  public void setCurCount(int curCount) {
    this.curCount = curCount;
  }

  public int getNewCount(int pos) {
    return curCount + getChargeCount(pos);
  }

  /**
   * 每一行都是左移一位
   * 垂直是 x 10
   */
  public int getChargeCount(int pos) {
    return (1000 * (pos / 3 <= 0 ? 1 : (pos / 3) * 10) << pos % 3);
  }

  public interface MVPView extends CView {
    void onPayPage(String s);

    void onPaySuccess();

    void onPayFailed();

    void onSmsLeft(int count);
  }
}
