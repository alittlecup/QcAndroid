package cn.qingchengfit.notisetting.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.WxAuthorWrap;
import cn.qingchengfit.staffkit.constant.Get_Api;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class NotiSettingWxTemplatePresenter extends BasePresenter {
  @Inject GymWrapper gymWrapper;
  @Inject LoginStatus loginStatus;
  @Inject QcRestRepository qcRestRepository;

  private MVPView view;

  @Inject public NotiSettingWxTemplatePresenter() {
  }

  public void queryWxTemplate() {
    RxRegiste(qcRestRepository.createRxJava1Api(Get_Api.class)
        .qcGetWxAuthor(loginStatus.staff_id(), gymWrapper.getParams())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<WxAuthorWrap>>() {
          @Override public void call(QcDataResponse<WxAuthorWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              view.onAuthored(qcResponse.data.gym.type, qcResponse.data.gym.auth_wechat != null &&(!qcResponse.data.gym.auth_wechat.equalsIgnoreCase("")),qcResponse.data.gym.is_ready);
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
    queryWxTemplate();
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  public interface MVPView extends CView {
    void onAuthored(int type,boolean author ,boolean isReady);
  }
}
