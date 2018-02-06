package cn.qingchengfit.saasbase.login.views;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.saasbase.login.ILoginModel;
import java.util.HashMap;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by fb on 2017/9/11.
 */

public class CheckProtocolPresenter extends BasePresenter<CheckProtocolPresenter.MVPView> {

  @Inject ILoginModel loginModel;

  @Inject public CheckProtocolPresenter() {
  }



  public void getIsAgree(String phone) {
    HashMap<String, Object> params = new HashMap<>();
    params.put("phone", phone);
    RxRegiste(loginModel
        .qcCheckProtocol(params)
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(checkProtocolModelQcResponseData -> {
          if (ResponseConstant.checkSuccess(checkProtocolModelQcResponseData)) {
            if (mvpView != null) {
              mvpView.onCheck(checkProtocolModelQcResponseData.data.has_read_agreement);
            }
          } else {
            mvpView.showAlert(checkProtocolModelQcResponseData.getMsg());
          }
        }, new NetWorkThrowable()));
  }

  public interface MVPView extends CView{
    void onCheck(boolean isAgree);

    void showAlert(String s);
  }
}
