package cn.qingchengfit.notisetting.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.SignInConfig;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.staffkit.constant.Get_Api;
import cn.qingcheng.gym.gymconfig.ShopConfigs;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class NotiSettingMsgDetailPresenter extends BasePresenter {
  @Inject GymWrapper gymWrapper;
  @Inject QcRestRepository qcRestRepository;
  @Inject LoginStatus loginStatus;
  private MVPView view;

  @Inject public NotiSettingMsgDetailPresenter() {
  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
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
                view.onCurSMSLeft(qcResponse.data.configs.get(0).getValueInt());
              }
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  public interface MVPView extends CView {
    void onCurSMSLeft(int x);
  }
}
