package cn.qingchengfit.pos.login.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.pos.login.model.Gym;
import cn.qingchengfit.pos.login.model.Login;
import cn.qingchengfit.pos.login.model.LoginBody;
import cn.qingchengfit.pos.net.PosApi;
import cn.qingchengfit.utils.ToastUtils;
import java.util.HashMap;
import javax.inject.Inject;
import rx.functions.Action1;

/**
 * Created by fb on 2017/10/10.
 */

public class LoginPresenter extends BasePresenter {

  @Inject QcRestRepository qcRestRepository;
  @Inject GymWrapper gymWrapper;
  private MVPView view;

  @Inject public LoginPresenter() {
  }

  @Override public void attachView(PView v) {
    this.view = (MVPView) v;
  }

  public void qcGetGym(String iMEI){
    HashMap<String, Object> params = new HashMap<>();
    params.put("imei", iMEI);
    qcRestRepository.createGetApi(PosApi.class)
        .qcGetGym(params)
        .subscribe(new Action1<QcDataResponse<Gym>>() {
          @Override public void call(QcDataResponse<Gym> gymQcDataResponse) {
            if (gymQcDataResponse.getStatus() == ResponseConstant.SUCCESS){
              if (view != null){
                view.onGetGym(gymQcDataResponse.data);
              }else{
                view.onShowError(gymQcDataResponse.getMsg());
              }
            }
          }
        }, new NetWorkThrowable());
  }

  public void qcGetCode(String phone){
    HashMap<String, Object> params = new HashMap<>();
    params.put("phone", phone);
    params.put("gym_id", gymWrapper.id());
    qcRestRepository.createPostApi(PosApi.class)
        .qcGetCode(params)
        .subscribe(new Action1<QcDataResponse>() {
          @Override public void call(QcDataResponse qcDataResponse) {
            if (qcDataResponse.getStatus() == ResponseConstant.SUCCESS){
              if (view != null){
                view.onGetCodeSuccess();
              }else{
                view.onShowError(qcDataResponse.getMsg());
              }
            }
          }
        }, new NetWorkThrowable());
  }

  public void qcLogin(LoginBody loginBody) {
    qcRestRepository.createPostApi(PosApi.class)
        .qcLogin(loginBody)
        .subscribe(new Action1<QcDataResponse<Login>>() {
          @Override public void call(QcDataResponse<Login> qcResponLogin) {
            if (qcResponLogin.getStatus() == ResponseConstant.SUCCESS) {
              view.onSuccess();
              //PreferenceUtils.setPrefString(App.context, Configs.PREFER_SESSION, qcResponLogin.data.session_id);
              //PreferenceUtils.setPrefString(App.context, Configs.PREFER_PHONE, loginBody.phone);
              //PreferenceUtils.setPrefString(App.context, Configs.PREFER_WORK_ID, qcResponLogin.data.staff.getId());
              //
              //App.staffId = qcResponLogin.data.staff.getId();
              //PreferenceUtils.setPrefString(App.context, Configs.PREFER_WORK_NAME, qcResponLogin.data.staff.getUsername());
              //PreferenceUtils.setPrefString(App.context, Configs.PREFER_USER_ID, qcResponLogin.getData().user.getId());
              //studentAction.delAllStudent();
              //mLoginView.onShowLogining();
              //getService(qcResponLogin);
            } else {
              view.onFailed(qcResponLogin.msg);
            }
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            //mLoginView.cancelLogin();
            ToastUtils.show("系统维护中...请稍后再试");
          }
        });
  }

  public interface MVPView extends CView {

    void onGetGym(Gym gym);

    void onSuccess();

    void onFailed(String s);

    void onGetCodeSuccess();
  }
}
