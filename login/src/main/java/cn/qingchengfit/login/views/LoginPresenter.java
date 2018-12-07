package cn.qingchengfit.login.views;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.login.ILoginModel;
import cn.qingchengfit.login.bean.GetCodeBody;
import cn.qingchengfit.login.bean.Login;
import cn.qingchengfit.login.bean.LoginBody;
import cn.qingchengfit.login.bean.RegisteBody;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.saascommon.utils.StringUtils;
import cn.qingchengfit.subscribes.NetSubscribe;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
 * Created by Paper on 15/11/19 2015.
 */
public class LoginPresenter extends BasePresenter<LoginView> {

  public static final String TAG = LoginPresenter.class.getSimpleName();
  @Inject GymWrapper gymWrapper;
  @Inject LoginStatus loginStatus;
  @Inject ILoginModel loginModel;

  List<Object> list = new ArrayList<>();

  @Inject public LoginPresenter() {

  }

  @Override public void onStart() {
    String str = "";
    List<String> sts = new ArrayList<>();
    list.addAll(sts);
  }

  public void loginWx(String code) {
    JsonObject body = new JsonObject();
    body.addProperty("code", code);
    body.addProperty("session_config",true);
    RxRegiste(loginModel.wxLogin(body)
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse<Login>>() {
        @Override public void onNext(QcDataResponse<Login> qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            if (!StringUtils.isEmpty(qcResponse.data.wechat_openid)) {
              mvpView.toInit(qcResponse.data.wechat_openid);
            } else {
              loginDone(qcResponse);
            }
          } else {
            mvpView.onShowError(qcResponse.getMsg());
          }
        }
      }));
  }

  public void doLogin(final LoginBody loginBody) {
    mvpView.onShowLogining();

    loginBody.setDevice_type("android");
    loginBody.setPush_channel_id("");
    RxRegiste(loginModel.doLogin(loginBody)
      .onBackpressureDrop()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse<Login>>() {
        @Override public void onNext(QcDataResponse<Login> qcResponLogin) {
          if (qcResponLogin.getStatus() == ResponseConstant.SUCCESS) {
            loginDone(qcResponLogin);
          } else {
            mvpView.onError(qcResponLogin.msg);
          }
        }
      }));
  }

  void loginDone(QcDataResponse<Login> qcResponLogin) {
    QcRestRepository.setSession(mvpContext, qcResponLogin.data.session_name,
      qcResponLogin.data.session_id);
    QcRestRepository.setSessionDomain(mvpContext,qcResponLogin.data.session_domain);
    PreferenceUtils.setPrefString(mvpContext, Configs.PREFER_PHONE,qcResponLogin.getData().user.getPhone());
    if (qcResponLogin.data.staff != null)
      PreferenceUtils.setPrefString(mvpContext, Configs.PREFER_WORK_ID,
      qcResponLogin.data.staff.getId());
    if (qcResponLogin.data.coach != null)
      PreferenceUtils.setPrefString(mvpContext, Configs.PREFER_COACH_ID,
        qcResponLogin.data.coach.getId());
    loginModel.setStaffId(AppUtils.getCurApp(mvpContext)==0?qcResponLogin.data.coach.getId():qcResponLogin.data.staff.getId());
    PreferenceUtils.setPrefString(mvpContext, Configs.PREFER_WORK_NAME,
      qcResponLogin.data.user.getUsername());
    PreferenceUtils.setPrefString(mvpContext,Configs.PREFER_WORK_NAME_MIRROR,qcResponLogin.data.user.username);
    PreferenceUtils.setPrefString(mvpContext, Configs.PREFER_USER_ID,
      qcResponLogin.getData().user.getId());
    SensorsDataAPI.sharedInstance(mvpContext.getApplicationContext()).login(qcResponLogin.getData().user.id);
    PreferenceUtils.setPrefString(mvpContext,"user_info",new Gson().toJson(qcResponLogin.data.user));
    loginModel.doOnLogin(mvpContext,qcResponLogin.data,mvpView);
    mvpView.onShowLogining();
  }



  public boolean isDebug() {
    return loginModel.isDebug();
  }



  public void queryCode(GetCodeBody phone) {
    RxRegiste(loginModel.getCode(phone)
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse>() {
        @Override public void onNext(QcDataResponse qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
          } else {
            mvpView.onShowError(qcResponse.getMsg());
          }
        }
      }));
  }

  public void registe(final RegisteBody body) {
    mvpView.onShowLogining();
    if (StringUtils.isEmpty(body.username)) {
      mvpView.onError("请填写用户名");
      return;
    }
    if (StringUtils.isEmpty(body.phone)) {
      mvpView.onError("请填写电话号码");
    }
    if (StringUtils.isEmpty(body.code)) {
      mvpView.onError("请填写验证码");
    }
    if (StringUtils.isEmpty(body.getPassword())) {
      mvpView.onError("请填写密码");
    }
    body.session_config =true;
    RxRegiste(loginModel.doRegiste(body)
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse<Login>>() {
        @Override public void onNext(QcDataResponse<Login> qcResponLogin) {
          if (ResponseConstant.checkSuccess(qcResponLogin)) {
            loginDone(qcResponLogin);
          } else {
            mvpView.onShowError(qcResponLogin.getMsg());
          }
        }
      }));
  }
}
