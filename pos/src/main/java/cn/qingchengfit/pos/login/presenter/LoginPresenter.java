package cn.qingchengfit.pos.login.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.pos.login.model.Login;
import cn.qingchengfit.pos.login.model.LoginBody;
import cn.qingchengfit.pos.net.PosApi;
import cn.qingchengfit.utils.ToastUtils;
import javax.inject.Inject;
import rx.functions.Action1;

/**
 * Created by fb on 2017/10/10.
 */

public class LoginPresenter extends BasePresenter {

  @Inject QcRestRepository qcRestRepository;
  private MVPView view;

  @Inject
  public LoginPresenter(){
  }

  @Override public void attachView(PView v) {
    this.view = (MVPView) v;
  }

  public void qcLogin(LoginBody loginBody){
      //loginBody.setDevice_type("android");
      //loginBody.setPush_channel_id("");
      qcRestRepository.createPostApi(PosApi.class).qcLogin(loginBody).subscribe(new Action1<QcDataResponse<Login>>() {
        @Override public void call(QcDataResponse<Login> qcResponLogin) {
          if (qcResponLogin.getStatus() == ResponseConstant.SUCCESS) {
            view.onSuccess();
            //PreferenceUtils.setPrefString(App.context, Configs.PREFER_SESSION, qcResponLogin.data.session_id);
            ////PreferenceUtils.setPrefString(App.context, Configs.PREFER_PHONE, loginBody.phone);
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

  public interface MVPView extends CView{
    void onSuccess();

    void onFailed(String s);
  }

}
