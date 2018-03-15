package cn.qingchengfit.saasbase.login.views;

import android.net.Uri;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventLoginChange;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.constant.Configs;
import cn.qingchengfit.saasbase.gymconfig.IGymConfigModel;
import cn.qingchengfit.saasbase.login.ILoginModel;
import cn.qingchengfit.saasbase.login.bean.GetCodeBody;
import cn.qingchengfit.saasbase.login.bean.Login;
import cn.qingchengfit.saasbase.login.bean.LoginBody;
import cn.qingchengfit.saasbase.login.bean.RegisteBody;
import cn.qingchengfit.saasbase.permission.QcDbManager;
import cn.qingchengfit.saasbase.utils.StringUtils;
import cn.qingchengfit.subscribes.NetSubscribe;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.views.fragments.EventFreshCoachService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.tencent.qcloud.sdk.Constant;
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
  @Inject IGymConfigModel gymConfigModel;
  @Inject LoginStatus loginStatus;
  @Inject ILoginModel loginModel;
  @Inject QcDbManager studentAction;
  @Inject QcDbManager qCDbManager;

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
      qcResponLogin.data.staff.getUsername());
    PreferenceUtils.setPrefString(mvpContext, Configs.PREFER_USER_ID,
      qcResponLogin.getData().user.getId());
    SensorsDataAPI.sharedInstance(mvpContext.getApplicationContext()).login(qcResponLogin.getData().user.id);
    PreferenceUtils.setPrefString(mvpContext,"user_info",new Gson().toJson(qcResponLogin.data.user));
    studentAction.delAllStudent();
    mvpView.onShowLogining();
    getService(qcResponLogin);
  }

  public boolean isDebug() {
    return loginModel.isDebug();
  }

  private void initIM() {
    Constant.setAccountType(isDebug() ? 12162 : 12165);
    Constant.setSdkAppid(isDebug() ? 1400029014 : 1400029022);
    Constant.setXiaomiPushAppid("2882303761517568688");
    Constant.setBussId(isDebug() ? 609 : 604);
    Constant.setXiaomiPushAppkey("5651756859688");
    Constant.setHuaweiBussId(606);
    Constant.setUsername(
      mvpContext.getResources().getString(R.string.chat_user_id_header, loginStatus.getUserId()));
    Constant.setHost(Uri.parse(Configs.Server).getHost());
  }

  /**
   * 更新场馆信息
   */
  public void getService(final QcDataResponse<Login> qcResponLogin) {
    Staff staff = new Staff(qcResponLogin.data.user);
    staff.id = AppUtils.getCurApp(mvpContext) == 0? qcResponLogin.data.coach.getId():qcResponLogin.data.staff.getId();
    loginStatus.setLoginUser(staff);
    loginStatus.setSession(qcResponLogin.data.session_id);
    loginStatus.setUserId(qcResponLogin.data.user.getId());
    RxRegiste(gymConfigModel.qcGetCoachService(null)
      .onBackpressureDrop()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(gymListQcResponseData -> {
        mvpView.cancelLogin();
        if (ResponseConstant.checkSuccess(gymListQcResponseData)) {
          List<CoachService> services = gymListQcResponseData.getData().services;
          qCDbManager.writeGyms(services);
          if (services == null || services.size() == 0) {
            gymWrapper.setNoService(true);
          } else if (services.size() == 1) {
            gymWrapper.setBrand(new Brand.Builder().id(services.get(0).brand_id())
              .name(services.get(0).name())
              .build());
            gymWrapper.setCoachService(services.get(0));
          } else {
            gymWrapper.setBrand(new Brand.Builder().id(services.get(0).brand_id())
              .name(services.get(0).name())
              .build());
          }
          mvpView.onSuccess(1);
          initIM();
          RxBus.getBus().post(new EventFreshCoachService());
          RxBus.getBus().post(new EventLoginChange());
        }
      }, throwable -> {
        mvpView.cancelLogin();
        mvpView.onError(throwable.getMessage());
      }));
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
