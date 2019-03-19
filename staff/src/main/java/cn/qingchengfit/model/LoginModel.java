package cn.qingchengfit.model;

import android.content.Context;
import android.net.Uri;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventLoginChange;
import cn.qingchengfit.login.ILoginModel;
import cn.qingchengfit.login.LoginApi;
import cn.qingchengfit.login.bean.CheckCodeBody;
import cn.qingchengfit.login.bean.GetCodeBody;
import cn.qingchengfit.login.bean.Login;
import cn.qingchengfit.login.bean.LoginBody;
import cn.qingchengfit.login.bean.RegisteBody;
import cn.qingchengfit.login.views.CheckProtocolModel;
import cn.qingchengfit.login.views.LoginView;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;

import cn.qingchengfit.saasbase.apis.GymConfigApi;
import cn.qingchengfit.saascommon.model.QcDbManager;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.BuildConfig;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.views.fragments.EventFreshCoachService;
import com.google.gson.JsonObject;
import com.tencent.qcloud.sdk.Constant;
import java.util.HashMap;
import java.util.List;
import rx.Observable;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2018/2/6.
 */

public class LoginModel implements ILoginModel {

  GymWrapper gymWrapper;
  LoginStatus loginStatus;
  LoginApi api;
  QcDbManager qcDbManager;
  GymConfigApi gymConfigApi;

  public LoginModel(GymWrapper gymWrapper, LoginStatus loginStatus,
    QcRestRepository qcRestRepository,QcDbManager qcDbManager) {
    this.gymWrapper = gymWrapper;
    this.loginStatus = loginStatus;
    api = qcRestRepository.createRxJava1Api(LoginApi.class);
    gymConfigApi = qcRestRepository.createRxJava1Api(GymConfigApi.class);
    this.qcDbManager = qcDbManager;
  }


  @Override public void setStaffId(String staffid) {
    App.staffId = staffid;
  }

  @Override public boolean isDebug() {
    return BuildConfig.DEBUG && BuildConfig.FLAVOR.equals("SIT");
  }

  @Override public void doOnLogin(Context ctx, Login login,LoginView mvpView) {
    qcDbManager.delAllStudent();
    getService(ctx,login,mvpView);
  }

  /**
   * 更新场馆信息
   */
  public void getService(Context mvpContext,final Login data,LoginView mvpView) {
    Staff staff = new Staff(data.user);
    staff.id = AppUtils.getCurApp(mvpContext) == 0? data.coach.getId():data.staff.getId();
    loginStatus.setLoginUser(staff);
    loginStatus.setSession(data.session_id);
    loginStatus.setUserId(data.user.getId());
    gymConfigApi.qcGetCoachService(staff.id,null)
      .onBackpressureDrop()
      .subscribeOn(rx.schedulers.Schedulers.newThread())
      .observeOn(rx.android.schedulers.AndroidSchedulers.mainThread())
      .subscribe(gymListQcResponseData -> {
        mvpView.cancelLogin();
        if (ResponseConstant.checkSuccess(gymListQcResponseData)) {
          List<CoachService> services = gymListQcResponseData.getData().services;
          qcDbManager.writeGyms(services);
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
          initIM(mvpContext);
          RxBus.getBus().post(new EventFreshCoachService());
          RxBus.getBus().post(new EventLoginChange());
        }
      }, throwable -> {
        mvpView.cancelLogin();
        mvpView.onError("登录错误");
      });
  }


  private void initIM(Context context) {
    Constant.setAccountType(isDebug() ? 12162 : 12165);
    Constant.setSdkAppid(isDebug() ? 1400029014 : 1400029022);
    Constant.setXiaomiPushAppid("2882303761517568688");
    Constant.setBussId(isDebug() ? 609 : 604);
    Constant.setXiaomiPushAppkey("5651756859688");
    Constant.setHuaweiBussId(606);
    Constant.setUsername(
      context.getResources().getString(R.string.chat_user_id_header, loginStatus.getUserId()));
    Constant.setHost(Uri.parse(Configs.Server).getHost());
  }


  @Override public Observable<QcDataResponse<Login>> doLogin(LoginBody body) {
    return api.qcLogin(body);
  }

  @Override public Observable<QcDataResponse<Login>> doRegiste(RegisteBody body) {
    return api.qcRegister(body);
  }

  @Override public Observable<QcDataResponse<Login>> wxLogin(JsonObject body) {
    return api.wxLogin(body);
  }

  @Override public Observable<QcDataResponse> unBindWx(CheckCodeBody body) {
    return api.unBindWx(body);
  }

  @Override public Observable<QcDataResponse> bindWx(CheckCodeBody body) {
    return api.bindWx(body);
  }

  @Override public Observable<QcDataResponse> getCode(GetCodeBody body) {
    return api.qcGetCode(body);
  }

  @Override public Observable<QcDataResponse> checkCode(CheckCodeBody body) {
    return api.qcCheckCode(body);
  }

  @Override public Observable<QcDataResponse<JsonObject>> checkPhoneBind(CheckCodeBody body) {
    return api.checkRegiste(body);
  }

  @Override public Observable<QcDataResponse<CheckProtocolModel>> qcCheckProtocol(
    HashMap<String, Object> params) {
    return api.qcCheckProtocol(params);
  }
}
