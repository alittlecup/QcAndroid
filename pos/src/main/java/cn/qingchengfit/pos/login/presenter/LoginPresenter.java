package cn.qingchengfit.pos.login.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.pos.PosApp;
import cn.qingchengfit.pos.login.model.GetCodeBody;
import cn.qingchengfit.pos.login.model.Login;
import cn.qingchengfit.pos.login.model.LoginBody;
import cn.qingchengfit.pos.net.PosApi;
import cn.qingchengfit.saasbase.constant.Configs;
import cn.qingchengfit.utils.PreferenceUtils;
import java.util.HashMap;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by fb on 2017/10/10.
 */

public class LoginPresenter extends BasePresenter {

  @Inject QcRestRepository qcRestRepository;
  @Inject GymWrapper gymWrapper;
  @Inject LoginStatus loginStatus;
  private MVPView view;

  @Inject public LoginPresenter() {
  }

  @Override public void attachView(PView v) {
    this.view = (MVPView) v;
  }

  public void qcGetCode(String phone){
    HashMap<String, Object> params = new HashMap<>();
    params.put("phone", phone);
    params.put("gym_id", gymWrapper.id());
    params.put("area_code", "+86");
    RxRegiste(qcRestRepository.createPostApi(PosApi.class)
        .qcGetCode(
            new GetCodeBody.Builder().phone(phone).gym_id(gymWrapper.id()).area_code("+86").build())
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse>() {
          @Override public void call(QcDataResponse qcDataResponse) {
            if (qcDataResponse.getStatus() == ResponseConstant.SUCCESS){
              if (view != null){
                view.onGetCodeSuccess();
              }
            }else{
                view.onGetCodeFailed(qcDataResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  public void qcLogin(LoginBody loginBody) {
    RxRegiste(qcRestRepository.createPostApi(PosApi.class)
        .qcLogin(loginBody)
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<Login>>() {
          @Override public void call(QcDataResponse<Login> qcResponLogin) {
            if (qcResponLogin.getStatus() == ResponseConstant.SUCCESS) {
              view.onSuccess();
              Staff staff = new Staff(qcResponLogin.data.cashier.user);
              staff.setId(qcResponLogin.data.cashier.id);
              loginStatus.setLoginUser(staff);
              loginStatus.setUserId(qcResponLogin.data.cashier.user.id);
              PreferenceUtils.setPrefString(PosApp.context, Configs.PREFER_SESSION, qcResponLogin.data.session_id);
              PreferenceUtils.setPrefString(PosApp.context, Configs.PREFER_SESSION_ID, qcResponLogin.data.session_id);
            } else {
              view.onFailed(qcResponLogin.msg);
            }
          }
        }, new NetWorkThrowable()));
  }

  public interface MVPView extends CView {

    void onSuccess();

    void onFailed(String s);

    void onGetCodeSuccess();

    void onGetCodeFailed(String s);
  }
}
